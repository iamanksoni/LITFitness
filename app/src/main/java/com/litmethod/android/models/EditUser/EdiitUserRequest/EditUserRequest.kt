package com.litmethod.android.models.EditUser.EdiitUserRequest

data class EditUserRequest(
    val action: String,
    val dob: String,
    val equipment: List<String>,
    val first_name: String,
    val gender: String,
    val goal: List<String>,
    val has_injury: Boolean,
    val heightUnit: String,
    val heightValueFeet: Int,
    val heightValueInches: Int,
    val injury: List<String>?,
    val interest: List<String>,
    val last_name: String,
    val level: List<String>,
    val onbordingStatus: Boolean,
    val unit: String,
    val username: String,
    val weight: Int
)