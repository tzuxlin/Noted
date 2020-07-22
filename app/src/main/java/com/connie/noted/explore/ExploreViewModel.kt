package com.connie.noted.explore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.data.Board
import com.connie.noted.data.source.NotedRepository

class ExploreViewModel(private val notedRepository: NotedRepository) : ViewModel() {


    var liveBoards = MutableLiveData<List<Board>>()

    init {
        getLiveBoard()
    }

    private fun getLiveBoard() {
        liveBoards = notedRepository.getLiveGlobalBoards("popular")
    }


}