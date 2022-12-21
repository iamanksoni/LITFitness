package com.litmethod.android.BluetoothConnection

import com.welie.blessed.BluetoothCentralManager
import com.welie.blessed.BluetoothPeripheral
import java.util.*


object LitDeviceConstants {

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

    lateinit var mRightLitAxisPeripheral: BluetoothPeripheral
    lateinit var mLeftLitAxisPeripheral: BluetoothPeripheral

    lateinit var mHeartRateMonitorPeripheral: BluetoothPeripheral
    lateinit var mStrengthMachinePeripheral: BluetoothPeripheral

    lateinit var mBleCentralManager: BluetoothCentralManager

    var mLitAxisDevicePair: LitAxisDevicePair = LitAxisDevicePair()


}