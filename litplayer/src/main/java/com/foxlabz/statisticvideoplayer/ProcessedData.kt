package com.foxlabz.statisticvideoplayer

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.Period
import java.util.*

object ProcessedData {
    private var currentAverageRate = 0
    private var heartRateCount = 0
    private var averageCaloriesBurnt = 0.0;
    private var averageCaloriesCount = 0;
    private var totalCaloriesBurnt = 0.0;
    private var timeUnderTension = 0
    private var heartRateList = mutableListOf<Pair<Int, Long>>()
    private var caloriesValuePair = CaloriesPair(0.0, 0.0)


    fun calculateAverageHeartRate(currentHeartRate: Int): Int {
        heartRateList.add(Pair(currentHeartRate, System.currentTimeMillis()))
        currentAverageRate =
            (currentAverageRate * heartRateCount + currentHeartRate) / (heartRateCount + 1)
        heartRateCount += 1
        return currentHeartRate;
    }


    fun findAverage(data: Int, count: Int) {

    }

    fun calculateResistance() {

    }

    /**
     * Just pass in the current time stamp for how long the value has alreadt olayed
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateCaloriesBurnt(timeInHours: Double): CaloriesPair {
        var caloriesBurnt: Double = 0.0;
        var gender = LitVideoPlayerSDK.gender.uppercase(Locale.getDefault())
        var weight: Double = LitVideoPlayerSDK.weight.toDouble()
        var weightUnit =
            LitVideoPlayerSDK.weightUnit.uppercase(Locale.getDefault())
        var dob = LitVideoPlayerSDK.dob
        var weightInLbs: Double = if (weightUnit == "KGS") {
            weight
        } else {
            weight / 2.20462
        }
        var date = LocalDate.parse(dob)
        var currentAge =
            getAge(date.year, date.monthValue, date.dayOfMonth).toFloat()
        if (LitVideoPlayerSDK.HR_CONNECTION_STATE == "CONNECTED") {

            when (gender) {
                "MALE" -> {
                    caloriesBurnt = calculateMaleCalories(weightInLbs, currentAge, timeInHours)
                }
                "FEMALE" -> {

                    caloriesBurnt = calculateFemaleCalories(weightInLbs, currentAge, timeInHours)
                }
                "OTHER" -> {

                    caloriesBurnt =
                        calculateOtherGenderCalories(weightInLbs, currentAge, timeInHours)
                }
            }
        }

        if (LitVideoPlayerSDK.HR_CONNECTION_STATE == "DISCONNECTED") {
            caloriesBurnt = calculateCaloriesWithoutHR(weightInLbs, currentAge, timeInHours)
        }
        averageCaloriesBurnt =
            (averageCaloriesCount * averageCaloriesCount + caloriesBurnt) / (averageCaloriesCount + 1)
        totalCaloriesBurnt += caloriesBurnt
        caloriesValuePair.first = averageCaloriesBurnt
        caloriesValuePair.second = totalCaloriesBurnt

        return caloriesValuePair;

    }

    fun calculateOtherGenderCalories(weightInLbs: Double, age: Float, timeInHours: Double): Double {
        var expression1 = (0.1988 * weightInLbs) + (0.2017 * age)  //21.34
        var caloriesBurned =
            ((-20.4022 + (0.4472 * currentAverageRate) - expression1) / 4.184) * 60 * timeInHours
        return caloriesBurned;
    }

    fun calculateMaleCalories(weightInLbs: Double, age: Float, timeInHours: Double): Double {
        var expression1 = (0.1988 * weightInLbs) + (0.2017 * age)
        var caloriesBurned =
            ((-55.0969 + (0.6309 * currentAverageRate) + expression1) / 4.184) * 60 * timeInHours
        return caloriesBurned;
    }

    fun calculateFemaleCalories(weightInLbs: Double, age: Float, timeInHours: Double): Double {
        var expression1 = (0.1263 * weightInLbs) + (0.074 * age)
        var caloriesBurned =
            ((-20.4022 + (0.4472 * currentAverageRate) - expression1) / 4.184) * 60 * timeInHours
        return caloriesBurned;
    }


    fun calculateCaloriesWithoutHR(weightInLbs: Double, age: Float, timeInHours: Double): Double {
        var expression = (0.1988 * weightInLbs) + (0.2017 * age)
        var caloriesBurnedValue =
            ((-55.0969 + (0.6309 * 120) + expression) / 4.184) * 60 * timeInHours
        return caloriesBurnedValue;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAge(year: Int, month: Int, dayOfMonth: Int): Int {
        return Period.between(
            LocalDate.of(year, month, dayOfMonth),
            LocalDate.now()
        ).years
    }
}