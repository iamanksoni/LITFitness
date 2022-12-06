package com.litmethod.android.ui.Dashboard.LiveClassTabScreen.TimeUtil

import android.util.Log

class SetThePostFixinDate {

    fun getTheLastNo( date:String):Char{
        val length = date.length
        val lastCharacter = date[length-1]
Log.d("theLastNois","the last no is $lastCharacter")
        return lastCharacter
    }

    fun setThePostFix(last:Char):String{
     val lastChar=  when(last){
            '1' -> "ST"
            '2' -> "ND"
            '3' -> "RD"
            else -> "TH"
        }
         return lastChar
    }


}