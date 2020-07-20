package com.connie.noted.notepage.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.data.Note
import com.connie.noted.data.source.NotedRepository

class ArticleViewModel(private val notedRepository: NotedRepository, val noteKey: Note) :
    ViewModel() {

    val note = MutableLiveData<Note>()

    private val _navigateToUrl = MutableLiveData<Boolean>()
    val navigateToUrl: LiveData<Boolean>
        get() = _navigateToUrl

    fun startUrlIntent() {

        _navigateToUrl.value = true

    }

    fun onUrlIntentCompleted(){
        _navigateToUrl.value = null
    }


}
