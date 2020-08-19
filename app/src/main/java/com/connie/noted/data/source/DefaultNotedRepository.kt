package com.connie.noted.data.source

import androidx.lifecycle.MutableLiveData
import com.connie.noted.board.BoardTypeFilter
import com.connie.noted.data.Board
import com.connie.noted.data.Note
import com.connie.noted.data.Result
import com.connie.noted.data.User

class DefaultNotedRepository(
    private val remoteDataSource: NotedDataSource
) : NotedRepository {

    override suspend fun createNote(note: Note): Result<Boolean> {
        return remoteDataSource.createNote(note)
    }

    override suspend fun createBoard(board: Board): Result<Boolean> {
        return remoteDataSource.createBoard(board)
    }

    override suspend fun likeNote(note: Note): Result<Boolean> {
        return remoteDataSource.likeNote(note)
    }

    override suspend fun likeBoard(board: Board): Result<Boolean> {
        return remoteDataSource.likeBoard(board)
    }

    override suspend fun updateUser(user: User): Result<Boolean> {
        return remoteDataSource.updateUser(user)
    }

    override suspend fun updateUserTags(tags: List<String?>): Result<Boolean> {
        return remoteDataSource.updateUserTags(tags)
    }

    override suspend fun saveBoard(board: Board): Boolean {
        return remoteDataSource.saveBoard(board)
    }

    override suspend fun deleteNote(note: Note): Boolean {
        return remoteDataSource.deleteNote(note)
    }

    override fun getLiveUser(): MutableLiveData<User> {
        return remoteDataSource.getLiveUser()
    }

    override fun getLiveNotes(): MutableLiveData<MutableList<Note>> {
        return remoteDataSource.getLiveNotes()
    }

    override fun getLiveBoards(type: BoardTypeFilter): MutableLiveData<List<Board>> {
        return remoteDataSource.getLiveBoards(type)
    }

    override fun getLiveGlobalBoards(condition: String): MutableLiveData<List<Board>> {
        return remoteDataSource.getLiveGlobalBoards(condition)
    }

    override fun searchLiveGlobalBoards(keywords: String): MutableLiveData<MutableList<Board>> {
        return remoteDataSource.searchLiveGlobalBoards(keywords)
    }

    override fun getBoardLiveNotes(noteIdList: MutableList<String?>): MutableLiveData<List<Note>> {
        return remoteDataSource.getBoardLiveNotes(noteIdList)
    }


}