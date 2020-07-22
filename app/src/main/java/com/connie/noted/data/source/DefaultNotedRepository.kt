package com.connie.noted.data.source

import androidx.lifecycle.MutableLiveData
import com.connie.noted.board.BoardTypeFilter
import com.connie.noted.data.Board
import com.connie.noted.data.Note
import com.connie.noted.data.Result
import com.connie.noted.data.User

class DefaultNotedRepository(
    private val remoteDataSource: NotedDataSource,
    private val localDataSource: NotedDataSource
) : NotedRepository {

    override suspend fun createNote(note: Note): Result<Boolean> {
        return remoteDataSource.createNote(note)
    }

    override suspend fun likeNote(note: Note): Result<Boolean> {
        return remoteDataSource.likeNote(note)
    }

    override suspend fun updateUser(user: User): Result<Boolean> {
        return remoteDataSource.updateUser(user)
    }

    override fun getLiveUser(): MutableLiveData<User> {
        return remoteDataSource.getLiveUser()
    }

    override fun getLiveNotes(): MutableLiveData<List<Note>> {
        return remoteDataSource.getLiveNotes()
    }

    override fun getLiveBoards(type: BoardTypeFilter): MutableLiveData<List<Board>> {
        return remoteDataSource.getLiveBoards(type)
    }

    override fun getLiveGlobalBoards(condition: String): MutableLiveData<List<Board>> {
        return remoteDataSource.getLiveGlobalBoards(condition)
    }

    override fun getBoardLiveNotes(noteIdList: MutableList<String?>): MutableLiveData<List<Note>> {
        return remoteDataSource.getBoardLiveNotes(noteIdList)
    }


}