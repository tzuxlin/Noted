package com.connie.noted.add2board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.NotedApplication
import com.connie.noted.R
import com.connie.noted.data.Board
import com.connie.noted.data.Note
import com.connie.noted.data.Result
import com.connie.noted.data.network.LoadApiStatus
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.login.UserManager
import kotlinx.coroutines.*


class Add2boardViewModel(
    private val notedRepository: NotedRepository
) : ViewModel() {


    // The new board to add
    val board = MutableLiveData<Board>().apply {
        value = Board()
    }


    var liveNotes = MutableLiveData<List<Note>>()
    var title = MutableLiveData<String>()

    val isPublic = MutableLiveData<Boolean>().apply {
        value = false
    }


    // Handle the invalid input
    private val _invalidInput = MutableLiveData<Int>()

    val invalidInput: LiveData<Int>
        get() = _invalidInput


    // Handle navigation to Added Success
    private val _navigateToAddedSuccess = MutableLiveData<Board>()

    val navigateToAddedSuccess: LiveData<Board>
        get() = _navigateToAddedSuccess


    // Handle navigation to Added Fail
    private val _navigateToAddedFail = MutableLiveData<Board>()

    val navigateToAddedFail: LiveData<Board>
        get() = _navigateToAddedFail


    // Handle leave add2board
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


    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun boardDataPreparation() {

        val noteImages = liveNotes.value?.let { notes ->
            notes.filter { note ->
                note.images.isNotEmpty()
            }.map {
                it.images[0]
            }
        }?.toMutableList<String?>()

        val noteIds = liveNotes.value?.map { note ->
            note.id
        }?.toMutableList<String?>()


        val board = Board(
            createdBy = UserManager.user.value?.email ?: "",
            creatorName = UserManager.user.value?.name ?: "",
            title = title.value ?: "",
            images = noteImages ?: mutableListOf(),
            isPublic = isPublic.value ?: false,
            notes = noteIds ?: mutableListOf()
        )

        toUploadBoard(board)


    }


    private fun toUploadBoard(board: Board) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = notedRepository.createBoard(board)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
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

    fun invalidInput(){
        _invalidInput.value = 0
    }

    fun restoreInvalidInput(){
        _invalidInput.value = null
    }


    fun leave() {
        _leave.value = true
    }


    fun onLeaveCompleted() {
        _leave.value = null
    }


    fun nothing() {

    }


}
