package com.litmethod.android.network

import com.litmethod.android.models.LogOut.LogOutRequest

class SettingFragmentRepository constructor(private val retrofitService: RetrofitDataSourceService) {
    fun logOut(auth:String,logOutRequest: LogOutRequest) = retrofitService.logOut(auth,logOutRequest)
}