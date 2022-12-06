package com.litmethod.android.network

import com.litmethod.android.models.FilterList.FilterListRequest
import com.litmethod.android.models.SignInRequest

class FilterActivityRepository  constructor(private val retrofitService: RetrofitService) {
    fun getFilterList(auth:String,filterListRequest: FilterListRequest) = retrofitService.getFilterList(auth,filterListRequest)
}