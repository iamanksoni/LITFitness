package com.litmethod.android.network

import com.litmethod.android.models.ClassDetails.ClassDetailsRequest
import com.litmethod.android.models.GetAllAccessCatagory.GetAllAccessCatagoryRequest
import com.litmethod.android.models.GetAllAccessFilter.GetAllAccessFilterRequest
import com.litmethod.android.models.GetCatagory.GetCatagoryRequest
import com.litmethod.android.models.GetClassCatagoryById.GetClassCatagoryByIDRequest
import com.litmethod.android.models.GetProgram.GetProgramsRequest
import com.litmethod.android.models.GetProgramById.GetProgramByIdRequest

class ClassesFragmentRepository constructor(private val retrofitService: RetrofitService){
    fun getCatagory(auth:String,getCatagoryRequest: GetCatagoryRequest) = retrofitService.getCategory(auth,getCatagoryRequest)
    fun getClassCategoryById(auth:String,getClassCatagoryByIDRequest: GetClassCatagoryByIDRequest) = retrofitService.getClassCategoryById(auth,getClassCatagoryByIDRequest)
    fun getPrograms(auth:String,getProgramsRequest: GetProgramsRequest) = retrofitService.getPrograms(auth,getProgramsRequest)
    fun getAllAccessCatagory(auth:String,getAllAccessCatagoryRequest: GetAllAccessCatagoryRequest) = retrofitService.getAllAccessCatagory(auth,getAllAccessCatagoryRequest)
    fun getProgramsById(auth:String,getProgramByIdRequest: GetProgramByIdRequest) = retrofitService.getProgramsById(auth,getProgramByIdRequest)
    fun getClassDetails(auth:String,glassDetailsRequest: ClassDetailsRequest) = retrofitService.getClassDetails(auth,glassDetailsRequest)
    fun getAllAccessFilter(auth:String,getAllAccessFilterRequest: GetAllAccessFilterRequest) = retrofitService.getAllAccessFilter(auth,getAllAccessFilterRequest)

}
