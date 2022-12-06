package com.litmethod.android.ui.Onboarding.YourInterestScreen.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.AcountScreenFragment.EditUserGoal.EditUserGoalRequest
import com.litmethod.android.models.AcountScreenFragment.EditUserInterest.EditUserInterestRequest
import com.litmethod.android.models.GetCustomers.GetCustomerResponse
import com.litmethod.android.models.GetInterest.GetInterestResponse
import com.litmethod.android.models.InjuryRequest
import com.litmethod.android.models.InjuryResponse.InjuryResponse
import com.litmethod.android.network.GetInterestRepository
import com.litmethod.android.network.InjuryRepository
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetInterestViewModel constructor(private val repository: GetInterestRepository, var context: Context) :
    ViewModel() {

    val interestData = MutableLiveData<GetInterestResponse>()
    val errorMessage = MutableLiveData<String>()

    private fun getInterestDataList(auth:String,injuryRequest: InjuryRequest) {
        val response = repository.getInterest(auth,injuryRequest)
        response.enqueue(object : Callback<GetInterestResponse> {
            override fun onResponse(
                call: Call<GetInterestResponse>,
                response: Response<GetInterestResponse>
            ) {
                if(response.code() == 200) {
                    interestData.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetInterestResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun checkgetInterest(auth: String) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getInterestDataList(
                    auth,
                    InjuryRequest("getInterest")
                )
            }
        }
    }

    val editUserForInterestResponse = MutableLiveData<GetCustomerResponse>()
    val errorMessage2 = MutableLiveData<String>()

    private fun editUserForInterest(auth: String,editUserInterestRequest: EditUserInterestRequest) {
        val response = repository.editUserForInterest(auth,editUserInterestRequest)
        response.enqueue(object : Callback<GetCustomerResponse> {
            override fun onResponse(
                call: Call<GetCustomerResponse>,
                response: Response<GetCustomerResponse>
            ) {
                if(response.code() == 200) {
                    editUserForInterestResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage2.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetCustomerResponse>, t: Throwable) {
                errorMessage2.postValue(t.message)
            }
        })
    }

    fun checkEditUserForInterest(auth: String,editUserInterestRequest: EditUserInterestRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                editUserForInterest(
                    auth ,
                    editUserInterestRequest

                )
            }
        }
    }

}