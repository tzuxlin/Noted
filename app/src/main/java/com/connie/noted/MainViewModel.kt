package com.connie.noted

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.data.User
import com.connie.noted.data.network.LoadApiStatus
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.login.UserManager
import com.connie.noted.util.CurrentFilterType
import com.connie.noted.util.CurrentFragmentType
import com.connie.noted.util.DrawerToggleType
import com.connie.noted.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [MainActivity].
 */
class MainViewModel(val notedRepository: NotedRepository) : ViewModel() {


    private val _urlString = MutableLiveData<String>()

    val rawUrlString: LiveData<String>
        get() = _urlString


    val viewType = MutableLiveData<Int>().apply {
        value = 0
    }

    val currentFilterType = MutableLiveData<CurrentFilterType>().apply {
        value = CurrentFilterType.ALL
    }

    // Record current fragment to support data binding
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()


    private val _userIsSynced = MutableLiveData<Boolean>()

    val userIsSynced: LiveData<Boolean>
        get() = _userIsSynced

    var user = MutableLiveData<User>()

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        syncUserData()
    }


    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status


    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun syncUserData() {
        coroutineScope.launch {
            Logger.d("MainViewModel, syncUserData() -> getLiveUser()")
            user = notedRepository.getLiveUser()
            UserManager.user = notedRepository.getLiveUser()
            _userIsSynced.value = true
        }
    }

    fun onSyncUserDataFinished() {
        _userIsSynced.value = null
    }

    fun setUrl(url: String){
        _urlString.value = url
    }

    fun clearUrl(){
        _urlString.value = null
    }


}