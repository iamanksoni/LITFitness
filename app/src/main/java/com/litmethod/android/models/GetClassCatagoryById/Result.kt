package com.litmethod.android.models.GetClassCatagoryById

data class Result(
    val currentPage: Int,
    val `data`: List<Data>,
    val maxPage: Int,
    val totalCount: Int
)