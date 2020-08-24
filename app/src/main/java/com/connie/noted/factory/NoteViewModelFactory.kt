package com.connie.noted.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.connie.noted.data.Note
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.note.NoteViewModel
import com.connie.noted.notepage.article.ArticleViewModel
import com.connie.noted.notepage.location.LocationViewModel
import com.connie.noted.notepage.video.VideoViewModel

/**
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

                isAssignableFrom(ArticleViewModel::class.java) ->
                    ArticleViewModel(notedRepository, note)

                isAssignableFrom(LocationViewModel::class.java) ->
                    LocationViewModel(notedRepository, note)

                isAssignableFrom(VideoViewModel::class.java) ->
                    VideoViewModel(notedRepository, note)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
