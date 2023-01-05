package com.foxlabz.statisticvideoplayer

import androidx.lifecycle.MutableLiveData

object LitVideoPlayerSDK {

    var streamingUrl = ""
    var videoTitle = ""
    var heartRateObservable: MutableLiveData<DeviceDataCalculated>? = null

    lateinit var targetAreaImage: String
    lateinit var litAxis: MutableLiveData<DeviceDataCalculated>
    lateinit var strengthMachine: MutableLiveData<DeviceDataCalculated>
    lateinit var timeUnderTensionObserver: MutableLiveData<Triple<Int, Int, Int>>
    lateinit var repsObservable: MutableLiveData<Triple<Int, Int, Int>>
    lateinit var weightObservable: MutableLiveData<Triple<Int, Int, Int>>
    lateinit var resistanceObservable: MutableLiveData<Pair<Int, Int>>
    lateinit var caloriesObservable: MutableLiveData<Int>
    lateinit var videoPlaybackTimer: MutableLiveData<Long>

    lateinit var gender: String
    lateinit var weightUnit: String
    lateinit var dob: String
    lateinit var HR_CONNECTION_STATE: String
    lateinit var LIT_AXIS_CONNECTION_STATE: String
    lateinit var ROWING_MACHINE_CONNECTION_STATE: String
    var weight: Int = 0


}