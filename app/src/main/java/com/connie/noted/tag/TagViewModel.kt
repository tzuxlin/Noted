package com.connie.noted.tag

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
import kotlinx.coroutines.*

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [Add2cartDialog].
 */
class TagViewModel(
    private val notedRepository: NotedRepository
) : ViewModel() {

    // Detail has product data from arguments
    val board = MutableLiveData<Board>().apply { value = Board() }

    // Handle the error for checkout
    private val _invalidInput = MutableLiveData<Int>()

    val invalidInput: LiveData<Int>
        get() = _invalidInput


    var liveNotes = MutableLiveData<List<Note>>()

    private val _editMode = MutableLiveData<Boolean>().apply { value = false }
    val editMode: LiveData<Boolean>
     get() = _editMode


    var followingTags = mutableListOf<String>()
    val tagsToAdd = mutableListOf<String?>()

    var inTagEditMode = false


    var newTag = MutableLiveData<String>()
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

    fun updateTags() {
        toUploadTags()
    }

    private fun toUploadTags() {
        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = notedRepository.updateUserTags(followingTags)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
//                    clearTags2Add()
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
//                    clearTags2Add()
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
//                    clearTags2Add()
                }
                else -> {
                    _error.value = NotedApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
//                    clearTags2Add()
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

    fun restoreStatus() {
        _status.value = null
    }

    fun restoreNewTag() {
        newTag.value = null

    }

    fun nothing() {}

    fun setEditMode(boolean: Boolean){
        _editMode.value = boolean
    }

//    fun clearTags2Add(){
//        followingTags.removeAll(tagsToAdd)
//    }

}
