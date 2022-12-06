package com.litmethod.android.models.GetInstructorInfo

data class GetInstructorInfoRequest(
    val NoOfClass: String,
    val action: String,
    val catId: String,
    val pageNo: String
)