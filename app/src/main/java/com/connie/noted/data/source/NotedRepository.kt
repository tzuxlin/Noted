package com.connie.noted.data.source

import androidx.lifecycle.MutableLiveData
import com.connie.noted.data.Note
import com.connie.noted.data.Result
import com.connie.noted.data.User

interface NotedRepository {

    suspend fun createNote(note: Note): Result<Boolean>

    suspend fun likeNote(note: Note): Result<Boolean>

    suspend fun updateUser(user: User): Result<Boolean>

    fun getLiveUser(): MutableLiveData<User>


    fun getLiveNotes(): MutableLiveData<List<Note>>


}