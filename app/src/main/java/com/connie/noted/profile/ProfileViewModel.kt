package com.connie.noted.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.connie.noted.data.User

class ProfileViewModel : ViewModel() {

    private val _user = MutableLiveData<User>().apply {
        value = User(
            "tzuxlin",
            "Xuan Lin",
            "annul7tin@gmail.com",
            listOf("Kotlin", "Cat"),
            "https://graph.facebook.com/3526320860715512/picture?type=large"
        )
    }



    val user: LiveData<User>
        get() = _user


}
