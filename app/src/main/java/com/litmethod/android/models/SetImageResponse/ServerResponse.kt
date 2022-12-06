package com.litmethod.android.models.SetImageResponse

import com.litmethod.android.models.InjuryResponse.SubscribtionStatus

data class ServerResponse(
    val message: String,
    val statusCode: Int,
    val success: Boolean,
    val subscribtionStatus: SubscribtionStatus
)