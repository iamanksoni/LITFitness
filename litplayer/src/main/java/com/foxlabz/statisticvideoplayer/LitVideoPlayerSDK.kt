package com.foxlabz.statisticvideoplayer

import androidx.lifecycle.MutableLiveData

object LitVideoPlayerSDK {

    var streamingUrl = ""
    var videoTitle = ""
    var heartRate: MutableLiveData<DeviceDataCalculated>? = null
    lateinit var litAxis: MutableLiveData<DeviceDataCalculated>
    lateinit var strengthMachine: MutableLiveData<DeviceDataCalculated>

    lateinit var timeUnderTension: MutableLiveData<Triple<Int, Int, Int>>

}