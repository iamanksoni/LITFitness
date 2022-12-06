package com.litmethod.android.ui.Dashboard.LiveClassTabScreen.LiveClassFragmentScreen.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.GetInstructorInfo.GetInstructorInfoRequest
import com.litmethod.android.models.GetInstructorInfo.GetInstructorInfoResponse
import com.litmethod.android.models.Liveclass.LiveClassResponse
import com.litmethod.android.models.LogOut.LogOutRequest
import com.litmethod.android.network.ClassesCoverActivityRepository
import com.litmethod.android.network.LiveClassFragmentRepository
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LiveScreenFragmentViewModel constructor(private val repository: LiveClassFragmentRepository, var context: Context) :
    ViewModel()  {

    val getLiveClassResponse = MutableLiveData<LiveClassResponse>()
    val errorMessage = MutableLiveData<String>()

    private fun getLiveClass(auth: String,logOutRequest: LogOutRequest) {
        val response = repository.getLiveClass(auth,logOutRequest)
        response.enqueue(object : Callback<LiveClassResponse> {
            override fun onResponse(
                call: Call<LiveClassResponse>,
                response: Response<LiveClassResponse>
            ) {
                if(response.code() == 200) {
                    getLiveClassResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<LiveClassResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
                Log.d("instrutorResponse","error is ${t.message}")
            }
        })
    }

    fun checkGetLiveClass(auth: String,logOutRequest: LogOutRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getLiveClass(
                    auth ,
                    logOutRequest

                )
            }
        }
    }
}