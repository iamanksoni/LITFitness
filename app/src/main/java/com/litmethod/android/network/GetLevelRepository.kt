package com.litmethod.android.network

import com.litmethod.android.models.InjuryRequest

class GetLevelRepository constructor(private val retrofitService: RetrofitDataSourceService) {
    fun getLevel(auth:String,injuryRequest: InjuryRequest) = retrofitService.getLevel(auth,injuryRequest)
}