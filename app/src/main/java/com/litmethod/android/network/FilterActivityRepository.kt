package com.litmethod.android.network

import com.litmethod.android.models.FilterList.FilterListRequest

class FilterActivityRepository  constructor(private val retrofitService: RetrofitDataSourceService) {
    fun getFilterList(auth:String,filterListRequest: FilterListRequest) = retrofitService.getFilterList(auth,filterListRequest)
}