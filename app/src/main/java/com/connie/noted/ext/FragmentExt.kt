package com.connie.noted.ext

import androidx.fragment.app.Fragment
import com.connie.noted.NotedApplication
import com.connie.noted.data.Note
import com.connie.noted.factory.NoteViewModelFactory
import com.connie.noted.factory.ViewModelFactory

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Extension functions for Fragment.
 */
fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as NotedApplication).notedRepository
    return ViewModelFactory(repository)
}


fun Fragment.getVmFactory(note: Note): NoteViewModelFactory {
    val repository = (requireContext().applicationContext as NotedApplication).notedRepository
    return NoteViewModelFactory(repository, note)
}