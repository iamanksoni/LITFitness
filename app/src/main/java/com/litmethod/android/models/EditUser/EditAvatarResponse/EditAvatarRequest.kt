package com.litmethod.android.models.EditUser.EditAvatarResponse

import okhttp3.MultipartBody

data class EditAvatarRequest(val fileName: MultipartBody.Part,val action:String)
