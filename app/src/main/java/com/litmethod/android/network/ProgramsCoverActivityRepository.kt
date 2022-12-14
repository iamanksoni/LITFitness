package com.litmethod.android.network

import com.litmethod.android.models.ClassDetails.ClassDetailsRequest

class ProgramsCoverActivityRepository constructor(private val retrofitService: RetrofitDataSourceService) {
    fun getClassDetails(auth:String,glassDetailsRequest: ClassDetailsRequest) = retrofitService.getClassDetails(auth,glassDetailsRequest)
}