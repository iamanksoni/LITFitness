package com.litmethod.android.models.InjuryResponse

data class ServerResponse(
    val message: String,
    val statusCode: Int,
    val success: Boolean,
    val subscribtionStatus:SubscribtionStatus

)