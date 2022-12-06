package com.litmethod.android.models.GetCity

data class GetCityRequest(
    val action: String,
    val countriCode: String,
    val getType: String
)