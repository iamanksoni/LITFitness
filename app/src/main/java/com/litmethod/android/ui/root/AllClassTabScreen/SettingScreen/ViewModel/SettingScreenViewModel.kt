package com.litmethod.android.ui.root.AllClassTabScreen.SettingScreen.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.GetCountries.GetCountriesRequest
import com.litmethod.android.models.GetCountries.GetCountriesResponse
import com.litmethod.android.models.LogOut.LogOutRequest
import com.litmethod.android.models.LogOut.LogOutResponse
import com.litmethod.android.network.EditProfileRepository
import com.litmethod.android.network.SettingFragmentRepository
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingScreenViewModel constructor(private val repository: SettingFragmentRepository, var context: Context) :
    ViewModel() {

    val logOutResponse= MutableLiveData<LogOutResponse>()
    val errorMessage = MutableLiveData<String>()

    private fun getLogOut(auth: String,logOutRequest: LogOutRequest) {
        val response = repository.logOut(auth,logOutRequest)
        response.enqueue(object : Callback<LogOutResponse> {
            override fun onResponse(
                call: Call<LogOutResponse>,
                response: Response<LogOutResponse>
            ) {
                if(response.code() == 200) {
                    logOutResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<LogOutResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun checkgetLogOut(auth: String,logOutRequest: LogOutRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getLogOut(
                    auth ,
                    logOutRequest

                )
            }
        }
    }
}