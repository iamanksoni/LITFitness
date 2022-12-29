package com.litmethod.android.BluetoothConnection

import com.welie.blessed.BluetoothPeripheral

class LitAxisDevicePair {

    var leftLitAxisDevice: BluetoothPeripheral?=null
    var rightLitAxisDevice: BluetoothPeripheral?=null

    fun getLeftLitAxis(): BluetoothPeripheral? {
        return this.leftLitAxisDevice
    }

    fun getRightLitAxis(): BluetoothPeripheral? {
        return this.rightLitAxisDevice
    }

    fun setLeftLitAxis(leftLitAxisDevice: BluetoothPeripheral?) {
        this.leftLitAxisDevice = leftLitAxisDevice
    }

    fun setRightLitAxis(rightLitAxisDevice: BluetoothPeripheral?) {
        this.rightLitAxisDevice = rightLitAxisDevice
    }
}