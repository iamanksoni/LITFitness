package com.litmethod.android.ui.Onboarding.MeasureScreen.Util

import android.util.Log

class UntiConvert {

    fun convertFeetToMeters(feetValue:String,inchesValue:String):String{
        var feetToMeters = feetValue.toInt() / 3.281
        var inchesToMeters = inchesValue.toDouble() / 39.37
        var heightInMeters = feetToMeters + inchesToMeters
        val finalheight =heightInMeters*100
        Log.d("height","height in meters is $finalheight and height in meter $heightInMeters")
     return finalheight.toString()
    }

    fun convertMetersToFeet(heightInCm:Double):FeetAndInches{
        val heightInMeter = heightInCm/100
        var heightInFeet = heightInMeter * 3.281
        val delim = "."

        val decimalPartInFeet = heightInFeet.toString().split(delim)
        val feet = decimalPartInFeet[0]
        var feetIndecimal = "0.${decimalPartInFeet[1]}"

        var decimalPartInInches = feetIndecimal.toDouble() * 12
        Log.d("height","the inches is ${decimalPartInFeet}  and fee $feet and decimal $feetIndecimal and inch $decimalPartInInches" )
        return FeetAndInches(feet,decimalPartInInches.toString())
    }

      fun converlbsTokg(lbsValueFloat:String):String{

     var kgsValue = lbsValueFloat.toDouble() / 2.204
      return kgsValue.toString()
     }

    fun convertkgTolbs(kgsValueFloat:String):String{

        var lbsValue = kgsValueFloat.toDouble() * 2.204
        return lbsValue.toString()
    }
}