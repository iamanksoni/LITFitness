package com.litmethod.android.network

import com.litmethod.android.models.AcountScreenFragment.BookmarkClass.BookmarkClassRequest
import com.litmethod.android.models.AcountScreenFragment.ClassBookmark.ClassBookmarkRequest
import com.litmethod.android.models.AcountScreenFragment.EditUserLevel.EditUserLevelRequest
import com.litmethod.android.models.AcountScreenFragment.GetAchievementsdayStreak.GetAchievementsdayStreakRequest
import com.litmethod.android.models.AcountScreenFragment.GetAchievementsweeklyStreak.GetAchievementsweeklyStreakRequest
import com.litmethod.android.models.AcountScreenFragment.GetAcieveMentClass.GetAchieveMentRequest
import com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack.GetCalenderRequest
import com.litmethod.android.models.AcountScreenFragment.GetCalories.GetCaloriesRequest
import com.litmethod.android.models.AcountScreenFragment.GetClassStatistics.GetClassStatisticsRequest
import com.litmethod.android.models.AcountScreenFragment.GetLbs.GetLbsRequest
import com.litmethod.android.models.InjuryRequest

class AcountScreenFragmentRepository  constructor(private val retrofitService: RetrofitDataSourceService) {
    fun getbookmarkClassRequest(auth:String,bookmarkClassRequest: BookmarkClassRequest) = retrofitService.getBookmarkClass(auth,bookmarkClassRequest)
    fun getLevel(auth:String,injuryRequest: InjuryRequest) = retrofitService.getLevel(auth,injuryRequest)
    fun getClassStatisticsRequest(auth:String,getClassStatisticsRequest: GetClassStatisticsRequest) = retrofitService.getClassStatitstics(auth,getClassStatisticsRequest)
    fun getAchievementsClass(auth:String,getAchieveMentRequest: GetAchieveMentRequest) = retrofitService.getAchievementsClass(auth,getAchieveMentRequest)
    fun getAchievementsdayStreak(auth:String,getAchievementsdayStreakRequest: GetAchievementsdayStreakRequest) = retrofitService.getAchievementsdayStreak(auth,getAchievementsdayStreakRequest)
    fun getAchievementsweeklyStreak(auth:String,getAchievementsweeklyStreakRequest: GetAchievementsweeklyStreakRequest) = retrofitService.getAchievementsweeklyStreak(auth,getAchievementsweeklyStreakRequest)
    fun getCalories(auth:String,getCaloriesRequest: GetCaloriesRequest) = retrofitService.getCalories(auth,getCaloriesRequest)
    fun getLbs(auth:String,getLbsRequest: GetLbsRequest) = retrofitService.getLbs(auth,getLbsRequest)
    fun editUserForLevel(auth:String,editUserLevelRequest: EditUserLevelRequest) = retrofitService.editUserForLevel(auth,editUserLevelRequest)
    fun getClassBookmark(auth:String,classBookmarkRequest: ClassBookmarkRequest) = retrofitService.getClassBookmark(auth,classBookmarkRequest)
    fun getCalanderTrack(auth:String,getCalanderRequest: GetCalenderRequest) = retrofitService.getCalenderTrack(auth,getCalanderRequest)

}