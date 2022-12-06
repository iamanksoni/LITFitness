package com.litmethod.android.network

import com.litmethod.android.models.GetCustomers.GetCutomerRequest

class SplashScreenRepository constructor(private val retrofitService: RetrofitService){
    fun getCustomer(auth: String,getCutomerRequest: GetCutomerRequest) = retrofitService.getCustomer(auth,getCutomerRequest)
}