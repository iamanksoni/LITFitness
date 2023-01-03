package com.litmethod.android.DataProcessing

import com.litmethod.android.BluetoothConnection.LitDeviceConstants
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import java.time.LocalDate
import java.time.Period
import java.util.*
import kotlin.math.roundToInt

object ProcessedData {
    private var heartRateList = mutableListOf<Int>()
    private var maximumWeight = 0
    private var currentAverageRate = 0
    private var heartRateCount = 0
    private var totalWeight = 0;
    private var averageCaloriesBurnt = 0.0;
    private var averageCaloriesCount = 0;

    fun calculateAverageHeartRate(currentHeartRate: Int): Int {
        currentAverageRate =
            (currentAverageRate * heartRateCount + currentHeartRate) / (heartRateCount + 1)
        heartRateCount += 1
        return currentHeartRate;
    }

    fun calculateMaximumWeight(leftWeight: Float, rightWeight: Float): Int {
        return -1
    }

    fun calculateTotalWeight(leftWeight: Float, rightWeight: Float): Int {
        totalWeight += Math.max(leftWeight, rightWeight).roundToInt()
        return totalWeight
    }

    fun findAverage(data: Int, count: Int) {

    }

    fun findTimeUnderTension() {

    }

    fun findRepsCalculated() {

    }

    fun calculateResistance() {

    }

    fun calculateCaloriesBurnt(timeInHours: Double): Double {
        var caloriesBurnt: Double = 0.0;
        var gender = BaseResponseDataObject.profilePageData.gender.uppercase(Locale.getDefault())
        var weight: Double = BaseResponseDataObject.profilePageData.weight.toDouble()
        var weightUnit =
            BaseResponseDataObject.profilePageData.weightUnit.uppercase(Locale.getDefault())
        var dob = BaseResponseDataObject.profilePageData.dob
        var weightInLbs: Double = if (weightUnit == "KGS") {
            weight * 2.20462
        } else {
            weight
        }
        var date = LocalDate.parse(dob)
        var currentAge =
            getAge(date.year, date.monthValue, date.dayOfMonth).toFloat()
        LitDeviceConstants.HR_CONNECTION_STATE = "CONNECTED"
        if (LitDeviceConstants.HR_CONNECTION_STATE == "CONNECTED") {

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

        if (LitDeviceConstants.HR_CONNECTION_STATE == "DISCONNECTED") {
            caloriesBurnt = calculateCaloriesWithoutHR(weightInLbs, currentAge, timeInHours)
        }
        averageCaloriesBurnt = (averageCaloriesCount * averageCaloriesCount + caloriesBurnt)/(averageCaloriesCount+1)
        return caloriesBurnt;

    }

    fun calculateOtherGenderCalories(weightInLbs: Double, age: Float, timeInHours: Double): Double {
        var expression1 = (0.1988 * weightInLbs) + (0.2017 * age)
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

    fun getAge(year: Int, month: Int, dayOfMonth: Int): Int {
        return Period.between(
            LocalDate.of(year, month, dayOfMonth),
            LocalDate.now()
        ).years
    }
}