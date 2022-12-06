package com.litmethod.android.network

import com.litmethod.android.models.ClassHistoryList.ClassHistoryListRequest
import com.litmethod.android.models.LogOut.LogOutRequest

class WorkOutActivityRepository  constructor(private val retrofitService: RetrofitService) {
    fun getClassHistoryList(auth:String,classHistoryListRequest: ClassHistoryListRequest) = retrofitService.getClassHistoryList(auth,classHistoryListRequest)
}