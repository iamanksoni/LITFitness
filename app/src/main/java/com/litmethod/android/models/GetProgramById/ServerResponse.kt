package com.litmethod.android.models.GetProgramById

data class ServerResponse(
    val message: String,
    val statusCode: Int,
    val subscribtionStatus: SubscribtionStatus,
    val success: Boolean
)