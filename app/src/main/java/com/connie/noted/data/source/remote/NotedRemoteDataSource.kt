package com.connie.noted.data.source.remote

import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.connie.noted.NotedApplication
import com.connie.noted.R
import com.connie.noted.board.BoardTypeFilter
import com.connie.noted.data.Board
import com.connie.noted.data.Note
import com.connie.noted.data.Result
import com.connie.noted.data.User
import com.connie.noted.data.source.NotedDataSource
import com.connie.noted.login.UserManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object NotedRemoteDataSource : NotedDataSource {


    private const val PATH_NOTES = "notes"
    private const val PATH_BOARDS = "boards"
    private const val KEY_CREATED_TIME = "createdTime"

    override fun getLiveNotes(): MutableLiveData<List<Note>> {
        val liveData = MutableLiveData<List<Note>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_NOTES)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Note>()
                    for (document in task.result!!) {
                        Log.d("Connie", document.id + " => " + document.data)

                        val note = document.toObject(Note::class.java)
                        list.add(note)
                    }
                    liveData.value = list
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
                                Log.d("Connie", document.id + " => " + document.data)

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
                                        Log.d("Connie", document.id + " => " + document.data)

                                        val board = document.toObject(Board::class.java)
                                        list.add(board)


                                    }

                                    liveData.value = when (type) {
                                        BoardTypeFilter.SAVED -> {
                                            list.filter { it.savedBy.contains(email) }
                                        }
                                        BoardTypeFilter.MINE -> {
                                            list.filter { it.createdBy == email }
                                        }
                                        else -> {
                                            list
                                        }

                                    }

                                }

                            }
                        }

                }
        }



        return liveData

    }


//    override fun getLiveNotesFromBoards(): MutableLiveData<List<Board>> {
//        val liveData = MutableLiveData<List<Board>>()
//
//        UserManager.userEmail?.let { email ->
//
//            FirebaseFirestore.getInstance()
//                .collection(PATH_BOARDS)
//                .whereEqualTo("createdBy", email)
//                .get()
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//
//                        task.result?.let { documents ->
//                            for (document in documents) {
//
//                                val noteIdList = document["notes"] as List<String>
//
//                                for (noteId in noteIdList) {
//
//
//
//                                }
//
//
//                            }
//
//
//                        }
//
//                    }
//                }
//        }
//
//        return liveData
//    }


    override suspend fun createNote(note: Note): Result<Boolean> =
        suspendCoroutine { continuation ->
            val notes = FirebaseFirestore.getInstance().collection(PATH_NOTES)
            val document = notes.document()

            note.id = document.id
            note.createdTime = Calendar.getInstance().timeInMillis

            document
                .set(note)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Connie", "$note")

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Connie",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )

                            continuation.resume(Result.Error(it))
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
                                Log.i("Connie", "New User: $user")
                                continuation.resume(Result.Success(true))
                            } else {
                                task.exception?.let {
                                    Log.w(
                                        "Connie",
                                        "[${this::class.simpleName}] Error getting documents. ${it.message}"
                                    )
                                    continuation.resume(Result.Error(it))
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
                    } else {
                        Log.d("Connie", "User already exist")
                    }
                }
        }


    override fun getLiveUser(): MutableLiveData<User> {
        val liveData = MutableLiveData<User>()
        FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", UserManager.userEmail)
            .addSnapshotListener { snapshot, exception ->
                Log.i("Connie", "addSnapshotListener detect")
                exception?.let {
                    Log.w(
                        "Connie",
                        "[${this::class.simpleName}] Error getting documents. ${it.message}"
                    )
                }
                var user = User()
                for (document in snapshot!!) {
                    Log.d("Connie", document.id + " => " + document.data)
                    val info = document.toObject(User::class.java)
                    user = info
                }
                liveData.value = user
            }
        return liveData
    }


    override suspend fun likeNote(note: Note): Result<Boolean> =
        suspendCoroutine { continuation ->

            val notes = FirebaseFirestore.getInstance().collection(PATH_NOTES)
            notes.document(note.id)
                .update("liked", !note.isLiked)

            continuation.resume(Result.Success(true))
        }

}
