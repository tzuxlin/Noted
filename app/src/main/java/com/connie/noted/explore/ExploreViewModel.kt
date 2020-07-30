package com.connie.noted.explore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.board.BoardTypeFilter
import com.connie.noted.data.Board
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.util.ServiceLocator.notedRepository

class ExploreViewModel(private val notedRepository: NotedRepository) : ViewModel() {


    var popularBoards = MutableLiveData<List<Board>>()
    var recommendBoards = MutableLiveData<List<Board>>()


    init {
        getLiveBoard()
    }

    private fun getLiveBoard() {
        popularBoards = notedRepository.getLiveGlobalBoards("popular")
        recommendBoards = notedRepository.getLiveGlobalBoards("recommend")
    }


}