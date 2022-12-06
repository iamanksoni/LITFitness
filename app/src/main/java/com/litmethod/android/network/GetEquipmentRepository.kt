package com.litmethod.android.network

import com.litmethod.android.models.AcountScreenFragment.EditUserEquipment.EditUserEquipmentRequest
import com.litmethod.android.models.EditUser.EdiitUserRequest.EditUserRequest
import com.litmethod.android.models.InjuryRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class GetEquipmentRepository constructor(private val retrofitService: RetrofitService){
    fun getEquipment(auth:String,injuryRequest: InjuryRequest) = retrofitService.getEquipment(auth,injuryRequest)
    fun editUser(auth:String,editUserRequest: EditUserRequest) = retrofitService.editUser(auth,editUserRequest)
    fun setImage(auth:String, fileName: MultipartBody.Part, action: RequestBody) = retrofitService.setImage(auth, fileName,action)
    fun editUserForEquipment(auth:String,editUserEquipmentRequest: EditUserEquipmentRequest) = retrofitService.editUserForEquipment(auth,editUserEquipmentRequest)
}