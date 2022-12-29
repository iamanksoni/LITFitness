package com.litmethod.android.ui.Onboarding.InjuryScreen.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.InjuryRequest
import com.litmethod.android.models.InjuryResponse.InjuryResponse
import com.litmethod.android.models.SignInRequest
import com.litmethod.android.network.InjuryRepository
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InjuryViewModel constructor(private val repository: InjuryRepository, var context: Context) :
    ViewModel() {

    val injuryData = MutableLiveData<InjuryResponse>()
    val errorMessage = MutableLiveData<String>()

   private fun getInjuryDataList(auth:String,injuryRequest: InjuryRequest) {
        val response = repository.getInjury(auth,injuryRequest)
        response.enqueue(object : Callback<InjuryResponse> {
            override fun onResponse(
                call: Call<InjuryResponse>,
                response: Response<InjuryResponse>
            ) {
                if(response.code() == 200) {
                    injuryData.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<InjuryResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun checkgetInjury(auth: String) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getInjuryDataList(
                    auth,
                    InjuryRequest("getInjury")
                )
            }
        }
    }

}