package com.litmethod.android.network

import com.litmethod.android.models.GetCountries.GetCountriesRequest
import com.litmethod.android.models.LogOut.LogOutRequest

class SettingFragmentRepository constructor(private val retrofitService: RetrofitService) {
    fun logOut(auth:String,logOutRequest: LogOutRequest) = retrofitService.logOut(auth,logOutRequest)
}