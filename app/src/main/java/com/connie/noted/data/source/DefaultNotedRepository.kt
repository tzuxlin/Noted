package com.connie.noted.data.source

import androidx.lifecycle.MutableLiveData
import com.connie.noted.data.Note
import com.connie.noted.data.Result

class DefaultNotedRepository(
    private val remoteDataSource: NotedDataSource,
    private val localDataSource: NotedDataSource
) : NotedRepository {

    override suspend fun createNote(note: Note): Result<Boolean> {
        return remoteDataSource.createNote(note)
    }

    override fun getLiveNotes(): MutableLiveData<List<Note>> {
        return remoteDataSource.getLiveNotes()
    }


}