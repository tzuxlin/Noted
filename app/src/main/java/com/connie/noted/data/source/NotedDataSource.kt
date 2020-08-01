package com.connie.noted.data.source

import androidx.lifecycle.MutableLiveData
import com.connie.noted.board.BoardTypeFilter
import com.connie.noted.data.Board
import com.connie.noted.data.Note
import com.connie.noted.data.Result
import com.connie.noted.data.User

interface NotedDataSource {

    suspend fun createNote(note: Note): Result<Boolean>

    suspend fun createBoard(board: Board): Result<Boolean>

    suspend fun likeNote(note: Note): Result<Boolean>

    suspend fun likeBoard(board: Board): Result<Boolean>

    suspend fun updateUser(user: User): Result<Boolean>

    suspend fun updateUserTags(tags: List<String?>): Result<Boolean>

    suspend fun saveBoard(board: Board): Boolean

    fun getLiveUser(): MutableLiveData<User>

    fun getLiveNotes(): MutableLiveData<MutableList<Note>>

    fun getLiveBoards(type: BoardTypeFilter): MutableLiveData<List<Board>>

    fun getLiveGlobalBoards(condition: String): MutableLiveData<List<Board>>

    fun searchLiveGlobalBoards(keywords: String): MutableLiveData<MutableList<Board>>

    fun getBoardLiveNotes(noteIdList: MutableList<String?>): MutableLiveData<List<Note>>


}