package com.connie.noted.data.source

import androidx.lifecycle.MutableLiveData
import com.connie.noted.data.Note
import com.connie.noted.data.Result

interface NotedRepository {

    suspend fun createNote(note: Note): Result<Boolean>

    fun getLiveNotes(): MutableLiveData<List<Note>>


}