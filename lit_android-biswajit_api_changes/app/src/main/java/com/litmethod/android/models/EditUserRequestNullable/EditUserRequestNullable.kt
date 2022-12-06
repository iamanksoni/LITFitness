package com.litmethod.android.models.EditUserRequestNullable

data class EditUserRequestNullable(
    val action: String,
    val dob: String,
    val first_name: String,
    val gender: String,
    val heightUnit: String,
    val heightValueFeet: Int,
    val heightValueInches: Int,
    val last_name: String,
    val unit: String,
    val weight: Int
)