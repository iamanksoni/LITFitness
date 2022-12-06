package com.litmethod.android.models.ClassHistoryList

data class ClassHistoryListRequest(
    val NoOfClass: Int,
    val action: String,
    val categoryId: String,
    val currentPage: Int,
    val month: String,
    val year: String
)