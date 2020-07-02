package com.connie.noted

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.connie.noted.data.source.NotedRepository
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

//
//    // user: MainViewModel has User info to provide Drawer UI
//    private val _user = MutableLiveData<User>()
//
//    val user: LiveData<User>
//        get() = _user
//
//    // products: Get products from database to provide count number to bottom badge of cart
//    val products: LiveData<List<Product>> = notedRepository.getProductsInCart()
//
//    // countInCart: Count number for bottom badge
//    val countInCart: LiveData<Int> = Transformations.map(products) { it.size }
//
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
//
//    // status: The internal MutableLiveData that stores the status of the most recent request
//    private val _status = MutableLiveData<LoadApiStatus>()
//
//    val status: LiveData<LoadApiStatus>
//        get() = _status
//
//    // error: The internal MutableLiveData that stores the error of the most recent request
//    private val _error = MutableLiveData<String>()
//
//    val error: LiveData<String>
//        get() = _error
//
//    // Create a Coroutine scope using a job to be able to cancel when needed
//    private var viewModelJob = Job()
//
//    // the Coroutine runs using the Main (UI) dispatcher
//    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
//
//    /**
//     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
//     * Retrofit service to stop.
//     */
//    override fun onCleared() {
//        super.onCleared()
//        viewModelJob.cancel()
//    }
//
//    init {
//        Logger.i("------------------------------------")
//        Logger.i("[${this::class.simpleName}]${this}")
//        Logger.i("------------------------------------")
//    }
//
//    fun setupUser(user: User) {
//
//        _user.value = user
//        Logger.i("=============")
//        Logger.i("| setupUser |")
//        Logger.i("user=$user")
//        Logger.i("MainViewModel=${this}")
//        Logger.i("=============")
//    }
//
//    fun checkUser() {
//        if (user.value == null) {
//            UserManager.userToken?.let {
//                getUserProfile(it)
//            }
//        }
//    }
//
//    fun navigateToLoginSuccess(user: User) {
//        _navigateToLoginSuccess.value = user
//    }
//
//    fun onLoginSuccessNavigated() {
//        _navigateToLoginSuccess.value = null
//    }
//
//    fun navigateToProfileByBottomNav(user: User) {
//        _navigateToProfileByBottomNav.value = user
//    }
//
//    fun onProfileNavigated() {
//        _navigateToProfileByBottomNav.value = null
//    }
//
//    fun navigateToHomeByBottomNav() {
//        _navigateToHomeByBottomNav.value = true
//    }
//
//    fun onHomeNavigated() {
//        _navigateToHomeByBottomNav.value = null
//    }
//
//    /**
//     * track [StylishRepository.getUserProfile]: -> [DefaultStylishRepository] : [StylishRepository] -> [StylishRemoteDataSource] : [StylishDataSource]
//     * @param token: Stylish token
//     */
//    private fun getUserProfile(token: String) {
//
//        coroutineScope.launch {
//
//            _status.value = LoadApiStatus.LOADING
//
//            val result = notedRepository.getUserProfile(token)
//
//            _user.value = when (result) {
//
//                is Result.Success -> {
//                    _error.value = null
//                    _status.value = LoadApiStatus.DONE
//                    result.data
//                }
//                is Result.Fail -> {
//                    _error.value = result.error
//                    _status.value = LoadApiStatus.ERROR
//                    if (result.error.contains("Invalid Access Token", true)) {
//                        UserManager.clear()
//                    }
//                    null
//                }
//                is Result.Error -> {
//                    _error.value = result.exception.toString()
//                    _status.value = LoadApiStatus.ERROR
//                    null
//                }
//                else -> {
//                    _error.value = getString(R.string.you_know_nothing)
//                    _status.value = LoadApiStatus.ERROR
//                    null
//                }
//            }
//        }
//    }
}