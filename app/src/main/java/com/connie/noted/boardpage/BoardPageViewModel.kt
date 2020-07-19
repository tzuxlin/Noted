package com.connie.noted.boardpage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.data.Board
import com.connie.noted.data.Note
import com.connie.noted.data.source.NotedRepository

class BoardPageViewModel(private val notedRepository: NotedRepository, val boardKey: Board) : ViewModel() {

    val board = MutableLiveData<Board>().apply {
        value = boardKey
    }

    var liveNotes = MutableLiveData<List<Note>>()

    init {getLiveNotes()}

    private fun getLiveNotes() {
        liveNotes = notedRepository.getBoardLiveNotes(boardKey.notes)
    }





}