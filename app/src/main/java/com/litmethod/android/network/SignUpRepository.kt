package com.litmethod.android.network

import com.litmethod.android.models.SignInRequest
import com.litmethod.android.models.SignUpRequest

class SignUpRepository constructor(private val retrofitService: RetrofitService) {
    fun signUpUser(signUpRequest: SignUpRequest) = retrofitService.signUpUser(signUpRequest)
}