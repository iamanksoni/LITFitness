package com.litmethod.android.BluetoothConnection

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.litmethod.android.BluetoothConnection.LitDeviceConstants.HEART_RATE_CHARACTERISTIC_UUID
import com.litmethod.android.BluetoothConnection.LitDeviceConstants.LIT_AXIS_WEIGHT_SCALE_CHARACHTERISTIC
import com.litmethod.android.BluetoothConnection.LitDeviceConstants.LIT_AXIS_WEIGHT_SCALE_SERVICE
import com.litmethod.android.BluetoothConnection.LitDeviceConstants.LIT_HEART_RATE_SERVICE
import com.litmethod.android.BluetoothConnection.LitDeviceConstants.LIT_STRENGTH_MACHINE_SERVICE
import com.litmethod.android.Parsing.Converters
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityDeviceScannerBinding
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.utlis.AppConstants.Companion.DEVICE_HEART_RATE
import com.litmethod.android.utlis.AppConstants.Companion.DEVICE_LIT_AXIS
import com.litmethod.android.utlis.AppConstants.Companion.DEVICE_NAME
import com.litmethod.android.utlis.AppConstants.Companion.DEVICE_STRENGTH_MACHINE
import com.litmethod.android.utlis.DataPreferenceObject
import com.siliconlabs.bledemo.bluetooth.data_types.Field
import com.siliconlabs.bledemo.bluetooth.parsing.Common
import com.siliconlabs.bledemo.bluetooth.parsing.Engine
import com.welie.blessed.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.util.*

class DeviceScannerActivity : BaseActivity() {
    lateinit var binding: ActivityDeviceScannerBinding
    private val neededPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN
    )
    var time = Long.MAX_VALUE
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var centralManager: BluetoothCentralManager;
    private val SCAN_PERIOD: Long = 10000
    private var isScanning: Boolean = false;
    private lateinit var litAxisDevicePair: LitAxisDevicePair;
    private lateinit var field: Field
    private lateinit var fieldValue: ByteArray
    private var value: ByteArray = ByteArray(0)
    private lateinit var builder: AlertDialog;
    var averageRSSI: HashMap<String, Triple<Int, Int, BluetoothPeripheral>> =
        HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkGPSIsOpen()
        checkPermissions()
        handlePermissions()
        litAxisDevicePair = LitAxisDevicePair();
        if (!checkIsAdapterEnabled()) {
            startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
            return
        }
        centralManager = BluetoothCentralManager(applicationContext)

        LitDeviceConstants.mBleCentralManager = centralManager
        handleBLEConnectionObserver()

        setupUI()

    }

    private fun handleBLEConnectionObserver() {
        val deviceName = intent?.getStringExtra(DEVICE_NAME)

        centralManager.observeConnectionState { peripheral, state ->
            Log.d("Peripheral", " ${peripheral.address} has $state")
            if (state.toString() == "CONNECTED") {

                if (peripheral.getService(LIT_HEART_RATE_SERVICE) != null) {
                    centralManager.stopScan()
                    LitDeviceConstants.mBleCentralManager = centralManager
                    LitDeviceConstants.mHeartRateMonitorPeripheral = peripheral
                    finish()

//                    var intent = Intent(this, DeviceDataLoggerActivity::class.java)
//                    intent.putExtra(AppConstants.DEVICE_NAME, AppConstants.DEVICE_HEART_RATE)
//                    startActivity(intent)

                    scope.launch {
                        DataPreferenceObject(this@DeviceScannerActivity).save(
                            "hrSensor",
                            peripheral.address
                        )
                    }
                    runOnUiThread() {

                        Toast.makeText(this, "Connected with heart rate sensor", Toast.LENGTH_SHORT)
                            .show()
//                        showHRConsole(peripheral)
                    }


                } else if (peripheral.getService(LIT_AXIS_WEIGHT_SCALE_SERVICE) != null) {

                    handleLitAxisConnectionLogic(peripheral)

                } else if (peripheral.getService(LIT_STRENGTH_MACHINE_SERVICE) != null) {
                    centralManager.stopScan()
                    runOnUiThread() {
                        Toast.makeText(this, "Connected with ROWING sensor", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    scope.launch {
                        try {
                            centralManager.cancelConnection(peripheral)
                        } catch (connectionFailed: ConnectionFailedException) {
                            Log.d("Device Scanner Activity", "disconnection failed")
                        }
                    }
                }
            }
            if (state.toString() == "DISCONNECTED") {
                if (!centralManager.isScanning) {
                    when (deviceName) {
                        DEVICE_LIT_AXIS -> {

                            updateLitAxisConnectionPair(peripheral)

                        }
                        DEVICE_HEART_RATE -> {
                            if (peripheral.getService(LIT_HEART_RATE_SERVICE) != null) {
                                Log.d("Disconnection", "HR sensor disconnected")
                                handleHeartRateConnection()
                            }
                        }
                        DEVICE_STRENGTH_MACHINE -> {

                            handleRowingMachineConnection()
                        }
                    }
                }
            }
        }
    }

    private fun handleLitAxisConnectionLogic(peripheral: BluetoothPeripheral) {
        if (litAxisDevicePair.leftLitAxisDevice == null) {
            /**
             * SAVING LEFT LIT AXIS DEVICE
             */
            runOnUiThread() {
                binding.tvLitHeartRateMessage.visibility = View.VISIBLE
                binding.tvLitHeartRateMessage.text = "Left Lit Axis Connected"
                binding.tvLitHeartRateMessage.setTextColor(R.color.blue)
                binding.tvLitHeartRateMessage.typeface =
                    Typeface.createFromAsset(assets, "futura_std_condensed.otf")
                Toast.makeText(
                    this,
                    "Connected with Left Lit AXIS sensor",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            val spannable =
                SpannableString(getString(R.string.right_lit_axis_connect_message))
            spannable.setSpan(
                ForegroundColorSpan(Color.RED),
                10, // start
                15, // end
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            binding.tvLitAxisMessage.text = spannable
            litAxisDevicePair.setLeftLitAxis(peripheral)

            scope.launch {
                DataPreferenceObject(this@DeviceScannerActivity).save(
                    "leftLitAxis",
                    peripheral.address
                )
            }

        } else if (litAxisDevicePair.rightLitAxisDevice == null) {
            if (litAxisDevicePair.leftLitAxisDevice != null) {

                litAxisDevicePair.leftLitAxisDevice?.let {
                    if (it.address != peripheral.address) {
                        runOnUiThread() {
                            Toast.makeText(
                                this,
                                "Connected with Right Lit AXIS sensor",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        litAxisDevicePair.setRightLitAxis(peripheral)

                        scope.launch {
                            DataPreferenceObject(this@DeviceScannerActivity).save(
                                "rightLitAxis",
                                peripheral.address
                            )
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this, "Device Mac is already paired", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

            } else {
                runOnUiThread {
                    Toast.makeText(
                        this,
                        "Left is disconnected so, making this one as the left axis connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            if (litAxisDevicePair.rightLitAxisDevice != null && litAxisDevicePair.leftLitAxisDevice != null) {
                LitDeviceConstants.mLitAxisDevicePair = litAxisDevicePair
                centralManager.stopScan()

                finish()

//                var intent = Intent(this, DeviceDataLoggerActivity::class.java)
//                intent.putExtra(AppConstants.DEVICE_NAME, AppConstants.DEVICE_LIT_AXIS)
//                startActivity(intent)
            }
        }

    }

    private fun updateLitAxisConnectionPair(disconnectedPeripheral: BluetoothPeripheral) {

        when (disconnectedPeripheral.address) {
            litAxisDevicePair.getLeftLitAxis()?.address -> {
                litAxisDevicePair.setLeftLitAxis(null)
                Log.d("DeviceScanner Activity", "Left Device Disconnected")
                runOnUiThread {
                    Toast.makeText(this, "Disconnected Left Lit Axis device", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            litAxisDevicePair.getRightLitAxis()?.address -> {
                litAxisDevicePair.setRightLitAxis(null)
                Log.d("DeviceScanner Activity", "Right Device Disconnected")
                runOnUiThread {
                    Toast.makeText(this, "Disconnected Right Lit Axis device", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
        LitDeviceConstants.mLitAxisDevicePair = litAxisDevicePair
        handleLitAxisConnection()

    }

    private fun setupUI() {
        val deviceName = intent?.getStringExtra(DEVICE_NAME)
        val spannable =
            SpannableString(getString(R.string.left_lit_axis_connect_message))
        spannable.setSpan(
            ForegroundColorSpan(Color.RED),
            10, // start
            14, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.tvLitAxisMessage.text = spannable;
        when (deviceName) {
            DEVICE_LIT_AXIS -> {
                Glide.with(this).load(R.drawable.device_lit_axis_animation)
                    .into(binding.ivAnimation)
                handleLitAxisConnection()

            }
            DEVICE_HEART_RATE -> {

                binding.tvLitHeartRateMessage.visibility = View.VISIBLE
                binding.tvLitAxisMessage.visibility = View.GONE
                Glide.with(this).load(R.drawable.heart_rate).into(binding.ivAnimation)
                handleHeartRateConnection()
            }

            DEVICE_STRENGTH_MACHINE -> {
                Glide.with(this).load(R.drawable.strength_machine).into(binding.ivAnimation)
                handleRowingMachineConnection()
            }
        }
        handleScanningAnimation()
    }

    private fun handleScanningAnimation() {
        object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var text = binding.tvScanning.text.toString().toLowerCase()
                if (text == "scanning") {
                    runOnUiThread {
                        binding.tvScanning.text = "Scanning."
                    }
                }
                if (text == "scanning.") {
                    runOnUiThread {
                        binding.tvScanning.text = "Scanning.."
                    }
                }
                if (text == "scanning..") {
                    runOnUiThread {
                        binding.tvScanning.text = "Scanning..."
                    }
                }
                if (text == "scanning...") {

                    runOnUiThread {
                        binding.tvScanning.text = "Scanning"

                    }
                }
            }

            override fun onFinish() {
            }

        }.start()

    }

    private fun handleLitAxisConnection() {

        var list = arrayOf<UUID>(LIT_AXIS_WEIGHT_SCALE_SERVICE)
        scanAndConnectLitAxisDevice(list)
    }

    private fun handleHeartRateConnection() {
        var list = arrayOf<UUID>(LIT_HEART_RATE_SERVICE)
        scanAndConnectWithHeartRateSensor(list)
    }

    private fun handleRowingMachineConnection() {
        var list = arrayOf<UUID>()
        scanAndConnectLitAxisDevice(list)
    }


    override fun onResume() {
        super.onResume()

    }

    private fun checkPermissions() {

        val permission2 =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH_CONNECT
                ), 1
            )
        }
    }


    private fun checkGPSIsOpen(): Boolean {
        val locationManager =
            this.getSystemService(LOCATION_SERVICE) as LocationManager ?: return false
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        if (shouldShowRequestPermissionRationale(neededPermissions[0]) || shouldShowRequestPermissionRationale(
                neededPermissions[1]
            )
        ) {

        } else {
            requestPermissions(neededPermissions, PERMISSIONS_REQUEST_CODE)
        }
    }

    private fun checkIsAdapterEnabled(): Boolean {
        val bluetoothAdapter: Boolean =
            getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
        if (!bluetoothAdapter) {
            return false
        } else {
            if (!checkLeSupported()) {
                Toast.makeText(this, "LE Feature is not supported", Toast.LENGTH_LONG).show()
                return false
            }
        }

        return true

    }

    private fun checkLeSupported(): Boolean {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    private fun handlePermissions() {
        if (!isPermissionGranted(neededPermissions[0]) || !isPermissionGranted(neededPermissions[1])) {
            requestPermission()
        } else {
//            scanLeDevice()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun onPause() {
        super.onPause()

    }

    private fun scanAndConnectLitAxisDevice(list: Array<UUID>) {

        Log.d("Device Scanner Activity", "Starting LE Scan")
        if (!isScanning) {
            centralManager.scanForPeripheralsWithServices(list, { peripheral, scanResult ->
                Log.d("Device", peripheral.address)
                scope.launch {
                    try {
                        centralManager.autoConnectPeripheral(peripheral)
                    } catch (connectionFailed: ConnectionFailedException) {
                        Log.d("TAG", "connection failed")
                    }
                }
            }, { error ->
                Log.d("Central Manager", error.toString())
            })
        }

    }

    private fun scanAndConnectWithHeartRateSensor(list: Array<UUID>) {

        var deviceMap = HashMap<String, BluetoothPeripheral>()
        centralManager.scanForPeripherals({ peripheral, scanResult ->

            if (scanResult.rssi > -60) {
                Log.d("Device --->>>", peripheral.address + "   RSSI -->>>" + scanResult.rssi)
                if (!averageRSSI.contains(peripheral.address)) {
                    averageRSSI.put(
                        peripheral.name, Triple(
                            1,
                            scanResult.rssi,
                            peripheral
                        )
                    )
                } else {
                    var dataPayload = averageRSSI.getValue(peripheral.address)
                    var rssi = dataPayload.second
                    var count = dataPayload.first
                    var average = (rssi * count + scanResult.rssi) / (count + 1)
                    var updatedPacket =
                        Triple(count + 1, average, peripheral)
                    averageRSSI.put(peripheral.address, updatedPacket)

                }
            }


        }, { scanFailure: ScanFailure ->

        })

        runOnUiThread {
            Handler().postDelayed(Runnable {
                centralManager.stopScan()
                averageRSSI.values.forEach {
                    Log.d("RSSI AVERAGE", "${it.third.address} ----> ${it.second}")
                    scope.launch {
                        centralManager.connectPeripheral(it.third)
                    }

                }

            }, 10000)
        }

    }


    private fun dataParser() {
        var mChar =
            Engine.getCharacteristic(UUID.fromString("00002a98-0000-1000-8000-00805f9b34fb"))
        field = mChar?.fields?.get(0)!!
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 400

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


    private fun showConsole(peripheral: BluetoothPeripheral) {
        var data = ""
        builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.ble_device_console, null)
        val notifyButton = view.findViewById<Button>(R.id.getNotifyPermission)
        val readButton = view.findViewById<Button>(R.id.getReadPermission)
        val consoleView = view.findViewById<TextView>(R.id.console)
        val clear_console = view.findViewById<TextView>(R.id.clear_text)
        val msg_box = view.findViewById<TextView>(R.id.message_box)
        clear_console.setOnClickListener {
            consoleView.text = ""
            data = ""
        }


        scope.launch {
            peripheral.readCharacteristic(
                LIT_AXIS_WEIGHT_SCALE_SERVICE,
                LIT_AXIS_WEIGHT_SCALE_CHARACHTERISTIC
            )
        }

        var notificationCharacteristic = peripheral.getCharacteristic(
            LIT_AXIS_WEIGHT_SCALE_SERVICE,
            LIT_AXIS_WEIGHT_SCALE_CHARACHTERISTIC
        )
        notificationCharacteristic?.let {
            scope.launch {
                peripheral.observe(it) { value ->
                    Log.d("Data Size", value.size.toString())
                    "Received : $value".also {
                        dataParser()
                        fieldValue = value
                        var data = readValue()
                        consoleView.text = String.format("%.2f", (data.toFloat() * 0.0005)) + "KG"
                        Log.d("TAG", "Weight LOgged: " + data.toFloat() * 0.0005 + "KG")

                    }
                }
            }
        }
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(builder.getWindow()?.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = 800




        builder.setView(view)
        builder.setCanceledOnTouchOutside(false)
        builder.show()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.90).toInt()

        builder.getWindow()?.setLayout(width, height)
    }

    private fun showHRConsole(peripheral: BluetoothPeripheral) {
        var data = ""
        builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.ble_device_console, null)
        val notifyButton = view.findViewById<Button>(R.id.getNotifyPermission)
        val readButton = view.findViewById<Button>(R.id.getReadPermission)
        val consoleView = view.findViewById<TextView>(R.id.console)
        val clear_console = view.findViewById<TextView>(R.id.clear_text)
        val msg_box = view.findViewById<TextView>(R.id.message_box)


        val lp = WindowManager.LayoutParams()
        lp.copyFrom(builder.getWindow()?.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = 800

        var notifyingCharacteristic = peripheral.getCharacteristic(
            LIT_HEART_RATE_SERVICE,
            HEART_RATE_CHARACTERISTIC_UUID
        )
        consoleView.text="Measuring Your pulse ... Please wait for a while"
        notifyingCharacteristic?.let {
            scope.launch {
                peripheral.observe(it) { value ->
                    consoleView.text = HeartRateMeasurement.fromBytes(value).pulse.toString() +"BPM"
                    Log.d(
                        "TAG",
                        "Weight LOgged: " + HeartRateMeasurement.fromBytes(value).pulse
                    )

                }
            }
        }


        builder.setView(view)
        builder.setCanceledOnTouchOutside(false)
        builder.show()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.90).toInt()

        builder.getWindow()?.setLayout(width, height)
    }

}