package com.connie.noted.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.connie.noted.MainActivity
import com.connie.noted.NotedApplication

/**
 * Updated by Wayne Chen in Mar. 2019.
 */
object Util {

    /**
     * Determine and monitor the connectivity status
     *
     * https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
     */
    fun isInternetConnected(): Boolean {
        val cm = NotedApplication.instance
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun getString(resourceId: Int): String {
        return NotedApplication.instance.getString(resourceId)
    }

    fun getColor(resourceId: Int): Int {
        return NotedApplication.instance.getColor(resourceId)
    }

    fun getWindowWidth(): Int{
        return NotedApplication.instance.resources.displayMetrics.widthPixels
    }
}
