package com.connie.noted.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.connie.noted.board.BoardTypeFilter
import com.connie.noted.board.item.BoardItemViewModel
import com.connie.noted.data.Board
import com.connie.noted.data.source.NotedRepository

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class BoardItemViewModelFactory constructor(
    private val notedRepository: NotedRepository,
    private val boardType: BoardTypeFilter
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(BoardItemViewModel::class.java) ->
                    BoardItemViewModel(notedRepository, boardType)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
