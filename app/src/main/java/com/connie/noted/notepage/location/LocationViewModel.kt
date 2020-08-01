package com.connie.noted.notepage.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.data.Note
import com.connie.noted.data.source.NotedRepository

class LocationViewModel(private val notedRepository: NotedRepository, val noteKey: Note) : ViewModel() {

    val note = MutableLiveData<Note>()


}