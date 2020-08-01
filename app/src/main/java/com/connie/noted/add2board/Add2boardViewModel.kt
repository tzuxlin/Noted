package com.connie.noted.add2board

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.data.Board
import com.connie.noted.data.Note
import com.connie.noted.data.Result
import com.connie.noted.data.network.LoadApiStatus
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.login.UserManager
import kotlinx.coroutines.*

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [Add2cartDialog].
 */
class Add2boardViewModel(
    private val notedRepository: NotedRepository
) : ViewModel() {

    // Detail has product data from arguments
    val board = MutableLiveData<Board>().apply {
        value = Board()
    }

    // Handle the error for checkout
    private val _invalidInput = MutableLiveData<Int>()

    val invalidInput: LiveData<Int>
        get() = _invalidInput


    var liveNotes = MutableLiveData<List<Note>>()


    var title = MutableLiveData<String>()
    var isPublic = false

    private val _toUploadBoard = MutableLiveData<Boolean>()
    val toUploadBoard: LiveData<Boolean>
        get() = _toUploadBoard

    // Handle navigation to Added Success
    private val _navigateToAddedSuccess = MutableLiveData<Board>()

    val navigateToAddedSuccess: LiveData<Board>
        get() = _navigateToAddedSuccess

    // Handle navigation to Added Fail
    private val _navigateToAddedFail = MutableLiveData<Board>()

    val navigateToAddedFail: LiveData<Board>
        get() = _navigateToAddedFail

    // Handle leave add2cart
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


    fun checkData() {
        _toUploadBoard.value = true
    }

    fun uploadBoard() {

        val noteWithImages = liveNotes.value?.let { notes ->
            notes.filter { note ->
                note.images.isNotEmpty()
            }.map {
                it.images[0]
            }
        }?.toMutableList<String?>()

        val noteWithId = liveNotes.value?.map { note ->
            note.id
        }?.toMutableList<String?>()


        val board = Board(
            createdBy = UserManager.user.value?.email ?: "",
            creatorName = UserManager.user.value?.name ?: "",
            title = title.value ?: "",
            images = noteWithImages ?: mutableListOf(),
            isPublic = isPublic,
            notes = noteWithId ?: mutableListOf()
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
                    leave()
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

    fun onAddedSuccessNavigated() {
        _navigateToAddedSuccess.value = null
    }

    fun onAddedFailNavigated() {
        _navigateToAddedFail.value = null
    }


    fun convertLongToString(value: Long): String {
        return value.toString()
    }

    fun leave() {
        _leave.value = true
    }

    fun onLeaveCompleted() {
        _leave.value = null
    }

    fun nothing() {}


}
