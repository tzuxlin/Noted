package com.connie.noted.notepage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.data.Note
import com.connie.noted.data.source.NotedRepository

class NotePageViewModel(private val notedRepository: NotedRepository, val noteKey: Note) : ViewModel() {

    val note = MutableLiveData<Note>()


}