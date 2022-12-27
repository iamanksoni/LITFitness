package com.litmethod.android

import android.os.Bundle
import android.util.Log
import com.litmethod.android.BluetoothConnection.LitDeviceConstants
import com.litmethod.android.Parsing.Converters
import com.litmethod.android.databinding.ActivityDeviceDataLoggerBinding
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.utlis.AppConstants
import com.siliconlabs.bledemo.bluetooth.data_types.Field
import com.siliconlabs.bledemo.bluetooth.parsing.Common
import com.siliconlabs.bledemo.bluetooth.parsing.Engine
import com.welie.blessed.HeartRateMeasurement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.util.*

class DeviceDataLoggerActivity : BaseActivity() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var field: Field
    private lateinit var fieldValue: ByteArray
    private var value: ByteArray = ByteArray(0)
    private lateinit var binding: ActivityDeviceDataLoggerBinding

    val LIT_AXIS_WEIGHT_SCALE_SERVICE =
        UUID.fromString("0000181d-0000-1000-8000-00805f9b34fb")
    val LIT_AXIS_WEIGHT_SCALE_CHARACHTERISTIC =
        UUID.fromString("00002a98-0000-1000-8000-00805f9b34fb")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceDataLoggerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var deviceName = intent?.getStringExtra(
            AppConstants.DEVICE_NAME
        )
        when (deviceName) {


            AppConstants.DEVICE_LIT_AXIS -> {
                var notificationLeftDevice =
                    LitDeviceConstants.mLitAxisDevicePair.leftLitAxisDevice?.getCharacteristic(
                        LIT_AXIS_WEIGHT_SCALE_SERVICE,
                        LIT_AXIS_WEIGHT_SCALE_CHARACHTERISTIC
                    )

                var notificationRightDevice =
                    LitDeviceConstants.mLitAxisDevicePair.rightLitAxisDevice?.getCharacteristic(
                        LIT_AXIS_WEIGHT_SCALE_SERVICE,
                        LIT_AXIS_WEIGHT_SCALE_CHARACHTERISTIC
                    )

                notificationLeftDevice?.let {
                    scope.launch {
                        LitDeviceConstants.mLitAxisDevicePair.leftLitAxisDevice?.observe(it) { value ->
                            Log.d("Data Size", value.size.toString())
                            "Received : $value".also {
                                dataParser()
                                fieldValue = value
                                val data = readValue()
                                runOnUiThread {
                                    binding.tvLeftAxis.text =
                                        "Left Pull-->:${(data.toFloat() * 0.005)} KG"
                                }
                                Log.d("TAG", "Left Weight: " + data.toFloat() * 0.005 + "KG")

                            }
                        }
                    }
                }

                notificationRightDevice?.let {
                    scope.launch {
                        LitDeviceConstants.mLitAxisDevicePair.rightLitAxisDevice?.observe(it) { value ->
                            Log.d("Data Size", value.size.toString())
                            "Received : $value".also {
                                dataParser()
                                fieldValue = value
                                val data = readValue()
                                runOnUiThread {
                                    binding.tvRightAxis.text =
                                        "Right Pull-->:${(data.toFloat() * 0.005)} KG"
                                }
                                Log.d("TAG", "Right Weight: " + data.toFloat() * 0.005 + "KG")

                            }
                        }
                    }
                }
            }

            AppConstants.DEVICE_HEART_RATE -> {


                var notifyingCharacteristic =
                    LitDeviceConstants.mHeartRateMonitorPeripheral.getCharacteristic(
                        LitDeviceConstants.LIT_HEART_RATE_SERVICE,
                        LitDeviceConstants.HEART_RATE_CHARACTERISTIC_UUID
                    )
                notifyingCharacteristic?.let {
                    scope.launch {
                        LitDeviceConstants.mHeartRateMonitorPeripheral.observe(it) { value ->
                            runOnUiThread{
                                binding.tvHeartRate.text =
                                    HeartRateMeasurement.fromBytes(value).toString()
                            }
                            Log.d(
                                "TAG",
                                "Weight LOgged in testing: " + HeartRateMeasurement.fromBytes(value).pulse
                            )

                        }
                    }
                }


            }
        }
    }

    private fun dataParser() {
        var mChar =
            Engine.getCharacteristic(UUID.fromString("00002a98-0000-1000-8000-00805f9b34fb"))
        field = mChar?.fields?.get(0)!!
    }

    companion object {
        private const val TYPE_FLOAT = "FLOAT"
        private const val TYPE_SFLOAT = "SFLOAT"
        private const val TYPE_FLOAT_32 = "float32"
        private const val TYPE_FLOAT_64 = "float64"
    }

    private fun readValue(): String {
        if (fieldValue.isEmpty()) {
            return ""
        }
        val format = field.format!!
        val formatLength = Engine.getFormat(format)

        if (formatLength == 0) {
            return if (format.toLowerCase(Locale.getDefault()) == "reg-cert-data-list") {
                val result = StringBuilder(
                    "0x" + Converters.bytesToHexWhitespaceDelimited(fieldValue)
                )
                StringBuilder(result.toString().replace(" ", "")).toString()
            } else {
                StringBuilder(String(fieldValue)).toString()
            }
        } else {
            return when {
                field.isFullByteSintFormat() -> convertSintToString()
                field.isFullByteUintFormat() -> convertUintToString(formatLength)
                field.isFloatFormat() -> {
                    val fValue = readFloat(format, formatLength)
                    StringBuilder(String.format(Locale.US, "%.1f", fValue)).toString()
                }
                else -> {
                    val result = StringBuilder()
                    for (element in fieldValue) {
                        result.append((element.toInt().and(0xff)))
                    }
                    result.toString()
                }
            }
        }
    }

    private fun convertSintToString(): String {
        val builder = StringBuilder()
        val reversedArray = fieldValue.reversedArray()
        for (i in reversedArray.indices) {
            if (reversedArray[i] < 0) {
                reversedArray[i] = (reversedArray[i] + 256).toByte()
            }
            builder.append(Converters.getHexValue(reversedArray[i]))
        }

        var result = builder.toString().toInt(16)
        if (result >= (Math.pow(256.0, fieldValue.size.toDouble()) / 2)) {
            result -= Math.pow(256.0, fieldValue.size.toDouble()).toInt()
        }

        return result.toString()
    }

    private fun convertUintToString(formatLength: Int): String {
        return try {
            if (formatLength < 9) {
                var uintAsLong = 0L
                for (i in 0 until formatLength) {
                    uintAsLong = uintAsLong shl 8
                    val byteAsInt: Int = fieldValue[formatLength - 1 - i].toInt().and(0xff)
                    uintAsLong = uintAsLong or byteAsInt.toLong()
                }
                uintAsLong.toString()
            } else { // uint128
                val binaryString = StringBuilder()
                for (element in fieldValue) {
                    binaryString.append(
                        String.format("%8s", Integer.toBinaryString(element.toInt().and(0xFF)))
                            .replace(' ', '0')
                    )
                }
                BigInteger("0$binaryString", 2).toString(16)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }

    private fun readFloat(format: String, formatLength: Int): Double {
        var result = 0.0
        when (format) {
            TYPE_SFLOAT -> result = Common.readSfloat(fieldValue).toDouble()
            TYPE_FLOAT -> result = Common.readFloat(fieldValue, 0, formatLength - 1).toDouble()
            TYPE_FLOAT_32 -> result = Common.readFloat32(fieldValue).toDouble()
            TYPE_FLOAT_64 -> result = Common.readFloat64(fieldValue)
        }
        return result
    }
}
