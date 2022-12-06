package com.litmethod.android.network

import com.litmethod.android.models.AcountScreenFragment.ClassBookmark.ClassBookmarkRequest
import com.litmethod.android.models.ClassDetails.ClassDetailsRequest
import com.litmethod.android.models.GetInstructorInfo.GetInstructorInfoRequest

class ClassesCoverActivityRepository constructor(private val retrofitService: RetrofitService) {
    fun getInstructorInfo(auth:String,getInstructorInfoRequest: GetInstructorInfoRequest) = retrofitService.getInstructorInfo(auth,getInstructorInfoRequest)
    fun getClassBookmark(auth:String,classBookmarkRequest: ClassBookmarkRequest) = retrofitService.getClassBookmark(auth,classBookmarkRequest)
}