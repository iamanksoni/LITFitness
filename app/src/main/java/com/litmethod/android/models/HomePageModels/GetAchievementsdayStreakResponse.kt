package com.litmethod.android.models.HomePageModels

data class GetAchievementsdayStreakResponse(
    val result: ResultGetAchievementsdayStreak,
    val serverResponse: ServerResponse
)

data class ResultGetAchievementsdayStreak(
    val `data`: DataResultGetAchievementsdayStreak
)

data class DataResultGetAchievementsdayStreak(
    val dayStreak: ArrayList<DayStreak>
)

data class DayStreak(
    val isComplete: Boolean,
    val isNotify: Boolean,
    val key: String,
    val msg: String,
    val number: String
)