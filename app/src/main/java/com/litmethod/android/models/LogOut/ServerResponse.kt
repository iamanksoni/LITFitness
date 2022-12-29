package com.litmethod.android.models.LogOut

data class ServerResponse(
    val message: String,
    val statusCode: Int,
    val success: Boolean
)