package com.litmethod.android.ui.root.AllClassTabScreen.EditProfile.ViewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.EditUserRequestNullable.EditUserRequestNullable
import com.litmethod.android.models.GetCustomers.GetCustomerResponse
import com.litmethod.android.models.SetImageResponse.SetImageResponse
import com.litmethod.android.network.EditProfileRepository
import com.litmethod.android.network.ServiceBuilder
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel  constructor(private val repository: EditProfileRepository, var context: Context) :
    ViewModel()  {

    val editUserRequestNullableResponse= MutableLiveData<GetCustomerResponse>()
    val errorMessage3 = MutableLiveData<String>()




    private fun editUserRequestNullable(auth: String,editUserRequestNullable: EditUserRequestNullable) {
        val response = repository.editUserRequestNullable(auth,editUserRequestNullable)
        response.enqueue(object : Callback<GetCustomerResponse> {
            override fun onResponse(
                call: Call<GetCustomerResponse>,
                response: Response<GetCustomerResponse>
            ) {
                if(response.code() == 200) {
                    editUserRequestNullableResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage3.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetCustomerResponse>, t: Throwable) {
                errorMessage3.postValue(t.message)
                Log.d("the error is ","the error ${t.message}")
            }
        })
    }

    fun checkeeditUserRequestNullable(auth: String,editUserRequestNullable: EditUserRequestNullable) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                editUserRequestNullable(
                    auth ,
                    editUserRequestNullable

                )
            }
        }
    }

    val setImageData = MutableLiveData<SetImageResponse>()
    val errorMessage4 = MutableLiveData<String>()


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
                    errorMessage3.postValue(
                        jObjError.getJSONObject("serverResponse").getString("message")
                    )
                }
            }

            override fun onFailure(call: Call<SetImageResponse>, t: Throwable) {
                errorMessage3.postValue(t.message)
                Log.d("theimageerroe", "the image error is ${t.message}")
            }
        })
    }

     fun setImage2(body: MultipartBody.Part) {
        val serviceBuilder = ServiceBuilder.myApi
        serviceBuilder.uploadImage(body).enqueue(object : retrofit2.Callback<SetImageResponse> {

            override fun onFailure(call: Call<SetImageResponse>, t: Throwable) {
                Toast.makeText(context, "Failed due ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(
                call: Call<SetImageResponse>,
                response: Response<SetImageResponse>
            ) {
                val responseCode = response.code()
                if (responseCode == 200) {
                    Toast.makeText(context, "Uploaded !", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

    fun checkSetImage(auth: String, fileName: MultipartBody.Part, action: RequestBody) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                setImage2(
                    fileName
                )
            }
        }
    }



}