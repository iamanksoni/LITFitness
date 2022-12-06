package com.litmethod.android.network

import com.litmethod.android.models.ForgetPasswordModel.ForgetPasswordRequest

class ForgetPasswordRepository constructor(private val retrofitService: RetrofitService) {
    fun forgetPasswordService(forgetPasswordRequest: ForgetPasswordRequest) =
        retrofitService.forgetPasswordRequest(forgetPasswordRequest)
}