package com.litmethod.android.models.GetCustomers

import com.litmethod.android.models.SignIn.SubscribtionStatus

data class ServerResponse(
    val message: String,
    val statusCode: Int,
    val success: Boolean,
    val subscribtionStatus: SubscribtionStatus,
)