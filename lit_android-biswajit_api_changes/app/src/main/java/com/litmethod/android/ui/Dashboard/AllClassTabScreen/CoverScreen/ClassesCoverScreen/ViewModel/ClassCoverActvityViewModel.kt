package com.litmethod.android.ui.Dashboard.AllClassTabScreen.CoverScreen.ClassesCoverScreen.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.AcountScreenFragment.ClassBookmark.ClassBookmarkRequest
import com.litmethod.android.models.AcountScreenFragment.ClassBookmark.ClassBookmarkResponse
import com.litmethod.android.models.GetCatagory.GetCatagoryRequest
import com.litmethod.android.models.GetCatagory.GetCatagoryResponse
import com.litmethod.android.models.GetInstructorInfo.GetInstructorInfoRequest
import com.litmethod.android.models.GetInstructorInfo.GetInstructorInfoResponse
import com.litmethod.android.network.ClassesCoverActivityRepository
import com.litmethod.android.network.ClassesFragmentRepository
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassCoverActvityViewModel constructor(private val repository: ClassesCoverActivityRepository, var context: Context) :
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
                Log.d("instrutorResponse","error is ${t.message}")
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

    val classBookmarkResponse = MutableLiveData<ClassBookmarkResponse>()
    val errorMessage2 = MutableLiveData<String>()

    private fun getClassBookmark(auth: String,classBookmarkRequest: ClassBookmarkRequest) {
        val response = repository.getClassBookmark(auth,classBookmarkRequest)
        response.enqueue(object : Callback<ClassBookmarkResponse> {
            override fun onResponse(
                call: Call<ClassBookmarkResponse>,
                response: Response<ClassBookmarkResponse>
            ) {
                if(response.code() == 200) {
                    classBookmarkResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage2.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<ClassBookmarkResponse>, t: Throwable) {
                errorMessage2.postValue(t.message)
            }
        })
    }

    fun checkgetClassBookmark(auth: String,classBookmarkRequest: ClassBookmarkRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getClassBookmark(
                    auth ,
                    classBookmarkRequest

                )
            }
        }
    }
}