package com.connie.noted.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.data.User

class ProfileViewModel : ViewModel() {

    val user = MutableLiveData<User>().apply {
        value = User()
    }



}
