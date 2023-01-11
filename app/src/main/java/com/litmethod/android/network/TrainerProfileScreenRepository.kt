package com.litmethod.android.network

import com.litmethod.android.models.ClassDetails.ClassDetailsRequest
import com.litmethod.android.models.GetInstructorInfo.GetInstructorInfoRequest

class TrainerProfileScreenRepository constructor(private val retrofitService: RetrofitDataSourceService) {
    fun getInstructorInfo(auth:String,getInstructorInfoRequest: GetInstructorInfoRequest) = retrofitService.getInstructorInfo(auth,getInstructorInfoRequest)
    fun getClassDetails(auth:String,glassDetailsRequest: ClassDetailsRequest) = retrofitService.getClassDetails(auth,glassDetailsRequest)


}