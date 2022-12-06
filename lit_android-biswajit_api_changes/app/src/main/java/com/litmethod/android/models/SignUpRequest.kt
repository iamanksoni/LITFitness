package com.litmethod.android.models

data class SignUpRequest(
    val action: String,
    val email: String,
    val newsLetterSelected: Boolean,
    val password: String
)