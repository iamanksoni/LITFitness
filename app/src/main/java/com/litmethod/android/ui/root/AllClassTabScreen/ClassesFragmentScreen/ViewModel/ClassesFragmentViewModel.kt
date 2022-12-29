package com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.ClassDetails.ClassDetailsRequest
import com.litmethod.android.models.ClassDetails.ClassDetailsResponse
import com.litmethod.android.models.GetAllAccessCatagory.GetAllAccessCatagoryRequest
import com.litmethod.android.models.GetAllAccessCatagory.GetAllCatagoryResponse
import com.litmethod.android.models.GetAllAccessFilter.GetAllAccessFilterRequest
import com.litmethod.android.models.GetAllAccessFilter.GetAllAccessFilterResponse
import com.litmethod.android.models.GetCatagory.GetCatagoryRequest
import com.litmethod.android.models.GetCatagory.GetCatagoryResponse
import com.litmethod.android.models.GetClassCatagoryById.GetClassCatagoryByIDRequest
import com.litmethod.android.models.GetClassCatagoryById.GetClassCatagoryByIdResponse
import com.litmethod.android.models.GetProgram.GetProgramsRequest
import com.litmethod.android.models.GetProgram.GetProgramsResponse
import com.litmethod.android.models.GetProgramById.GetProgramByIdRequest
import com.litmethod.android.models.GetProgramById.GetProgramByIdResponse
import com.litmethod.android.network.ClassesFragmentRepository
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassesFragmentViewModel constructor(private val repository: ClassesFragmentRepository, var context: Context) :
    ViewModel()  {
    val getCatagoryListResponse = MutableLiveData<GetCatagoryResponse>()
    val errorMessage = MutableLiveData<String>()

    val getClassCatagoryByIdResponse = MutableLiveData<GetClassCatagoryByIdResponse>()
    val getClassCatagoryByIdResponseFilter = MutableLiveData<GetClassCatagoryByIdResponse>()

    val errorMessage2 = MutableLiveData<String>()

    val getProgramsResponseList = MutableLiveData<GetProgramsResponse>()
    val errorMessage3 = MutableLiveData<String>()

    val getAllAccessCatagoryResponse = MutableLiveData<GetAllCatagoryResponse>()
    val errorMessage4 = MutableLiveData<String>()

    val getProgramByIdResponse = MutableLiveData<GetProgramByIdResponse>()
    val errorMessage5 = MutableLiveData<String>()

    val classDetailsResponse = MutableLiveData<ClassDetailsResponse>()
    val errorMessage6 = MutableLiveData<String>()

    val getAllAccessFilterResponse = MutableLiveData<GetAllAccessFilterResponse>()
    val errorMessage7 = MutableLiveData<String>()

    private fun getCatagoryListData(auth: String,getCatagoryRequest:GetCatagoryRequest) {
        val response = repository.getCatagory(auth,getCatagoryRequest)
        response.enqueue(object : Callback<GetCatagoryResponse> {
            override fun onResponse(
                call: Call<GetCatagoryResponse>,
                response: Response<GetCatagoryResponse>
            ) {
                if(response.code() == 200) {
                    getCatagoryListResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetCatagoryResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun checkgetInjury(auth: String) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getCatagoryListData(
                    auth ,
                    GetCatagoryRequest(AppConstants.actionTypegetCatagoryLIst)

                )
            }
        }
    }

    private fun getClassCategoryById(auth: String,getClassCatagoryByIDRequest: GetClassCatagoryByIDRequest) {
        val response = repository.getClassCategoryById(auth,getClassCatagoryByIDRequest)
        response.enqueue(object : Callback<GetClassCatagoryByIdResponse> {
            override fun onResponse(
                call: Call<GetClassCatagoryByIdResponse>,
                response: Response<GetClassCatagoryByIdResponse>
            ) {
                if(response.code() == 200) {
                    Log.d("idResponse","response body ${response.body()}")
         if (BaseResponseDataObject.onResumeViewModel == true){
//             BaseResponseDataObject.onResumeViewModel = false
             getClassCatagoryByIdResponseFilter.postValue(response.body())

         } else{
             Log.d("checkData","main response called")
             getClassCatagoryByIdResponse.postValue(response.body())
         }




                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
//                    Log.d("checkData","main response error ${response.errorBody()!!.string()}")
                    errorMessage2.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetClassCatagoryByIdResponse>, t: Throwable) {
                errorMessage2.postValue(t.message)
                Log.d("checkData","main response error ${  errorMessage2.postValue(t.message)}")
            }
        })
    }

    fun checkGetClassCatagoryById(auth: String,getClassCatagoryByIDRequest: GetClassCatagoryByIDRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                Log.d("idResponse","the data req $auth and $getClassCatagoryByIDRequest")
                getClassCategoryById(
                    auth ,
                    getClassCatagoryByIDRequest

                )
            }
        }
    }


    private fun getProgramsList(auth: String,getProgramsRequest: GetProgramsRequest) {
        val response = repository.getPrograms(auth,getProgramsRequest)
        response.enqueue(object : Callback<GetProgramsResponse> {
            override fun onResponse(
                call: Call<GetProgramsResponse>,
                response: Response<GetProgramsResponse>
            ) {
                if(response.code() == 200) {
                    getProgramsResponseList.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage3.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetProgramsResponse>, t: Throwable) {
                errorMessage3.postValue(t.message)
            }
        })
    }

    fun checkgetProgramsList(auth: String,getProgramsRequest: GetProgramsRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getProgramsList(
                    auth ,
                    getProgramsRequest

                )
            }
        }
    }

    private fun getAllAccessCatagory(auth: String,getAllAccessCatagoryRequest: GetAllAccessCatagoryRequest) {
        val response = repository.getAllAccessCatagory(auth,getAllAccessCatagoryRequest)
        response.enqueue(object : Callback<GetAllCatagoryResponse> {
            override fun onResponse(
                call: Call<GetAllCatagoryResponse>,
                response: Response<GetAllCatagoryResponse>
            ) {
                if(response.code() == 200) {
                    getAllAccessCatagoryResponse.postValue(response.body())
                }else  if(response.code() == 600) {
                     Log.d("getData25","error")
                    errorMessage4.postValue("You don't have permisson to see the class pleace subcribe")
                }
            }
            override fun onFailure(call: Call<GetAllCatagoryResponse>, t: Throwable) {
                errorMessage4.postValue(t.message)
            }
        })
    }

    fun checkGetAllAccessCatagory(auth: String,getAllAccessCatagoryRequest: GetAllAccessCatagoryRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getAllAccessCatagory(
                    auth ,
                    getAllAccessCatagoryRequest

                )
            }
        }
    }

    private fun getProgramsById(auth: String,getClassCatagoryByIDRequest: GetProgramByIdRequest) {
        val response = repository.getProgramsById(auth,getClassCatagoryByIDRequest)
        response.enqueue(object : Callback<GetProgramByIdResponse> {
            override fun onResponse(
                call: Call<GetProgramByIdResponse>,
                response: Response<GetProgramByIdResponse>
            ) {
                if(response.code() == 200) {
                    getProgramByIdResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage5.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetProgramByIdResponse>, t: Throwable) {
                errorMessage5.postValue(t.message)
            }
        })
    }

    fun checkgetProgramsById(auth: String,getProgramByIdRequest: GetProgramByIdRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getProgramsById(
                    auth ,
                    getProgramByIdRequest

                )
            }
        }
    }

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

    private fun getAllAccessFilter(auth: String,getAllAccessFilterRequest: GetAllAccessFilterRequest) {
        val response = repository.getAllAccessFilter(auth,getAllAccessFilterRequest)
        response.enqueue(object : Callback<GetAllAccessFilterResponse> {
            override fun onResponse(
                call: Call<GetAllAccessFilterResponse>,
                response: Response<GetAllAccessFilterResponse>
            ) {
                if(response.code() == 200) {
                    getAllAccessFilterResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage7.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetAllAccessFilterResponse>, t: Throwable) {
                errorMessage7.postValue(t.message)
            }
        })
    }

    fun checkgetAllAccessFilter(auth: String,getAllAccessFilterRequest: GetAllAccessFilterRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getAllAccessFilter(
                    auth ,
                    getAllAccessFilterRequest

                )
            }
        }
    }
}