package com.litmethod.android.ui.Onboarding.LevelScreen.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.GetInterest.GetInterestResponse
import com.litmethod.android.models.GetLevel.GetLevelResponse
import com.litmethod.android.models.InjuryRequest
import com.litmethod.android.network.GetInterestRepository
import com.litmethod.android.network.GetLevelRepository
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetLevelViewModel  constructor(private val repository: GetLevelRepository, var context: Context) :
    ViewModel()  {

    val getLevelData = MutableLiveData<GetLevelResponse>()
    val errorMessage = MutableLiveData<String>()

    private fun getLevelDataList(auth:String,injuryRequest: InjuryRequest) {
        val response = repository.getLevel(auth,injuryRequest)
        response.enqueue(object : Callback<GetLevelResponse> {
            override fun onResponse(
                call: Call<GetLevelResponse>,
                response: Response<GetLevelResponse>
            ) {
                if(response.code() == 200) {
                    getLevelData.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetLevelResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun checkgetLevel(auth:String) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getLevelDataList(auth,
                    InjuryRequest("getLevel")
                )
            }
        }
    }

}