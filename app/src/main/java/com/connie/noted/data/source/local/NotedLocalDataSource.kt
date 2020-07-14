package com.connie.noted.data.source.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.connie.noted.data.Note
import com.connie.noted.data.Result
import com.connie.noted.data.source.NotedDataSource

class NotedLocalDataSource(val context: Context): NotedDataSource {


    override suspend fun createNote(note: Note): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun likeNote(note: Note): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getLiveNotes(): MutableLiveData<List<Note>> {
        TODO("Not yet implemented")
    }


}


