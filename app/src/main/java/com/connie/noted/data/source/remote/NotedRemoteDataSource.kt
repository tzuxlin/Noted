package com.connie.noted.data.source.remote

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.connie.noted.data.Note
import com.connie.noted.data.Result
import com.connie.noted.data.source.NotedDataSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object NotedRemoteDataSource : NotedDataSource {


    private const val PATH_NOTES = "notes"
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

}
