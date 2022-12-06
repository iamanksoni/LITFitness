package com.litmethod.android.network

import com.litmethod.android.models.InjuryRequest

class GetLevelRepository constructor(private val retrofitService: RetrofitService) {
    fun getLevel(auth:String,injuryRequest: InjuryRequest) = retrofitService.getLevel(auth,injuryRequest)
}