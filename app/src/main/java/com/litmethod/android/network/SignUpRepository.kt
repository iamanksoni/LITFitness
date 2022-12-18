package com.litmethod.android.network

import com.litmethod.android.models.SignUpRequest

class SignUpRepository constructor(private val retrofitService: RetrofitDataSourceService) {
    fun signUpUser(signUpRequest: SignUpRequest) = retrofitService.signUpUser(signUpRequest)
}