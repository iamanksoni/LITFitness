package com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ProgramsCoverScreen.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.ClassDetails.ClassDetailsRequest
import com.litmethod.android.models.ClassDetails.ClassDetailsResponse
import com.litmethod.android.network.ClassesCoverActivityRepository
import com.litmethod.android.network.ProgramsCoverActivityRepository
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProgramsCoverAcivityViewModel constructor(private val repository: ProgramsCoverActivityRepository, var context: Context) :
    ViewModel() {
    val classDetailsResponse = MutableLiveData<ClassDetailsResponse>()
    val errorMessage6 = MutableLiveData<String>()

    private fun getClassDetails(auth: String,classDetailsRequest: ClassDetailsRequest) {
        val response = repository.getClassDetails(auth,classDetailsRequest)
        response.enqueue(object : Callback<ClassDetailsResponse> {
            override fun onResponse(
                call: Call<ClassDetailsResponse>,
                response: Response<ClassDetailsResponse>
            ) {
                if(response.code() == 200) {
                    classDetailsResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage6.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<ClassDetailsResponse>, t: Throwable) {
                errorMessage6.postValue(t.message)
            }
        })
    }

    fun checkgetClassDetails(auth: String,classDetailsRequest: ClassDetailsRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getClassDetails(
                    auth ,
                    classDetailsRequest

                )
            }
        }
    }
}