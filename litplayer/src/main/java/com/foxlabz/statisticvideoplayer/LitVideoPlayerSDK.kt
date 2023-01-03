package com.foxlabz.statisticvideoplayer

import androidx.lifecycle.MutableLiveData

object LitVideoPlayerSDK {

    var streamingUrl = ""
    lateinit var heartRate: MutableLiveData<DeviceDataCalculated?>
    lateinit var litAxis: MutableLiveData<DeviceDataCalculated>
    lateinit var strengthMachine: MutableLiveData<DeviceDataCalculated>

}