package com.litmethod.android.network

import com.litmethod.android.models.SetImageResponse.SetImageResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface IApi {


    @Multipart
    @POST("me/setProfileImage")
    fun uploadImage( @Part image:MultipartBody.Part):Call<SetImageResponse>


}