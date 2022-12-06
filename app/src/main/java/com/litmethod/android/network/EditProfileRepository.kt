package com.litmethod.android.network

import com.litmethod.android.models.EditUser.EditAvatarResponse.EditAvatarRequest
import com.litmethod.android.models.EditUserRequestNullable.EditUserRequestNullable
import com.litmethod.android.models.GetCatagory.GetCatagoryRequest
import com.litmethod.android.models.GetCity.GetCityRequest
import com.litmethod.android.models.GetCountries.GetCountriesRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditProfileRepository constructor(private val retrofitService: RetrofitService) {
    fun editUserRequestNullable(auth:String,editUserRequestNullable: EditUserRequestNullable) = retrofitService.editUserNullable(auth,editUserRequestNullable)
    fun setImage(auth:String, fileName: MultipartBody.Part, action: RequestBody) = retrofitService.setImage(auth, fileName,action)
}
