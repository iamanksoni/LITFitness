package com.litmethod.android.utlis

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkHelper private constructor() {

    fun isNetworkAvailable(context: Context): Boolean {
        var activeNetworkInfo: NetworkInfo? = null
        try {
            activeNetworkInfo = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    companion object {
        private var ourInstance: NetworkHelper? = null
        val instance: NetworkHelper
            get() {
                if (ourInstance == null) {
                    ourInstance = NetworkHelper()
                }
                return ourInstance!!
            }
    }
}