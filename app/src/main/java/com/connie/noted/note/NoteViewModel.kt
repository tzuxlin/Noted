package com.connie.noted.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.NotedApplication
import com.connie.noted.R
import com.connie.noted.data.Note
import com.connie.noted.data.Result
import com.connie.noted.data.User
import com.connie.noted.data.crawler.NoteCrawlerClass
import com.connie.noted.data.network.LoadApiStatus
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.login.UserManager
import com.connie.noted.util.ImageLeftOutlineProvider
import com.connie.noted.util.ImageTopOutlineProvider
import com.connie.noted.util.Logger
import com.connie.noted.util.ParseType
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
    private val _parsedNote = MutableLiveData<Note>()
    val parsedNote: LiveData<Note>
        get() = _parsedNote

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

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    val viewType = MutableLiveData<Int>().apply {
        value = 1
    }

    val isEditMode = MutableLiveData<Boolean>().apply {
        value = false
    }

    val toObserveNote = MutableLiveData<Boolean>()




    val topOutlineProvider = ImageTopOutlineProvider()
    val leftOutlineProvider = ImageLeftOutlineProvider()


    init {
        getLiveNotes()
    }


    fun determineParseType(rawUrl: String) {

        val url = getExactUrl(rawUrl)

        when (parseType(url)) {
            ParseType.VIDEO -> parseYoutube(url)
            ParseType.LOCATION -> parseLocation(url)
            else -> parseArticle(url)

        }
    }


    fun getLiveNotes() {
        notes = notedRepository.getLiveNotes()
        toObserveNote.value = true
    }


    fun updateNoteToFirebase(note: Note) {

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
                    _error.value = NotedApplication.instance.getString(R.string.you_know_nothing)
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
        toUpdateIsLiked(note)
    }

    private fun toUpdateIsLiked(note: Note) {
        coroutineScopeMain.launch {
            notedRepository.likeNote(note)
        }
    }

    private fun parseType(url: String): ParseType {

        return when {

            url.contains("//youtu", true) -> {
                ParseType.VIDEO
            }
            url.contains("//g.", true) ||
                    url.contains("//goo", true) ||
                    url.contains("//maps.", true) -> {

                ParseType.LOCATION

            }
            else -> {
                ParseType.ARTICLE
            }
        }
    }

    private fun parseArticle(url: String) {
        coroutineScopeIO.launch {
            _parsedNote.postValue(toGetMediumArticle(url))
        }
    }

    private fun parseLocation(url: String) {

        Logger.d("ParseLocation, origin content: $url")

        val data = url.lines()

        for (i in data.indices) {
            if (data[i].contains("http")) {
                coroutineScopeIO.launch {
                    _parsedNote.postValue(toGetGoogleLocation(data[i]))
                }
            }
        }
    }

    private fun parseYoutube(url: String) {
        coroutineScopeIO.launch {
            _parsedNote.postValue(toGetYoutube(url))
        }
    }

    private fun getExactUrl(url: String): String {

        var result = url.lines().filter { it.contains("http") }[0]
        if (result.contains("//maps.", true)) {
            result += "?_imcp=1"
        }

        return result
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

    fun toAddNote(note: Note) {

        notes.value?.let { liveNotes ->

            liveNotes.map {
                if (it.id == note.id) {
                    it.isSelected = !it.isSelected
                }
            }

            noteToAdd.value = liveNotes.filter {
                it.isSelected
            }
        }
    }

    fun deleteNote(note: Note) {

        Logger.e("ViewModel, delete ${note.title}")
        coroutineScopeMain.launch {
            notedRepository.deleteNote(note)
        }
    }

    fun onNoteObserved() {
        toObserveNote.value = false
    }

}
