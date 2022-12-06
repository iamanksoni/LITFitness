package com.litmethod.android.ui.Dashboard.AllClassTabScreen.CoverScreen.TrainerProfileScreen.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.GetInstructorInfo.GetInstructorInfoRequest
import com.litmethod.android.models.GetInstructorInfo.GetInstructorInfoResponse
import com.litmethod.android.network.ClassesCoverActivityRepository
import com.litmethod.android.network.TrainerProfileScreenRepository
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrainerProfileViewModel constructor(private val repository: TrainerProfileScreenRepository, var context: Context) :
    ViewModel()  {

    val getInstructorInfoResponse = MutableLiveData<GetInstructorInfoResponse>()
    val errorMessage = MutableLiveData<String>()

    private fun getInstructorInfoRequest(auth: String,getInstructorInfoRequest: GetInstructorInfoRequest) {
        val response = repository.getInstructorInfo(auth,getInstructorInfoRequest)
        response.enqueue(object : Callback<GetInstructorInfoResponse> {
            override fun onResponse(
                call: Call<GetInstructorInfoResponse>,
                response: Response<GetInstructorInfoResponse>
            ) {
                if(response.code() == 200) {
                    getInstructorInfoResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetInstructorInfoResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun checkGetInstructorInfoRequest(auth: String,getInstructorInfoRequest: GetInstructorInfoRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getInstructorInfoRequest(
                    auth ,
                    getInstructorInfoRequest

                )
            }
        }
    }
}