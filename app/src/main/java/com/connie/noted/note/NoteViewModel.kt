package com.connie.noted.note

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.connie.noted.NotedApplication
import com.connie.noted.data.Board
import com.connie.noted.data.Note
import com.connie.noted.data.Result
import com.connie.noted.data.User
import com.connie.noted.data.crawler.NoteCrawlerClass
import com.connie.noted.data.network.LoadApiStatus
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NoteViewModel(private val notedRepository: NotedRepository, private val note: Note?) :
    ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScopeMain = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val coroutineScopeIO = CoroutineScope(viewModelJob + Dispatchers.IO)

    // newNote value is assigned from crawler result
    private val _newNote = MutableLiveData<Note>()
    val newNote: LiveData<Note>
        get() = _newNote

    val userIsReady = MutableLiveData<Boolean>()

    var notes = MutableLiveData<MutableList<Note>>()

    var noteToAdd = MutableLiveData<List<Note>>().apply {
        value = mutableListOf()
    }

    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    val viewType = MutableLiveData<Int>().apply {
        value = 1
    }

    val isEditMode = MutableLiveData<Boolean>().apply {
        value = false
    }


    init {
        getLiveNotes()
    }



    fun goGo(url: String) {
//        if (!NotedApplication.isGoGo) {

        when {

//                url.contains("http:", true) -> {
//                    Log.e("ConnieCrawler", "Gogo, split")
//                    val splitUrl = url.split("//")
//                    goGo("https://${splitUrl[1]}")
//                }

            url.contains("//youtu", true) -> {
                Log.e("ConnieCrawler", "Gogo, youtube: $url")
                coroutineScopeIO.launch {
                    _newNote.postValue(toGetYoutube(url))
                }
            }
            url.contains("//g.", true) ||
                    url.contains("//goo", true) -> {

                val data = url.lines()

//                    val url = data[3]
                Log.e(
                    "Connie",
                    "0 = ${data[0]}, 1 = ${data[1]}, 2 = ${data[2]}, 3 = ${data[3]}, 4 = ${data[4]}"
                )

                for (i in data.indices){
                    if (data[i].contains("http")) {
                        coroutineScopeIO.launch {
                            _newNote.postValue(toGetGoogleLocation(data[i]))
                        }
                    }
                }

            }
            else -> {
                coroutineScopeIO.launch {
                    Log.e("ConnieCrawler", "Gogo, article: $url")
                    _newNote.postValue(toGetMediumArticle(url))
                }
            }
        }
//            NotedApplication.isGoGo = true
//        }
    }


    private suspend fun toGetGoogleLocation(url: String): Note {
        val noteCrawler = NoteCrawlerClass()
        return noteCrawler.getGoogleLocation(url)
    }

    private suspend fun toGetYoutube(url: String): Note {
        val noteCrawler = NoteCrawlerClass()
        return noteCrawler.getYoutube(url)
    }

    private suspend fun toGetMediumArticle(url: String): Note {
        val noteCrawler = NoteCrawlerClass()
        return noteCrawler.getMediumArticle(url)
    }


    fun getLiveNotes() {
        notes = notedRepository.getLiveNotes()
    }

    fun create(note: Note) {

        coroutineScopeMain.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = notedRepository.createNote(note)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    leave(true)
                    getLiveNotes()
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = "You know nothing"
//                        NotedApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }

    fun checkLogin() {
        if (UserManager.justLogin) {
            UserManager.user.value?.let {
                updateUser(it)
            }
        } else {
            UserManager.userEmail?.let {
                getUser()
            }
        }
    }

    private fun updateUser(user: User) {
        coroutineScopeMain.launch {
            notedRepository.updateUser(user)
        }

    }

    private fun getUser() {
        UserManager.user = notedRepository.getLiveUser()
        userIsReady.value = true
    }


    fun likeButtonClicked(note: Note) {
        Log.e("Connie", note.toString())
        updateIsLiked(note)
    }

    private fun updateIsLiked(note: Note) {
        coroutineScopeMain.launch {
            notedRepository.likeNote(note)
        }
    }
}
