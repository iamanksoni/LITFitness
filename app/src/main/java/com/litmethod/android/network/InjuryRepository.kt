package com.litmethod.android.network

import com.litmethod.android.models.InjuryRequest

class InjuryRepository constructor(private val retrofitService: RetrofitDataSourceService) {
    fun getInjury(auth:String,injuryRequest: InjuryRequest) = retrofitService.getInjury(auth,injuryRequest)
}