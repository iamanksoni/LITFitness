package com.litmethod.android.utlis

import okhttp3.MultipartBody
import okhttp3.RequestBody

object UiDataObject {

  var firstName =""
  var lastName =""
  var etDob=""
  var gender =""
  var HightFt=0
  var HightIn=0
  var unitWeight=""
  var unitHeight=""
  var Weight=0
  var injuryLevel: ArrayList<String> = ArrayList<String>()
  var has_injury:Boolean = false
  var goalLevel: ArrayList<String> = ArrayList<String>()
  var interestData: ArrayList<String> = ArrayList<String>()
  val eqipLevel: ArrayList<String> = ArrayList<String>()
  var level: ArrayList<String> = ArrayList<String>()
  lateinit var  body: MultipartBody.Part
  lateinit var action:RequestBody
  var username =""
}