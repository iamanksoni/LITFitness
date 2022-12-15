package com.litmethod.android.network

import com.litmethod.android.models.EditUserRequestNullable.EditUserRequestNullable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditProfileRepository constructor(private val retrofitService: RetrofitDataSourceService) {
    fun editUserRequestNullable(auth:String,editUserRequestNullable: EditUserRequestNullable) = retrofitService.editUserNullable(auth,editUserRequestNullable)
    fun setImage(auth:String, fileName: MultipartBody.Part, action: RequestBody) = retrofitService.setImage(auth, fileName,action)
}
