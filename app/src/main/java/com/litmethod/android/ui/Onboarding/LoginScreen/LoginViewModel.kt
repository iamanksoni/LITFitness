package com.litmethod.android.ui.Onboarding.LoginScreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.GetCustomers.GetCustomerResponse
import com.litmethod.android.models.SignInRequest
import com.litmethod.android.network.SignInRepository
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel constructor(private val repository: SignInRepository, var context: Context) :
    ViewModel() {
    val signInUserData = MutableLiveData<GetCustomerResponse>()
    val errorMessage = MutableLiveData<String>()

    fun signInUserDataList(signInRequest: SignInRequest) {
        val response = repository.signInUser(signInRequest)
        response.enqueue(object : Callback<GetCustomerResponse> {
            override fun onResponse(
                call: Call<GetCustomerResponse>,
                response: Response<GetCustomerResponse>
            ) {
                Log.d("theDataIS","the data is $response")
                if(response.code() == 200) {
                    signInUserData.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetCustomerResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
                Log.d("theDataIS","the data error is ${t.message}")
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
                Log.d("theDataIS","the data $email and pass $pass")
                signInUserDataList(SignInRequest("jhgdhfjukujyhtrdfuytetjuyjhetsdhf",AppConstants.OSType,
                AppConstants.actionTypeSignIn,email,pass))
            }
        }
    }
}