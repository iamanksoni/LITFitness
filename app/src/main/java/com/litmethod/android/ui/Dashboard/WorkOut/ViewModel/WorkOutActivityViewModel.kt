package com.litmethod.android.ui.Dashboard.WorkOut.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.ClassHistoryList.ClassHistoryListRequest
import com.litmethod.android.models.ClassHistoryList.ClassHistoryListResponse
import com.litmethod.android.models.Liveclass.LiveClassResponse
import com.litmethod.android.models.LogOut.LogOutRequest
import com.litmethod.android.network.LiveClassFragmentRepository
import com.litmethod.android.network.WorkOutActivityRepository
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkOutActivityViewModel  constructor(private val repository: WorkOutActivityRepository, var context: Context) :
    ViewModel()  {

    val classHistoryListResponse = MutableLiveData<ClassHistoryListResponse>()
    val errorMessage = MutableLiveData<String>()

    private fun getClassHistoryList(auth: String,classHistoryListRequest: ClassHistoryListRequest) {
        val response = repository.getClassHistoryList(auth,classHistoryListRequest)
        response.enqueue(object : Callback<ClassHistoryListResponse> {
            override fun onResponse(
                call: Call<ClassHistoryListResponse>,
                response: Response<ClassHistoryListResponse>
            ) {
                if(response.code() == 200) {
                    classHistoryListResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<ClassHistoryListResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
                Log.d("instrutorResponse","error is ${t.message}")
            }
        })
    }

    fun checkgetClassHistoryList(auth: String,classHistoryListRequest: ClassHistoryListRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getClassHistoryList(
                    auth ,
                    classHistoryListRequest

                )
            }
        }
    }
}