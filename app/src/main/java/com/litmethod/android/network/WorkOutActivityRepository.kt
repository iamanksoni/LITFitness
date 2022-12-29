package com.litmethod.android.network

import com.litmethod.android.models.ClassHistoryList.ClassHistoryListRequest

class WorkOutActivityRepository  constructor(private val retrofitService: RetrofitDataSourceService) {
    fun getClassHistoryList(auth:String,classHistoryListRequest: ClassHistoryListRequest) = retrofitService.getClassHistoryList(auth,classHistoryListRequest)
}