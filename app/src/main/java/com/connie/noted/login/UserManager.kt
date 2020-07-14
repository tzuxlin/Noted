package com.connie.noted.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.connie.noted.NotedApplication
import com.connie.noted.data.User

/**
 * Created by Wayne Chen in Jul. 2019.
 */
object UserManager {

    private const val USER_DATA = "user_data"
    private const val USER_TOKEN = "user_token"
    private const val USER_ID = "user_id"

    private val _user = MutableLiveData<User>().apply {
        value = User()
    }

    val user: LiveData<User>
        get() = _user

    var userToken: String? = null
        get() = NotedApplication.instance
            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
            .getString(USER_TOKEN, null)
        set(value) {
            field = when (value) {
                null -> {
                    NotedApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .remove(USER_TOKEN)
                        .apply()
                    null
                }
                else -> {
                    NotedApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .putString(USER_TOKEN, value)
                        .apply()
                    value
                }
            }
        }





    var userId: Int? = null
        get() = NotedApplication.instance
            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
            .getInt(USER_ID, -1)
        set(value) {
            NotedApplication.instance
                .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
                .edit()
                .putInt(USER_ID, value!!)
                .apply()
            field = value
        }

    /**
     * It can be use to check login status directly
     */
    val isLoggedIn: Boolean
        get() = userToken != null

    /**
     * Clear the [userToken] and the [user]/[_user] data
     */
    fun clear() {
        userToken = null
        userId = null
        _user.value = null
    }


}

