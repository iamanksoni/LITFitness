package com.foxlabz.statisticvideoplayer

import androidx.lifecycle.MutableLiveData

object LitVideoPlayerSDK {

    var streamingUrl = ""
    var heartRate: MutableLiveData<DeviceDataCalculated>? = null
    lateinit var litAxis: MutableLiveData<DeviceDataCalculated>
    lateinit var strengthMachine: MutableLiveData<DeviceDataCalculated>

}