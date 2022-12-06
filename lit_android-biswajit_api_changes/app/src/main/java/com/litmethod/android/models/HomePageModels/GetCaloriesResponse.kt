package com.litmethod.android.models.HomePageModels

data class GetCaloriesResponse(
    val result: ResultGetCalories,
    val serverResponse: ServerResponse
)

data class ResultGetCalories(
    val `data`: DataGetCalories
)

data class DataGetCalories(
    val caloriesachievements: ArrayList<Caloriesachievement>
)

data class Caloriesachievement(
    val isComplete: Boolean,
    val isNotify: Boolean,
    val key: String,
    val msg: String,
    val number: String
)