package com.connie.noted.data.source

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.connie.noted.NotedApplication
import com.connie.noted.R
import com.connie.noted.board.BoardTypeFilter
import com.connie.noted.data.Board
import com.connie.noted.data.Note
import com.connie.noted.data.Result
import com.connie.noted.data.User
import com.connie.noted.login.UserManager
import com.connie.noted.util.Logger
import com.connie.noted.util.Util.replaceBr
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object NotedRemoteDataSource : NotedDataSource {


    private const val PATH_NOTES = "notes"
    private const val PATH_BOARDS = "boards"
    private const val KEY_CREATED_TIME = "createdTime"


    override fun getLiveNotes(): MutableLiveData<MutableList<Note>> {

        val liveData = MutableLiveData<MutableList<Note>>()

        UserManager.userEmail?.let { email ->

            FirebaseFirestore.getInstance()
                .collection(PATH_NOTES)
                .whereEqualTo("createdBy", email)
                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, exception ->

                    Logger.i("Firebase: Notes changed, addSnapshotListener detect")

                    exception?.let {

                        Logger.w("Firebase: Error getting documents. ${it.message}")
                    }

                    val list = mutableListOf<Note>()

                    for (document in snapshot!!) {

                        Logger.v("Firebase: ${document.id} => ${document.data["title"]}, ${document.data["url"]}")

                        val note = document.toObject(Note::class.java)

                        note.summary = replaceBr(note.summary ?: "")

                        list.add(note)

                    }

                    if (list.isNullOrEmpty()) {

                        val initList = mutableListOf<Note>()

                        FirebaseFirestore.getInstance()
                            .collection(PATH_NOTES)
                            .document("0000")
                            .get()
                            .addOnSuccessListener { document ->

                                val initNote = document.toObject(Note::class.java)

                                initNote?.let { note ->

                                    note.summary = replaceBr(note.summary ?: "")
                                    initList.add(note)

                                    liveData.value = initList
                                }
                            }

                    } else {

                        liveData.value = list

                    }
                }
        }

        return liveData

    }

    override fun getLiveGlobalBoards(condition: String): MutableLiveData<List<Board>> {

        val liveData = MutableLiveData<List<Board>>()

        UserManager.userEmail?.let { email ->

            val list = mutableListOf<Board>()

            FirebaseFirestore.getInstance()
                .collection(PATH_BOARDS)
                .whereEqualTo("public", true)
                .get()
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        task.result?.let { documents ->

                            for (document in documents) {
                                Logger.d("${document.id} => ${document.data["title"]}")

                                val board = document.toObject(Board::class.java)
                                list.add(board)
                            }

                            when (condition) {

                                "popular" -> {

                                    list.sortByDescending { it.savedBy.size }
                                    liveData.value = list.filter { it.savedBy.size != 0 }

                                }

                                "recommend" -> {

                                    val followingTags = UserManager.user.value?.followingTags

                                    if (!followingTags.isNullOrEmpty()) {
                                        val myList: MutableList<Board> = list.filter { board ->
                                            board.tags.any { tag -> tag in followingTags }
                                        }.toMutableList()

                                        liveData.value = myList.shuffled()
                                    }
                                }
                                else -> {
                                }
                            }
                        }
                    }

                }
        }
        return liveData

    }

    override fun searchLiveGlobalBoards(keywords: String): MutableLiveData<MutableList<Board>> {

        val liveData = MutableLiveData<MutableList<Board>>()

        val list = mutableListOf<Board>()

        val keys = keywords.split(" ")

        FirebaseFirestore.getInstance()
            .collection(PATH_BOARDS)
            .whereEqualTo("public", true)
            .whereArrayContainsAny("tags", keys)
            .get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    task.result?.let { documents ->

                        for (document in documents) {

                            Logger.d("${document.id} => ${document.data["title"]}")

                            val board = document.toObject(Board::class.java)
                            list.add(board)

                        }

                        liveData.value = list

                    }

                }

            }

        return liveData
    }

    override fun getLiveBoards(type: BoardTypeFilter): MutableLiveData<List<Board>> {
        val liveData = MutableLiveData<List<Board>>()

        UserManager.userEmail?.let { email ->

            val list = mutableListOf<Board>()

            FirebaseFirestore.getInstance()
                .collection(PATH_BOARDS)
                .whereEqualTo("createdBy", email)
                .get()
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        task.result?.let { documents ->

                            for (document in documents) {

                                Logger.d("${document.id} => ${document.data["title"]}")

                                val board = document.toObject(Board::class.java)
                                list.add(board)
                            }

                        }

                    }

                    FirebaseFirestore.getInstance()
                        .collection(PATH_BOARDS)
                        .whereArrayContains("savedBy", email)
                        .get()
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {

                                task.result?.let { documents ->

                                    for (document in documents) {

                                        Logger.d("${document.id} => ${document.data["title"]}")

                                        val board = document.toObject(Board::class.java)
                                        list.add(board)


                                    }

                                    liveData.value = when (type) {
                                        BoardTypeFilter.SAVED -> {
                                            list.filter { it.savedBy.contains(email) }
                                                .sortedByDescending { it.createdTime }
                                        }
                                        BoardTypeFilter.MINE -> {
                                            list.filter { it.createdBy == email }
                                                .sortedByDescending { it.createdTime }
                                        }
                                        else -> {
                                            list.sortedByDescending { it.createdTime }
                                        }

                                    }

                                }

                            }
                        }

                }
        }



        return liveData

    }

    override fun getBoardLiveNotes(noteIdList: MutableList<String?>): MutableLiveData<List<Note>> {
        val liveData = MutableLiveData<List<Note>>()
        val list = mutableListOf<Note>()

        for (note in noteIdList) {


            FirebaseFirestore.getInstance()
                .collection(PATH_NOTES)
                .whereEqualTo("id", note)
                .get()
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        task.result?.let { documents ->

                            for (document in documents) {
                                Logger.d("${document.id} => ${document.data}")

                                val note = document.toObject(Note::class.java)
                                list.add(note)
                            }

                        }

                    }

                    liveData.value = list
                }

        }

        return liveData

    }


    override suspend fun createNote(note: Note): Result<Boolean> =
        suspendCoroutine { continuation ->
            val notes = FirebaseFirestore.getInstance().collection(PATH_NOTES)
            val document = notes.document()


            notes
                .whereEqualTo("createdBy", note.createdBy)
                .whereEqualTo("url", note.url)
                .get()
                .addOnSuccessListener {
                    if (!it.isEmpty) {
                        continuation.resume(Result.Fail("Same note in database"))
                    } else {
                        note.id = document.id
                        note.createdBy = UserManager.user.value?.email
                        note.createdTime = Calendar.getInstance().timeInMillis

                        document
                            .set(note)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Logger.d("$note")

                                    continuation.resume(
                                        Result.Success(
                                            true
                                        )
                                    )
                                } else {
                                    task.exception?.let { e ->
                                        Logger.w("[${this::class.simpleName}] Error getting documents. ${e.message}")
                                        continuation.resume(
                                            Result.Error(
                                                e
                                            )
                                        )
                                        return@addOnCompleteListener
                                    }
                                    continuation.resume(
                                        Result.Fail(
                                            "Fail"
                                        )
                                    )
                                }
                            }
                    }
                }
        }

    override suspend fun createBoard(board: Board): Result<Boolean> =
        suspendCoroutine { continuation ->
            val boards = FirebaseFirestore.getInstance().collection(PATH_BOARDS)
            val document = boards.document()

            board.id = document.id
            board.createdTime = Calendar.getInstance().timeInMillis

            document
                .set(board)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.d("$board")

                        continuation.resume(
                            Result.Success(
                                true
                            )
                        )
                    } else {
                        task.exception?.let { e ->
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${e.message}")

                            continuation.resume(
                                Result.Error(
                                    e
                                )
                            )
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("Fail"))
                    }
                }
        }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun updateUser(user: User): Result<Boolean> =
        suspendCoroutine { continuation ->

            UserManager.userEmail = user.email

            val users = FirebaseFirestore.getInstance().collection("users")
            val document = user.email.let {
                users.document(it)
            }
            users.whereEqualTo("email", user.email)
                .get()
                .addOnSuccessListener { result ->
                    if (result.isEmpty) {
                        document.set(user).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Logger.i("New User: $user")
                                continuation.resume(
                                    Result.Success(
                                        true
                                    )
                                )
                            } else {
                                task.exception?.let { e ->
                                    Logger.w("[${this::class.simpleName}] Error getting documents. ${e.message}"
                                    )
                                    continuation.resume(
                                        Result.Error(
                                            e
                                        )
                                    )
                                    return@addOnCompleteListener
                                }
                                continuation.resume(
                                    Result.Fail(
                                        NotedApplication.instance.getString(
                                            R.string.you_know_nothing
                                        )
                                    )
                                )
                            }
                        }
                    }
                }
        }

    override suspend fun updateUserTags(tags: List<String?>): Result<Boolean> =
        suspendCoroutine { continuation ->

            val email = UserManager.userEmail ?: ""
            val users = FirebaseFirestore.getInstance().collection("users")


            users.document(email).update("followingTags", tags)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("Tag is updated, $tags")
                        continuation.resume(
                            Result.Success(
                                true
                            )
                        )
                    } else {
                        task.exception?.let { e ->
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${e.message}")

                            continuation.resume(
                                Result.Error(
                                    e
                                )
                            )
                            return@addOnCompleteListener
                        }
                        continuation.resume(
                            Result.Fail(
                                NotedApplication.instance.getString(
                                    R.string.you_know_nothing
                                )
                            )
                        )
                    }
                }
        }

    override suspend fun saveBoard(board: Board): Boolean =
        suspendCoroutine { continuation ->

            FirebaseFirestore.getInstance().collection(PATH_BOARDS)
                .document(board.id).update("savedBy", board.savedBy)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(true)
                    } else {
                        task.exception?.let { e ->
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${e.message}")

                            continuation.resume(false)
                            return@addOnCompleteListener
                        }
                        continuation.resume(false)

                    }
                }
        }

    override suspend fun deleteNote(note: Note): Boolean =

        suspendCoroutine { continuation ->

            Logger.d("to delete ${note.title}, ${note.id}")

            FirebaseFirestore.getInstance().collection(PATH_NOTES)
                .document(note.id)
                .delete()
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        Logger.d("success delete ${note.title}, ${note.id}")
                        continuation.resume(true)

                    } else {
                        task.exception?.let { e ->

                            Logger.w("[${this::class.simpleName}] Error deleting documents. ${e.message}")

                            continuation.resume(false)
                            return@addOnCompleteListener
                        }
                        continuation.resume(false)

                    }
                }
        }


    override fun getLiveUser(): MutableLiveData<User> {
        val liveData = MutableLiveData<User>()
        FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", UserManager.userEmail)
            .addSnapshotListener { snapshot, exception ->
                Logger.i("addSnapshotListener detect")
                exception?.let { e ->
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${e.message}")

                }
                var user = User()
                for (document in snapshot!!) {
                    Logger.d("${document.id} => ${document.data}")
                    val info = document.toObject(User::class.java)
                    user = info
                }
                liveData.value = user
            }
        return liveData
    }


    override suspend fun likeNote(note: Note): Result<Boolean> =
        suspendCoroutine { continuation ->

            val value = !note.isLiked

            val notes = FirebaseFirestore.getInstance().collection(PATH_NOTES)
            notes.document(note.id)
                .update("liked", value)

            continuation.resume(Result.Success(true))
        }

    override suspend fun likeBoard(board: Board): Result<Boolean> =
        suspendCoroutine { continuation ->

            val value = !board.isLiked

            val notes = FirebaseFirestore.getInstance().collection(PATH_BOARDS)
            notes.document(board.id)
                .update("liked", value)

            continuation.resume(Result.Success(true))
        }

}
