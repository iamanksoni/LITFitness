package com.litmethod.android.network

import com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack.GetCalanderRequest
import com.litmethod.android.models.ClassDetails.ClassDetailsRequest
import com.litmethod.android.models.GetProgramById.GetProgramByIdRequest
import com.litmethod.android.models.HomePageModels.GetUserAnalycticsDetilesRequest
import com.litmethod.android.models.HomePageModels.UserAnalyticsRequest
import com.litmethod.android.models.InjuryRequest

class HomeTabFragmentRepository constructor(private val retrofitService: RetrofitService) {
    fun getHome(auth: String, injuryRequest: InjuryRequest) =
        retrofitService.getHome(auth, injuryRequest)

    fun getUserAnalytics(auth: String, userAnalyticsRequest: UserAnalyticsRequest) =
        retrofitService.getUserAnalytics(auth, userAnalyticsRequest)

    fun getAchievementsClass(
        auth: String, injuryRequest: InjuryRequest
    ) = retrofitService.getAchievementsClass(auth, injuryRequest)

    fun getAchievementsdayStreak(
        auth: String, injuryRequest: InjuryRequest
    ) = retrofitService.getAchievementsdayStreak(auth, injuryRequest)

    fun getAchievementsweeklyStreak(
        auth: String, injuryRequest: InjuryRequest
    ) = retrofitService.getAchievementsweeklyStreak(auth, injuryRequest)

    fun getCalories(
        auth: String, injuryRequest: InjuryRequest
    ) = retrofitService.getCalories(auth, injuryRequest)

    fun getLbs(
        auth: String, injuryRequest: InjuryRequest
    ) = retrofitService.getLbs(auth, injuryRequest)

    fun getUserAnalycticsDetiles(
        auth: String,
        getUserAnalycticsDetilesRequest: GetUserAnalycticsDetilesRequest
    ) = retrofitService.getUserAnalycticsDetiles(auth, getUserAnalycticsDetilesRequest)

    fun getCalanderTrack(auth:String,getCalanderRequest: GetCalanderRequest) =
        retrofitService.getCalanderTrack(auth,getCalanderRequest)

    fun getProgramsById(auth:String,getProgramByIdRequest: GetProgramByIdRequest) = retrofitService.getProgramsById(auth,getProgramByIdRequest)

    fun getClassDetails(auth:String,glassDetailsRequest: ClassDetailsRequest) = retrofitService.getClassDetails(auth,glassDetailsRequest)
}