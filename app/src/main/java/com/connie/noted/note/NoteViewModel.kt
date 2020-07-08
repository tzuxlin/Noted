package com.connie.noted.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.data.Note
import com.connie.noted.data.Result
import com.connie.noted.data.crawler.NoteCrawlerClass
import com.connie.noted.data.network.LoadApiStatus
import com.connie.noted.data.source.NotedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NoteViewModel(private val notedRepository: NotedRepository, private val note: Note) :
    ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScopeMain = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val coroutineScopeIO = CoroutineScope(viewModelJob + Dispatchers.IO)

    private val _newNote = MutableLiveData<Note>()
    val newNote: LiveData<Note>
        get() = _newNote

    var liveNotes = MutableLiveData<List<Note>>()

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


    init {
        getLiveNotes()
//        goGo()
    }


    private fun goGo() {
        coroutineScopeIO.launch {
            _newNote.postValue(toGetYoutube("https://youtu.be/NHyBeGmpMIY"))
        }
        coroutineScopeIO.launch {
            _newNote.postValue(toGetMediumArticle("https://medium.com/@chichushaheen/web-scraping-in-kotlin-using-jsoup-8b14b9d1761c"))
        }
        coroutineScopeIO.launch {
            _newNote.postValue(toGetGoogleLocation("https://goo.gl/maps/XUGaJF9B7c1LmM1z9"))
        }
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
        liveNotes = notedRepository.getLiveNotes()
    }

    fun create(note: Note) {

        coroutineScopeMain.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = notedRepository.createNote(note)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    leave(true)
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

//    val mockNote: List<Note> =
//        listOf(
//            Note(
//                "Article",
//                "Medium",
//                "A simple way to work with Kotlin Coroutines in Android",
//                mutableListOf(
//                    "https://miro.medium.com/max/1400/1*X_pycRAcXOh176Fals07wg.jpeg",
//                    "https://miro.medium.com/max/1400/1*bVOsGqOWVmcqCfwZRm4yDg.jpeg"
//                ),
//                "Do you feel curious about Kotlin’s coroutines? Have you heard about this before? Do you want to start working with them within your Android project? Well, I’m gonna try to write a simple example in…",
//                mutableListOf("Kotlin", "Coroutines", "Android"),
//                true
//            ),
//            Note(
//                "Location",
//                "Google Maps",
//                "Googleplex",
//                mutableListOf("https://lh5.googleusercontent.com/p/AF1QipPrMWQ5TsMMgKAB6w2dvH624FJ0agQWncjFI68X=w256-h256-k-no-p"),
//                "★★★★☆ · Corporate campus · 1600 Amphitheatre Pkwy",
//                mutableListOf("work"),
//                false
//            ),
//            Note(
//                "Article",
//                "Medium",
//                "RxJava VS. Coroutines In Two Use Cases",
//                mutableListOf(
//                    "https://miro.medium.com/max/2000/0*aN1Xq8hVYBLZW35-"
//                ),
//                "RxJava has been a lifesaver for me for a long time. With all the functionalities it provides, my Android programming mindset has greatly shifted to a more reactive way...",
//                mutableListOf("RxJava", "Coroutines", "Android"),
//                false
//            ),
//            Note(
//                "Video",
//                "YouTube",
//                "千千，妳不是比目魚！是龍虎石斑魚頭火鍋！｜Fred吃上癮｜feat.千千進食中",
//                mutableListOf("https://i.ytimg.com/vi/NHyBeGmpMIY/maxresdefault.jpg"),
//                "自從John離開家門到千千那去之後，我的思念從來沒有一刻停過。 每當我在節目中提起John，都會讓我感到撕心裂肺的痛楚。 我的聲音在笑，淚在飆。電話那頭的你可知道？ 在這最痛苦的時候，好在有 #富鴻網 #智慧農業 和新竹縣政府與經濟部工業局「普及智慧城鄉生活應用計畫」補助。 讓我可以利用這次機會與千千再見一面，而...",
//                mutableListOf("廚佛"),
//                true
//            ),
//            Note(
//                "Article",
//                "Medium",
//                "Web Scraping in Kotlin using Jsoup",
//                mutableListOf(),
//                "Jsoup is a powerul library to perform web scraping in android. Well the intention to write this post is inspired from the tough times that I had while trying to perform this task...",
//                mutableListOf("Kotlin", "Jsoup"),
//                false
//            ),
//            Note(
//                "Article",
//                "Medium",
//                "MVVM on Android with the Architecture Components + Koin",
//                mutableListOf("https://miro.medium.com/max/1400/1*_DMvajfGcKQoIOWpLysa1Q.png"),
//                "MVVM facilitates a separation of development of the graphical user interface be it via a markup language or GUI code from the development of the business logic or back-end logic...",
//                mutableListOf("MVVM", "Android", "Koin"),
//                false
//            )
//        )


}
