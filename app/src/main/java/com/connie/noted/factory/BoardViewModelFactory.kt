package com.connie.noted.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.connie.noted.boardpage.BoardPageViewModel
import com.connie.noted.data.Board
import com.connie.noted.data.source.NotedRepository

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class BoardViewModelFactory constructor(
    private val notedRepository: NotedRepository,
    private val board: Board
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(BoardPageViewModel::class.java) ->
                    BoardPageViewModel(notedRepository, board)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
