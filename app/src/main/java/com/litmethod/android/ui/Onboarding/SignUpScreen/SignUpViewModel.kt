package com.litmethod.android.ui.Onboarding.SignUpScreen

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.SignUpRequest
import com.litmethod.android.models.SignUpResponse.SignUpResponse
import com.litmethod.android.network.SignUpRepository
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModell constructor(private val repository: SignUpRepository, var context: Context) :
    ViewModel()  {

    val signUpUserData = MutableLiveData<SignUpResponse>()
    val errorMessage = MutableLiveData<String>()

    fun signUpUserDataList(signUpRequest: SignUpRequest) {
        val response = repository.signUpUser(signUpRequest)
        response.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                if(response.code() == 200) {
                    signUpUserData.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun checkLogin(email: String, pass: String) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                signUpUserDataList(
                    SignUpRequest(AppConstants.actionTypeSignUp,email,true,pass
                    )
                )
            }
        }
    }

}