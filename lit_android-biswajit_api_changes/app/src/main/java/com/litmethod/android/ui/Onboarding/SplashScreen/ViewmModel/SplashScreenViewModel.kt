package com.litmethod.android.ui.Onboarding.SplashScreen.ViewmModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.GetCustomers.GetCustomerResponse
import com.litmethod.android.models.GetCustomers.GetCutomerRequest
import com.litmethod.android.network.SplashScreenRepository
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreenViewModel constructor(private val repository: SplashScreenRepository, var context: Context) :
    ViewModel()  {

    val getCustomerResponse = MutableLiveData<GetCustomerResponse>()
    val errorMessage = MutableLiveData<String>()

    fun getCustomer(token:String,getCutomerRequest: GetCutomerRequest) {
        val response = repository.getCustomer(token,getCutomerRequest)
        response.enqueue(object : Callback<GetCustomerResponse> {
            override fun onResponse(
                call: Call<GetCustomerResponse>,
                response: Response<GetCustomerResponse>
            ) {
                Log.d("theDataIS","the data is $response")
                if(response.code() == 200) {
                    getCustomerResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetCustomerResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun checkGetCustomer(token: String, getCutomerRequest: GetCutomerRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {

                getCustomer(
                  token,getCutomerRequest
                )
            }
        }
    }

}