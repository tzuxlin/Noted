package com.connie.noted

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.connie.noted.data.User
import com.connie.noted.data.network.LoadApiStatus
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.login.UserManager
import com.connie.noted.util.DrawerToggleType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [MainActivity].
 */
class MainViewModel(private val notedRepository: NotedRepository) : ViewModel() {


    // According to current fragment to change different drawer toggle
    val currentDrawerToggleType = MutableLiveData<DrawerToggleType>().apply {
        value = DrawerToggleType.NORMAL
    }



//    // Record current fragment to support data binding
//    val currentFragmentType = MutableLiveData<CurrentFragmentType>()
//
//    // According to current fragment to change different drawer toggle
//    val currentDrawerToggleType: LiveData<DrawerToggleType> = Transformations.map(currentFragmentType) {
//        when (it) {
//            CurrentFragmentType.PAYMENT -> DrawerToggleType.BACK
//            else -> DrawerToggleType.NORMAL
//        }
//    }
//
//    // Handle navigation to login success
//    private val _navigateToLoginSuccess = MutableLiveData<User>()
//
//    val navigateToLoginSuccess: LiveData<User>
//        get() = _navigateToLoginSuccess
//
//    // Handle navigation to profile by bottom nav directly which includes icon change
//    private val _navigateToProfileByBottomNav = MutableLiveData<User>()
//
//    val navigateToProfileByBottomNav: LiveData<User>
//        get() = _navigateToProfileByBottomNav
//
//    // Handle navigation to home by bottom nav directly which includes icon change
//    private val _navigateToHomeByBottomNav = MutableLiveData<Boolean>()
//
//    val navigateToHomeByBottomNav: LiveData<Boolean>
//        get() = _navigateToHomeByBottomNav
//
//    // it for set up the circle image of an avatar
//    val outlineProvider = ProfileAvatarOutlineProvider()
//
//    // check user login status
//    val isLoggedIn
//        get() = UserManager.isLoggedIn

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

//    // error: The internal MutableLiveData that stores the error of the most recent request
//    private val _error = MutableLiveData<String>()
//
//    val error: LiveData<String>
//        get() = _error


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



//    fun checkUser() {
//        if (user.value == null) {
//            UserManager.userToken?.let {
//                getUserProfile(it)
//            }
//        }
//    }




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
        coroutineScope.launch {
            notedRepository.updateUser(user)
        }

    }

    private fun getUser() {
        UserManager.user = notedRepository.getLiveUser()
    }
}