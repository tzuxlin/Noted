package com.connie.noted.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.data.User

class ProfileViewModel : ViewModel() {

    private val _user = MutableLiveData<User>().apply {
        value = User()
    }



    val user: LiveData<User>
        get() = _user


}
