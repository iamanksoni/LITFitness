package com.litmethod.android.models.ForgetPasswordModel

data class ForgetPasswordResponse(
	val serverResponse: ServerResponse? = null
)

data class ServerResponse(
	val success: Boolean? = null,
	val message: String? = null,
	val statusCode: Int? = null
)

