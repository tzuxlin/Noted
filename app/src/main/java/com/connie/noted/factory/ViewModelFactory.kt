package com.connie.noted.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.connie.noted.MainViewModel
import com.connie.noted.add2board.Add2boardViewModel
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.explore.ExploreViewModel
import com.connie.noted.tag.TagViewModel

/**
 * Factory for all ViewModels.
 */

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val notedRepository: NotedRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(notedRepository)

                isAssignableFrom(ExploreViewModel::class.java) ->
                    ExploreViewModel(notedRepository)

                isAssignableFrom(Add2boardViewModel::class.java) ->
                    Add2boardViewModel(notedRepository)

                isAssignableFrom(TagViewModel::class.java) ->
                    TagViewModel(notedRepository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")

            } as T
        }
}
