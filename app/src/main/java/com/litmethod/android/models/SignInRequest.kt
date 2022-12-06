package com.litmethod.android.models

data class SignInRequest(
    val FCMToken: String,
    val OSType: String,
    val action: String,
    val email: String,
    val password: String
)