package com.litmethod.android.network

import com.litmethod.android.models.AcountScreenFragment.EditUserInterest.EditUserInterestRequest
import com.litmethod.android.models.InjuryRequest

class GetInterestRepository constructor(private val retrofitService: RetrofitDataSourceService){
    fun getInterest(auth:String,injuryRequest: InjuryRequest) = retrofitService.getInterest(auth,injuryRequest)
    fun editUserForInterest(auth:String,editUserInterestRequest: EditUserInterestRequest) = retrofitService.editUserForInterest(auth,editUserInterestRequest)
}