package com.connie.noted.board.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.board.BoardTypeFilter
import com.connie.noted.data.Board
import com.connie.noted.data.Note
import com.connie.noted.data.source.NotedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [CatalogItemFragment].
 */
class BoardItemViewModel(
    private val notedRepository: NotedRepository,
    boardType: BoardTypeFilter // Handle the type for each catalog item
) : ViewModel() {




    // Handle navigation to detail
    private val _navigateToDetail = MutableLiveData<Board>()

    val navigateToDetail: LiveData<Board>
        get() = _navigateToDetail

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val viewType = MutableLiveData<Int>().apply {
        value = 0
    }

    var liveBoards = MutableLiveData<List<Board>>()


    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    init {
        toGetBoard()
    }

    private fun toGetBoard(){
        liveBoards = notedRepository.getLiveBoards()
    }


    fun navigateToDetail(board: Board) {
        _navigateToDetail.value = board
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}