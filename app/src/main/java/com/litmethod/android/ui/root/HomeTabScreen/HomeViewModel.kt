package com.litmethod.android.ui.root.HomeTabScreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litmethod.android.models.*
import com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack.GetCalenderRequest
import com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack.GetCalanderResponse
import com.litmethod.android.models.ClassDetails.ClassDetailsRequest
import com.litmethod.android.models.ClassDetails.ClassDetailsResponse
import com.litmethod.android.models.GetProgramById.GetProgramByIdRequest
import com.litmethod.android.models.GetProgramById.GetProgramByIdResponse
import com.litmethod.android.models.HomePageModels.*
import com.litmethod.android.network.HomeTabFragmentRepository
import com.litmethod.android.utlis.DialogueBox
import com.litmethod.android.utlis.NetworkHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel constructor(
    private val repository: HomeTabFragmentRepository,
    var context: Context
) :
    ViewModel() {
    val getHomeData = MutableLiveData<ResultGetHome>()
    val errorMessage = MutableLiveData<String>()
    val getUserAnalyticsData = MutableLiveData<ArrayList<ResultUserAnalytics>>()
    val getAchievementsClassData = MutableLiveData<ResultGetAchievementsClass>()
    val getAchievementsdayStreakData = MutableLiveData<ResultGetAchievementsdayStreak>()
    val getAchievementsweeklyData = MutableLiveData<ResultGetAchievementsweeklyStreak>()
    val getCaloriesData = MutableLiveData<ResultGetCalories>()
    val getLbsData = MutableLiveData<ResultGetLbs>()
    val getUserAnalycticsDetilesData = MutableLiveData<ResultGetUserAnalycticsDetiles>()


    private fun getHomeDataList(auth: String, injuryRequest: InjuryRequest) {
        val response = repository.getHome(auth, injuryRequest)
        response.enqueue(object : Callback<GetHomeResponse> {
            override fun onResponse(
                call: Call<GetHomeResponse>,
                response: Response<GetHomeResponse>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.serverResponse.statusCode == 200) {
                        getHomeData.postValue(response.body()!!.result)
                    } else {
                        errorMessage.postValue("Sesison Timeout")
                    }
                } else if (response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(
                        jObjError.getJSONObject("serverResponse").getString("message")
                    )
                }
            }

            override fun onFailure(call: Call<GetHomeResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getHomeCheck(auth: String) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Home Page",
                    "Please check your internet connection."
                )
            else -> {
                getHomeDataList(
                    auth,
                    InjuryRequest("getHome")
                )
            }
        }
    }

    private fun getUserAnalyticsList(auth: String, userAnalyticsRequest: UserAnalyticsRequest) {
        val response = repository.getUserAnalytics(auth, userAnalyticsRequest)
        response.enqueue(object : Callback<UserAnalyticsResponse> {
            override fun onResponse(
                call: Call<UserAnalyticsResponse>,
                response: Response<UserAnalyticsResponse>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.serverResponse.statusCode == 200) {
                        getUserAnalyticsData.postValue(response.body()!!.result)
                    } else {
                        errorMessage.postValue("Sesison Timeout")
                    }
                } else if (response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(
                        jObjError.getJSONObject("serverResponse").getString("message")
                    )
                }
            }

            override fun onFailure(call: Call<UserAnalyticsResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }



    fun getUserAnalyticsCheck(auth: String, filter: String) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Home Page",
                    "Please check your internet connection."
                )
            else -> {
                getUserAnalyticsList(
                    auth,
                    UserAnalyticsRequest("useranalytics", filter)
                )
            }
        }
    }

    private fun getAchievementsList(auth: String) {

        val response1 = repository.getAchievementsClass(auth, InjuryRequest("getAchievementsClass"))
        response1.enqueue(object : Callback<GetAchievementsClassResponse> {
            override fun onResponse(
                call: Call<GetAchievementsClassResponse>,
                response: Response<GetAchievementsClassResponse>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.serverResponse.statusCode == 200) {
                        getAchievementsClassData.postValue(response.body()!!.result)
                    } else {
                        errorMessage.postValue("Sesison Timeout")
                    }
                } else if (response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(
                        jObjError.getJSONObject("serverResponse").getString("message")
                    )
                }
            }

            override fun onFailure(call: Call<GetAchievementsClassResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })


        val response2 =
            repository.getAchievementsdayStreak(auth, InjuryRequest("getAchievementsdayStreak"))
        response2.enqueue(object : Callback<GetAchievementsdayStreakResponse> {
            override fun onResponse(
                call: Call<GetAchievementsdayStreakResponse>,
                response: Response<GetAchievementsdayStreakResponse>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.serverResponse.statusCode == 200) {
                        getAchievementsdayStreakData.postValue(response.body()!!.result)
                    } else {
                        errorMessage.postValue("Sesison Timeout")
                    }
                } else if (response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(
                        jObjError.getJSONObject("serverResponse").getString("message")
                    )
                }
            }

            override fun onFailure(call: Call<GetAchievementsdayStreakResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })


        val response3 = repository.getAchievementsweeklyStreak(
            auth,
            InjuryRequest("getAchievementsweeklyStreak")
        )
        response3.enqueue(object : Callback<GetAchievementsweeklyStreakResponse> {
            override fun onResponse(
                call: Call<GetAchievementsweeklyStreakResponse>,
                response: Response<GetAchievementsweeklyStreakResponse>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.serverResponse.statusCode == 200) {
                        getAchievementsweeklyData.postValue(response.body()!!.result)
                    } else {
                        errorMessage.postValue("Sesison Timeout")
                    }
                } else if (response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(
                        jObjError.getJSONObject("serverResponse").getString("message")
                    )
                }
            }

            override fun onFailure(call: Call<GetAchievementsweeklyStreakResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })


        val response4 = repository.getCalories(auth, InjuryRequest("getCalories"))
        response4.enqueue(object : Callback<GetCaloriesResponse> {
            override fun onResponse(
                call: Call<GetCaloriesResponse>,
                response: Response<GetCaloriesResponse>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.serverResponse.statusCode == 200) {
                        getCaloriesData.postValue(response.body()!!.result)
                    } else {
                        errorMessage.postValue("Sesison Timeout")
                    }
                } else if (response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(
                        jObjError.getJSONObject("serverResponse").getString("message")
                    )
                }
            }

            override fun onFailure(call: Call<GetCaloriesResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })


        val response5 = repository.getLbs(auth, InjuryRequest("getLbs"))
        response5.enqueue(object : Callback<GetLbsResponse> {
            override fun onResponse(
                call: Call<GetLbsResponse>,
                response: Response<GetLbsResponse>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.serverResponse.statusCode == 200) {
                        getLbsData.postValue(response.body()!!.result)
                    } else {
                        errorMessage.postValue("Sesison Timeout")
                    }
                } else if (response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(
                        jObjError.getJSONObject("serverResponse").getString("message")
                    )
                }
            }

            override fun onFailure(call: Call<GetLbsResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })

    }

    fun getAchievementsCheck(auth: String) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Home Page",
                    "Please check your internet connection."
                )
            else -> {
                getAchievementsList(
                    auth
                )
            }
        }
    }

    private fun getUserAnalycticsDetiles(
        auth: String,
        getUserAnalycticsDetilesRequest: GetUserAnalycticsDetilesRequest
    ) {
        val response = repository.getUserAnalycticsDetiles(auth, getUserAnalycticsDetilesRequest)
        response.enqueue(object : Callback<GetUserAnalycticsDetilesResponse> {
            override fun onResponse(
                call: Call<GetUserAnalycticsDetilesResponse>,
                response: Response<GetUserAnalycticsDetilesResponse>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.serverResponse.statusCode == 200) {
                        getUserAnalycticsDetilesData.postValue(response.body()!!.result)
                    } else {
                        errorMessage.postValue("Sesison Timeout")
                    }
                } else if (response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage.postValue(
                        jObjError.getJSONObject("serverResponse").getString("message")
                    )
                }
            }

            override fun onFailure(call: Call<GetUserAnalycticsDetilesResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getUserAnalycticsDetilesCheck(
        auth: String,
        getUserAnalycticsDetilesRequest: GetUserAnalycticsDetilesRequest
    ) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Home Page",
                    "Please check your internet connection."
                )
            else -> {
                getUserAnalycticsDetiles(
                    auth,
                    getUserAnalycticsDetilesRequest
                )
            }
        }
    }

    val getCalanderResponse = MutableLiveData<GetCalanderResponse>()
    val errorMessage11 = MutableLiveData<String>()
    private fun getCalanderTrack(auth: String, getCalanderRequest: GetCalenderRequest) {
        val response = repository.getCalanderTrack(auth, getCalanderRequest)
        response.enqueue(object : Callback<GetCalanderResponse> {
            override fun onResponse(
                call: Call<GetCalanderResponse>,
                response: Response<GetCalanderResponse>
            ) {
                Log.d("calagedbdn", "calder error ${response.body()}")
                if (response.code() == 200) {
                    getCalanderResponse.postValue(response.body())
                } else if (response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage11.postValue(
                        jObjError.getJSONObject("serverResponse").getString("message")
                    )
                }
            }

            override fun onFailure(call: Call<GetCalanderResponse>, t: Throwable) {
                errorMessage11.postValue(t.message)
                Log.d("calagedbdn", "calder error ${t.message}")
            }
        })
    }

    fun checkgetCalanderTrack(auth: String, getCalanderRequest: GetCalenderRequest) {
        when {
            !NetworkHelper.instance.isNetworkAvailable(context) ->
                DialogueBox.showMsg(
                    context,
                    "Sign in failed!",
                    "Please check your internet connection."
                )
            else -> {
                getCalanderTrack(
                    auth,
                    getCalanderRequest

                )
            }
        }
    }

    val getProgramByIdResponse = MutableLiveData<GetProgramByIdResponse>()
    val errorMessage5 = MutableLiveData<String>()

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

    val classDetailsResponse = MutableLiveData<ClassDetailsResponse>()
    val errorMessage6 = MutableLiveData<String>()

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

}