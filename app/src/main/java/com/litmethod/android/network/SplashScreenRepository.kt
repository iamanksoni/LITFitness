package com.litmethod.android.network

import com.litmethod.android.models.GetCustomers.GetCutomerRequest

class SplashScreenRepository constructor(private val retrofitService: RetrofitDataSourceService){
    fun getCustomer(auth: String,getCutomerRequest: GetCutomerRequest) = retrofitService.getCustomer(auth,getCutomerRequest)
}