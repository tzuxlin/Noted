package com.connie.noted.notepage.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.data.Note
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.util.Logger

class VideoViewModel(private val notedRepository: NotedRepository, val noteKey: Note) :
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

    fun getYoutubeId(): String {

        val url = note.value?.url?.split("/")

        val result = url?.let { it ->
            it[it.size - 1]
        }?.replace(" ","")

        Logger.e("url = $url, result = $result")

        return result ?: ""
    }


}