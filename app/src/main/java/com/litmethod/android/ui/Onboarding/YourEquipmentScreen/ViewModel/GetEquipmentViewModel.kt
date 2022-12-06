package com.litmethod.android.ui.Onboarding.YourEquipmentScreen.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.AcountScreenFragment.EditUserEquipment.EditUserEquipmentRequest
import com.litmethod.android.models.EditUser.EdiitUserRequest.EditUserRequest
import com.litmethod.android.models.GetCustomers.GetCustomerResponse
import com.litmethod.android.models.GetEquipment.GetEquipMentResponse
import com.litmethod.android.models.InjuryRequest
import com.litmethod.android.models.SetImageResponse.SetImageResponse
import com.litmethod.android.network.GetEquipmentRepository
import com.litmethod.android.ui.Dashboard.AllClassTabScreen.ClassesFragmentScreen.Util.AllClassesDataObject
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetEquipmentViewModel  constructor(private val repository: GetEquipmentRepository, var context: Context) : ViewModel() {
    val equipmentData = MutableLiveData<GetEquipMentResponse>()
    val errorMessage = MutableLiveData<String>()

    val editUsertData = MutableLiveData<GetCustomerResponse>()
    val errorMessage2 = MutableLiveData<String>()

    val setImageData = MutableLiveData<SetImageResponse>()
    val errorMessage3 = MutableLiveData<String>()

    private fun getEquipmentDataList(auth:String,injuryRequest: InjuryRequest) {
        val response = repository.getEquipment(auth,injuryRequest)
        response.enqueue(object : Callback<GetEquipMentResponse> {
            override fun onResponse(
                call: Call<GetEquipMentResponse>,
                response: Response<GetEquipMentResponse>
            ) {
                if(response.code() == 200) {
                    equipmentData.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetEquipMentResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun checkgetEquipment(auth:String) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getEquipmentDataList(
                    auth,
                    InjuryRequest("getEquipment")
                )
            }
        }
    }

    private fun getEditUserDataList(auth:String,editUserRequest: EditUserRequest) {
        val response = repository.editUser(auth,editUserRequest)
        response.enqueue(object : Callback<GetCustomerResponse> {
            override fun onResponse(
                call: Call<GetCustomerResponse>,
                response: Response<GetCustomerResponse>
            ) {
                if(response.code() == 200) {
                    editUsertData.postValue(response.body())
                    Log.d("edit","edit user successfull")
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                   Log.d("edit","edit user failed $jObjError")
                    errorMessage2.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetCustomerResponse>, t: Throwable) {
                errorMessage2.postValue(t.message)
                Log.d("edit","edit user error")
            }
        })
    }

    fun checkgetEditUser( auth: String,dob:String,username:String,eqitpment:ArrayList<String>,firstName:String,gender:String,goal:ArrayList<String>,heightUnit:String,
    heightFeet:Int,heightInches:Int,interest:ArrayList<String>,lastName:String,weightUnit:String,weight:Int,injury:ArrayList<String>,has_injury:Boolean,level:ArrayList<String>
                          ) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getEditUserDataList(
                    auth,
                    EditUserRequest("editUser",dob=dob, username = username, equipment = eqitpment, first_name = firstName, gender = gender, goal = goal, heightUnit = heightUnit, heightValueFeet = heightFeet, heightValueInches = heightInches, interest = interest
                    , last_name = lastName, onbordingStatus = true, unit = weightUnit, weight = weight, has_injury = has_injury, injury = injury, level = level
                    )
                )
            }
        }
    }


    private fun setImage(auth:String, fileName: MultipartBody.Part,action: RequestBody) {
        val response = repository.setImage(auth,fileName,action)
        response.enqueue(object : Callback<SetImageResponse> {
            override fun onResponse(
                call: Call<SetImageResponse>,
                response: Response<SetImageResponse>
            ) {
                Log.d("theeris","the error is $response")
                if(response.code() == 200) {
                    setImageData.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage3.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<SetImageResponse>, t: Throwable) {
                errorMessage3.postValue(t.message)
            }
        })
    }

    fun checkSetImage(auth:String, fileName: MultipartBody.Part,action:RequestBody) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                setImage(
                    auth,fileName,
                    action
                )
            }
        }
    }



    val editUserForEquipmenttResponse = MutableLiveData<GetCustomerResponse>()
    val errorMessage4 = MutableLiveData<String>()

    private fun editUserForEquipmentt(auth: String,editUserEquipmentRequest: EditUserEquipmentRequest) {
        val response = repository.editUserForEquipment(auth,editUserEquipmentRequest)
        response.enqueue(object : Callback<GetCustomerResponse> {
            override fun onResponse(
                call: Call<GetCustomerResponse>,
                response: Response<GetCustomerResponse>
            ) {
                if(response.code() == 200) {
                    editUserForEquipmenttResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage4.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetCustomerResponse>, t: Throwable) {
                errorMessage4.postValue(t.message)
            }
        })
    }

    fun checkEditUserForEquipment(auth: String,editUserEquipmentRequest: EditUserEquipmentRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                editUserForEquipmentt(
                    auth ,
                    editUserEquipmentRequest

                )
            }
        }
    }

}