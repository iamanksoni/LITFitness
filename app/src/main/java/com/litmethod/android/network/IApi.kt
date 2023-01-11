package com.litmethod.android.network

import com.litmethod.android.models.SetImageResponse.SetImageResponse
import com.litmethod.android.utlis.AppConstants
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface IApi {


    @Multipart
    @POST("me/setProfileImage")
    fun uploadImage(@Header(AppConstants.AUTH_TOKEN) authorizationKey: String, @Part image:MultipartBody.Part):Call<SetImageResponse>


}