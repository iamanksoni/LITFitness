package com.litmethod.android.ui.Dashboard.AllClassTabScreen.FilterScreen.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.FilterList.FilterListRequest
import com.litmethod.android.models.FilterList.FilterListResponse
import com.litmethod.android.models.GetInstructorInfo.GetInstructorInfoRequest
import com.litmethod.android.models.GetInstructorInfo.GetInstructorInfoResponse
import com.litmethod.android.network.FilterActivityRepository
import com.litmethod.android.network.TrainerProfileScreenRepository
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilterActivityViewModel constructor(private val repository: FilterActivityRepository, var context: Context) :
    ViewModel()  {

    val getFilterListResponse= MutableLiveData<FilterListResponse>()
    val errorMessage = MutableLiveData<String>()

    private fun getFilterList(auth: String,filterListRequest: FilterListRequest) {
        val response = repository.getFilterList(auth,filterListRequest)
        response.enqueue(object : Callback<FilterListResponse> {
            override fun onResponse(
                call: Call<FilterListResponse>,
                response: Response<FilterListResponse>
            ) {
                if(response.code() == 200) {
                    getFilterListResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<FilterListResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun checkGetFilterList(auth: String,filterListRequest: FilterListRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getFilterList(
                    auth ,
                    filterListRequest

                )
            }
        }
    }
}