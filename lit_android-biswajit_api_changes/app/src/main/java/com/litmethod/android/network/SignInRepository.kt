package com.litmethod.android.network

import com.litmethod.android.models.SignInRequest

class SignInRepository constructor(private val retrofitService: RetrofitService) {
    fun signInUser(signInRequest: SignInRequest) = retrofitService.signInUser(signInRequest)
}