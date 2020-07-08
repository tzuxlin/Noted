package com.connie.noted.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.connie.noted.data.Note
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.note.NoteViewModel

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class NoteViewModelFactory constructor(
    private val notedRepository: NotedRepository,
    private val note: Note
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(NoteViewModel::class.java) ->
                    NoteViewModel(notedRepository, note)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
