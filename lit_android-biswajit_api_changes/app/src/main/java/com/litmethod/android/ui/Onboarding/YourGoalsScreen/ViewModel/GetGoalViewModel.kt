package com.litmethod.android.ui.Onboarding.YourGoalsScreen.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.AcountScreenFragment.EditUserGoal.EditUserGoalRequest
import com.litmethod.android.models.GetCustomers.GetCustomerResponse
import com.litmethod.android.models.GetYourGoalResponse.GetYourGoalResponse
import com.litmethod.android.models.InjuryRequest
import com.litmethod.android.network.GetGoalRepository
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetGoalViewModel  constructor(private val repository: GetGoalRepository, var context: Context) :
    ViewModel() {
    val getGoalData = MutableLiveData<GetYourGoalResponse>()
    val errorMessage = MutableLiveData<String>()

    val editUserForGoalResponse = MutableLiveData<GetCustomerResponse>()
    val errorMessage2 = MutableLiveData<String>()

    private fun getInjuryDataList(auth:String,injuryRequest: InjuryRequest) {
        val response = repository.getGoal(auth,injuryRequest)
        response.enqueue(object : Callback<GetYourGoalResponse> {
            override fun onResponse(
                call: Call<GetYourGoalResponse>,
                response: Response<GetYourGoalResponse>
            ) {
                if(response.code() == 200) {
                    getGoalData.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetYourGoalResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun checkgetGoal(auth: String) {
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
                    InjuryRequest("getGoal")
                )
            }
        }
    }




    private fun editUserForGoal(auth: String,editUserGoalRequest: EditUserGoalRequest) {
        val response = repository.editUserForGoal(auth,editUserGoalRequest)
        response.enqueue(object : Callback<GetCustomerResponse> {
            override fun onResponse(
                call: Call<GetCustomerResponse>,
                response: Response<GetCustomerResponse>
            ) {
                if(response.code() == 200) {
                    editUserForGoalResponse.postValue(response.body())
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

    fun checkEditUserForGoal(auth: String,editUserGoalRequest: EditUserGoalRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                editUserForGoal(
                    auth ,
                    editUserGoalRequest

                )
            }
        }
    }
}