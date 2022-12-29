package com.litmethod.android.ui.Onboarding.ForgotPasswordScreen.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.ForgetPasswordModel.ForgetPasswordRequest
import com.litmethod.android.models.ForgetPasswordModel.ForgetPasswordResponse
import com.litmethod.android.models.GetCatagory.GetCatagoryRequest
import com.litmethod.android.models.GetCatagory.GetCatagoryResponse
import com.litmethod.android.network.ForgetPasswordRepository
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordViewModel constructor(
    private val repository: ForgetPasswordRepository,
    var context: Context
) :
    ViewModel() {

    val forgetPassword = MutableLiveData<ForgetPasswordResponse>()
    val errorMessage = MutableLiveData<String>()

    fun getForgetPassword(request: ForgetPasswordRequest) {
        val response = repository.forgetPasswordService( request)
        response.enqueue(object : Callback<ForgetPasswordResponse> {
            override fun onResponse(
                call: Call<ForgetPasswordResponse>,
                response: Response<ForgetPasswordResponse>
            ) {
                if (response.code() == 200) {
                    forgetPassword.postValue(response.body())
                } else if (response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(
                        jObjError.getJSONObject("serverResponse").getString("message")
                    )
                }
            }

            override fun onFailure(call: Call<ForgetPasswordResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }


}