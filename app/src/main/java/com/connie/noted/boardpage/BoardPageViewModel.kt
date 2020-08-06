package com.connie.noted.boardpage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.data.Board
import com.connie.noted.data.Note
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BoardPageViewModel(private val notedRepository: NotedRepository, val boardKey: Board) :
    ViewModel() {

    val board = MutableLiveData<Board>().apply {
        value = boardKey
    }

    val savedBy = MutableLiveData<MutableList<String?>>().apply {
        value = boardKey.savedBy
    }

    var toSaved = false

    var liveNotes = MutableLiveData<List<Note>>()

    val savedCompleted = MutableLiveData<Boolean>()

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScopeMain = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getLiveNotes()
    }

    private fun getLiveNotes() {
        liveNotes = notedRepository.getBoardLiveNotes(boardKey.notes)
    }

    fun saveBoard() {

        val email = UserManager.userEmail

        board.value?.let { board ->
            val savedBy = board.savedBy
            toSaved = if (savedBy.contains(email)) {
                savedBy.remove(email)
                false
            } else {
                savedBy.add(email)
                true
            }

            this.savedBy.value = savedBy

        }

        toSaveBoard()
    }

    private fun toSaveBoard() {
        coroutineScopeMain.launch {
            savedCompleted.value = notedRepository.saveBoard(boardKey)
        }
    }

    fun doneNavigate(){
        savedCompleted.value = null
    }


}