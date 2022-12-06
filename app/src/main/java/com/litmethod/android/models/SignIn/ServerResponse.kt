package com.litmethod.android.models.SignIn

data class ServerResponse(
    val message: String,
    val statusCode: Int,
    val success: Boolean,
    val subscribtionStatus: SubscribtionStatus,
)