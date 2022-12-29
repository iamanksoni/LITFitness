package com.litmethod.android.models.HomePageModels

data class GetAchievementsweeklyStreakResponse(
    val result: ResultGetAchievementsweeklyStreak,
    val serverResponse: ServerResponse
)

data class ResultGetAchievementsweeklyStreak(
    val `data`: DataGetAchievementsweeklyStreak
)

data class DataGetAchievementsweeklyStreak(
    val weeklyStreak: ArrayList<WeeklyStreak>
)

data class WeeklyStreak(
    val isComplete: Boolean,
    val isNotify: Boolean,
    val key: String,
    val msg: String,
    val number: String
)