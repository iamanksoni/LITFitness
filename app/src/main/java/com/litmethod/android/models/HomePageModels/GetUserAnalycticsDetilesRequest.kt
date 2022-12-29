package com.litmethod.android.models.HomePageModels

data class GetUserAnalycticsDetilesRequest(
    val action: String,
    val filter: String,
    val key: String,
    val subKey: String,
    val timeZone: String
)