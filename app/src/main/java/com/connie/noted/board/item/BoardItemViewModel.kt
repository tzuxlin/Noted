package com.connie.noted.board.item

import android.util.Log
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
import kotlinx.coroutines.launch

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [CatalogItemFragment].
 */
class BoardItemViewModel(
    private val notedRepository: NotedRepository,
    private val boardType: BoardTypeFilter // Handle the type for each catalog item
) : ViewModel() {


    val hasNewTag = MutableLiveData<Boolean>()

    val filterTags: MutableList<String?> = mutableListOf()

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

    private fun toGetBoard() {
        liveBoards = notedRepository.getLiveBoards(boardType)
    }

    fun tagClicked(tag: String) {

        hasNewTag.value = true


        if (filterTags.contains(tag)) {
            filterTags.remove(tag)
        } else {
            filterTags.add(tag)
        }


        Log.w("Connie", "viewModel, tagClicked, ${filterTags}")


    }

    fun likeButtonClicked(board: Board){
        Log.e("Connie", board.toString())
        updateIsLiked(board)
    }

    private fun updateIsLiked(board: Board){
        coroutineScope.launch {
            notedRepository.likeBoard(board)
        }
    }
}