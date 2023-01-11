package com.foxlabz.statisticvideoplayer

import androidx.lifecycle.MutableLiveData
import com.foxlabz.statisticvideoplayer.Parsing.DeviceData
import java.util.*

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
//    lateinit var deviceData: listOf<Long>
    lateinit var gender: String
    var feedStation: String = "Rowing_Radio_Station_1"
    lateinit var weightUnit: String
    lateinit var dob: String
    lateinit var HR_CONNECTION_STATE: String
    lateinit var LIT_AXIS_CONNECTION_STATE: String
    lateinit var ROWING_MACHINE_CONNECTION_STATE: String
    var weight: Int = 0


    val LIT_AXIS_WEIGHT_SCALE_SERVICE = UUID.fromString("0000181d-0000-1000-8000-00805f9b34fb")
    val LIT_AXIS_GENERIC_ATTTR = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb")
    val LIT_AXIS_GENERIC_ACCESS = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb")
    val LIT_AXIS_BATTERY_SERVICE = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb")
    val LIT_AXIS_DEVICE_INFO_SERVICE = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb")
    val HEART_RATE_CHARACTERISTIC_UUID =
        UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb")
    val LIT_HEART_RATE_SERVICE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb")
    val LIT_STRENGTH_MACHINE_SERVICE = UUID.fromString("00001826-0000-1000-8000-00805f9b34fb")
    val LIT_AXIS_WEIGHT_SCALE_CHARACHTERISTIC =
        UUID.fromString("00002a98-0000-1000-8000-00805f9b34fb")

    var dataList: ArrayList<DeviceData> = ArrayList<DeviceData>()
}