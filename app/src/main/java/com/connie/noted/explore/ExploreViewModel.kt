package com.connie.noted.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.board.BoardTypeFilter
import com.connie.noted.data.Board
import com.connie.noted.data.network.LoadApiStatus
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.util.ServiceLocator.notedRepository

class ExploreViewModel(private val notedRepository: NotedRepository) : ViewModel() {


    var popularBoards = MutableLiveData<List<Board>>()
    var recommendBoards = MutableLiveData<List<Board>>()

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _enableSearch = MutableLiveData<Boolean>().apply {
        value = false
    }
    val enableSearch: LiveData<Boolean>
        get() = _enableSearch


    init {
        getLiveBoard()
    }

    private fun getLiveBoard() {
        _status.value = LoadApiStatus.LOADING
        popularBoards = notedRepository.getLiveGlobalBoards("popular")
        recommendBoards = notedRepository.getLiveGlobalBoards("recommend")
    }


    fun loadApiStatusDone() {
        _status.value = LoadApiStatus.DONE
    }

    fun search() {
        Log.e("Connie", "Search")
    }

    fun toEnableSearch() {
        _enableSearch.value = true
    }


    fun toDisableSearch() {
        _enableSearch.value = false
    }


}