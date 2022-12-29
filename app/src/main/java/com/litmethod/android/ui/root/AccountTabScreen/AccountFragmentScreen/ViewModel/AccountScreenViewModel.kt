package com.litmethod.android.ui.root.AccountTabScreen.AccountFragmentScreen.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.AcountScreenFragment.BookmarkClass.BookmarkClassRequest
import com.litmethod.android.models.AcountScreenFragment.BookmarkClass.BookmarkClassResponse
import com.litmethod.android.models.AcountScreenFragment.ClassBookmark.ClassBookmarkRequest
import com.litmethod.android.models.AcountScreenFragment.ClassBookmark.ClassBookmarkResponse
import com.litmethod.android.models.AcountScreenFragment.EditUserLevel.EditUserLevelRequest
import com.litmethod.android.models.AcountScreenFragment.GetAchievementsdayStreak.GetAchievementsdayStreakRequest
import com.litmethod.android.models.AcountScreenFragment.GetAchievementsdayStreak.GetAchievementsdayStreakResponse
import com.litmethod.android.models.AcountScreenFragment.GetAchievementsweeklyStreak.GetAchievementsweeklyStreakRequest
import com.litmethod.android.models.AcountScreenFragment.GetAchievementsweeklyStreak.GetAchievementsweeklyStreakResponse
import com.litmethod.android.models.AcountScreenFragment.GetAcieveMentClass.GetAchieveMentRequest
import com.litmethod.android.models.AcountScreenFragment.GetAcieveMentClass.GetAchievementResponse
import com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack.GetCalenderRequest
import com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack.GetCalanderResponse
import com.litmethod.android.models.AcountScreenFragment.GetCalories.GetCaloriesRequest
import com.litmethod.android.models.AcountScreenFragment.GetCalories.GetCaloriesResponse
import com.litmethod.android.models.AcountScreenFragment.GetClassStatistics.GetClassStatisticsRequest
import com.litmethod.android.models.AcountScreenFragment.GetClassStatistics.GetClassStatisticsResponse
import com.litmethod.android.models.AcountScreenFragment.GetLbs.GetLbsRequest
import com.litmethod.android.models.AcountScreenFragment.GetLbs.GetLbsResponse
import com.litmethod.android.models.GetCustomers.GetCustomerResponse
import com.litmethod.android.models.GetLevel.GetLevelResponse
import com.litmethod.android.models.InjuryRequest
import com.litmethod.android.network.AcountScreenFragmentRepository
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountScreenViewModel  constructor(private val repository: AcountScreenFragmentRepository, var context: Context) :
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

    fun checkgetLevel(auth: String) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getLevelDataList(
                    auth,
                    InjuryRequest("getLevel")
                )
            }
        }
    }


    val getBookmarkClassResponse = MutableLiveData<BookmarkClassResponse>()
    val errorMessage2 = MutableLiveData<String>()

    private fun getBookmarkClass(auth: String,bookmarkClassRequest: BookmarkClassRequest) {
        val response = repository.getbookmarkClassRequest(auth,bookmarkClassRequest)
        response.enqueue(object : Callback<BookmarkClassResponse> {
            override fun onResponse(
                call: Call<BookmarkClassResponse>,
                response: Response<BookmarkClassResponse>
            ) {
                if(response.code() == 200) {
                    getBookmarkClassResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage2.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<BookmarkClassResponse>, t: Throwable) {
                errorMessage2.postValue(t.message)
            }
        })
    }

    fun checkgetBookmarkClass(auth: String,bookmarkClassRequest: BookmarkClassRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getBookmarkClass(
                    auth ,
                    bookmarkClassRequest

                )
            }
        }
    }

    val getClassStatisticsResponse = MutableLiveData<GetClassStatisticsResponse>()
    val errorMessage3 = MutableLiveData<String>()

    private fun getClassStatistics(auth: String,getClassStatisticsRequest: GetClassStatisticsRequest) {
        val response = repository.getClassStatisticsRequest(auth,getClassStatisticsRequest)
        response.enqueue(object : Callback<GetClassStatisticsResponse> {
            override fun onResponse(
                call: Call<GetClassStatisticsResponse>,
                response: Response<GetClassStatisticsResponse>
            ) {
                if(response.code() == 200) {
                    getClassStatisticsResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage3.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetClassStatisticsResponse>, t: Throwable) {
                errorMessage3.postValue(t.message)
                Log.d("GetClassStatisticsResponse","the error ${t.message}")
            }
        })
    }

    fun checkgetClassStatistics(auth: String,getClassStatisticsRequest: GetClassStatisticsRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getClassStatistics(
                    auth ,
                    getClassStatisticsRequest

                )
            }
        }
    }


    val getAchievementResponse = MutableLiveData<GetAchievementResponse>()
    val errorMessage4 = MutableLiveData<String>()

    private fun getAchievementsClass(auth: String,getAchieveMentRequest: GetAchieveMentRequest) {
        val response = repository.getAchievementsClass(auth,getAchieveMentRequest)
        response.enqueue(object : Callback<GetAchievementResponse> {
            override fun onResponse(
                call: Call<GetAchievementResponse>,
                response: Response<GetAchievementResponse>
            ) {
                if(response.code() == 200) {
                    getAchievementResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage4.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }


            override fun onFailure(call: Call<GetAchievementResponse>, t: Throwable) {
                errorMessage4.postValue(t.message)
            }


        })
    }

    fun checkgetAchievementsClass(auth: String,getAchieveMentRequest: GetAchieveMentRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getAchievementsClass(
                    auth ,
                    getAchieveMentRequest

                )
            }
        }
    }

    val getAchievementsdayStreakResponse = MutableLiveData<GetAchievementsdayStreakResponse>()
    val errorMessage5 = MutableLiveData<String>()

    private fun getAchievementsdayStreak(auth: String,getAchievementsdayStreakRequest: GetAchievementsdayStreakRequest) {
        val response = repository.getAchievementsdayStreak(auth,getAchievementsdayStreakRequest)
        response.enqueue(object : Callback<GetAchievementsdayStreakResponse> {
            override fun onResponse(
                call: Call<GetAchievementsdayStreakResponse>,
                response: Response<GetAchievementsdayStreakResponse>
            ) {
                if(response.code() == 200) {
                    getAchievementsdayStreakResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage5.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }

            override fun onFailure(call: Call<GetAchievementsdayStreakResponse>, t: Throwable) {
                errorMessage5.postValue(t.message)
            }


        })
    }

    fun checkgetAchievementsdayStreak(auth: String,getAchievementsdayStreakRequest: GetAchievementsdayStreakRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getAchievementsdayStreak(
                    auth ,
                    getAchievementsdayStreakRequest

                )
            }
        }
    }

    val getAchievementsweeklyStreakResponse = MutableLiveData<GetAchievementsweeklyStreakResponse>()
    val errorMessage6 = MutableLiveData<String>()

    private fun getAchievementsweeklyStreak(auth: String,getAchievementsweeklyStreakRequest: GetAchievementsweeklyStreakRequest) {
        val response = repository.getAchievementsweeklyStreak(auth,getAchievementsweeklyStreakRequest)
        response.enqueue(object : Callback<GetAchievementsweeklyStreakResponse> {
            override fun onResponse(
                call: Call<GetAchievementsweeklyStreakResponse>,
                response: Response<GetAchievementsweeklyStreakResponse>
            ) {
                if(response.code() == 200) {
                    getAchievementsweeklyStreakResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage6.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }

            override fun onFailure(call: Call<GetAchievementsweeklyStreakResponse>, t: Throwable) {
                errorMessage6.postValue(t.message)
            }


        })
    }

    fun checkgetAchievementsweeklyStreak(auth: String,getAchievementsweeklyStreakRequest: GetAchievementsweeklyStreakRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getAchievementsweeklyStreak(
                    auth ,
                    getAchievementsweeklyStreakRequest

                )
            }
        }
    }

    val getCaloriesResponse = MutableLiveData<GetCaloriesResponse>()
    val errorMessage7 = MutableLiveData<String>()

    private fun getCalories(auth: String,getCaloriesRequest: GetCaloriesRequest) {
        val response = repository.getCalories(auth,getCaloriesRequest)
        response.enqueue(object : Callback<GetCaloriesResponse> {
            override fun onResponse(
                call: Call<GetCaloriesResponse>,
                response: Response<GetCaloriesResponse>
            ) {
                if(response.code() == 200) {
                    getCaloriesResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage7.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }

            override fun onFailure(call: Call<GetCaloriesResponse>, t: Throwable) {
                errorMessage7.postValue(t.message)
            }


        })
    }

    fun checkgetCalories(auth: String,getCaloriesRequest: GetCaloriesRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getCalories(
                    auth ,
                    getCaloriesRequest

                )
            }
        }
    }

    val GetLbsResponse = MutableLiveData<GetLbsResponse>()
    val errorMessage8 = MutableLiveData<String>()

    private fun getLbs(auth: String,getLbsRequest: GetLbsRequest) {
        val response = repository.getLbs(auth,getLbsRequest)
        response.enqueue(object : Callback<GetLbsResponse> {
            override fun onResponse(
                call: Call<GetLbsResponse>,
                response: Response<GetLbsResponse>
            ) {
                if(response.code() == 200) {
                    GetLbsResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage8.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }

            override fun onFailure(call: Call<GetLbsResponse>, t: Throwable) {
                errorMessage8.postValue(t.message)
            }


        })
    }

    fun checkgetLbs(auth: String,getLbsRequest: GetLbsRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getLbs(
                    auth ,
                    getLbsRequest

                )
            }
        }
    }

    val getCustomerResponse = MutableLiveData<GetCustomerResponse>()
    val errorMessage9 = MutableLiveData<String>()

    private fun editUserForLevel(auth: String,editUserLevelRequest: EditUserLevelRequest) {
        val response = repository.editUserForLevel(auth,editUserLevelRequest)
        response.enqueue(object : Callback<GetCustomerResponse> {
            override fun onResponse(
                call: Call<GetCustomerResponse>,
                response: Response<GetCustomerResponse>
            ) {
                if(response.code() == 200) {
                    getCustomerResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage9.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }

            override fun onFailure(call: Call<GetCustomerResponse>, t: Throwable) {
                errorMessage9.postValue(t.message)
            }


        })
    }

    fun checkeditUserForLevel(auth: String,editUserLevelRequest: EditUserLevelRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                editUserForLevel(
                    auth ,
                    editUserLevelRequest

                )
            }
        }
    }

    val classBookmarkResponse = MutableLiveData<ClassBookmarkResponse>()
    val errorMessage10 = MutableLiveData<String>()

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
                    errorMessage10.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<ClassBookmarkResponse>, t: Throwable) {
                errorMessage10.postValue(t.message)
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

    val getCalanderResponse = MutableLiveData<GetCalanderResponse>()
    val errorMessage11 = MutableLiveData<String>()
    private fun getCalanderTrack(auth: String,getCalanderRequest: GetCalenderRequest) {
        val response = repository.getCalanderTrack(auth,getCalanderRequest)
        response.enqueue(object : Callback<GetCalanderResponse> {
            override fun onResponse(
                call: Call<GetCalanderResponse>,
                response: Response<GetCalanderResponse>
            ) {
                Log.d("calagedbdn","calder error ${response.body()}")
                if(response.code() == 200) {
                    getCalanderResponse.postValue(response.body())
                }else  if(response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage11.postValue(jObjError.getJSONObject("serverResponse").getString("message"))
                }
            }
            override fun onFailure(call: Call<GetCalanderResponse>, t: Throwable) {
                errorMessage11.postValue(t.message)
                Log.d("calagedbdn","calder error ${t.message}")
            }
        })
    }

    fun checkgetCalanderTrack(auth: String,getCalanderRequest: GetCalenderRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getCalanderTrack(
                    auth ,
                    getCalanderRequest

                )
            }
        }
    }

}