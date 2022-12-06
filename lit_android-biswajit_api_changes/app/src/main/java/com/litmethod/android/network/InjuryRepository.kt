package com.litmethod.android.network

import com.litmethod.android.models.InjuryRequest
import com.litmethod.android.models.SignInRequest

class InjuryRepository constructor(private val retrofitService: RetrofitService) {
    fun getInjury(auth:String,injuryRequest: InjuryRequest) = retrofitService.getInjury(auth,injuryRequest)
}