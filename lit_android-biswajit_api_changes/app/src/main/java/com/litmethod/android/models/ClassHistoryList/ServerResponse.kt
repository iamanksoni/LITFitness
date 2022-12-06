package com.litmethod.android.models.ClassHistoryList

data class ServerResponse(
    val message: String,
    val statusCode: Int,
    val subscribtionStatus: SubscribtionStatus,
    val success: Boolean
)