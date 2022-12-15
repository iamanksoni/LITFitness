package com.litmethod.android.network

import com.litmethod.android.models.LogOut.LogOutRequest

class LiveClassFragmentRepository constructor(private val retrofitService: RetrofitDataSourceService){
    fun getLiveClass(auth:String,injuryRequest: LogOutRequest) = retrofitService.getLiveClass(auth,injuryRequest)
}