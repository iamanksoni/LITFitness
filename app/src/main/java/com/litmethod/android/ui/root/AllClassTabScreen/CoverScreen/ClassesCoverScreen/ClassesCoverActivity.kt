package com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ClassesCoverScreen

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import carbon.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foxlabz.statisticvideoplayer.DeviceDataCalculated
import com.foxlabz.statisticvideoplayer.LitVideoPlayerSDK
import com.foxlabz.statisticvideoplayer.VideoPlayerActivity
import com.litmethod.android.BluetoothConnection.LitAxisDevicePair
import com.litmethod.android.BluetoothConnection.LitDeviceConstants
import com.litmethod.android.BluetoothConnection.LitDeviceConstants.HR_CONNECTION_STATE
import com.litmethod.android.BluetoothConnection.LitDeviceConstants.LIT_AXIS_CONNECTION_STATE
import com.litmethod.android.BluetoothConnection.LitDeviceConstants.LIT_AXIS_WEIGHT_SCALE_CHARACHTERISTIC
import com.litmethod.android.BluetoothConnection.LitDeviceConstants.LIT_AXIS_WEIGHT_SCALE_SERVICE
import com.litmethod.android.DataProcessing.ProcessedData
import com.litmethod.android.DataProcessing.RepsCalculator
import com.litmethod.android.Parsing.Converters
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityClassesCoverBinding
import com.litmethod.android.models.AcountScreenFragment.ClassBookmark.ClassBookmarkRequest
import com.litmethod.android.models.ClassDetails.EquipmentVideo
import com.litmethod.android.models.ClassDetails.InstructorInfo
import com.litmethod.android.models.GetEquipment.Data
import com.litmethod.android.models.GetInstructorInfo.GetInstructorInfoRequest
import com.litmethod.android.network.ClassesCoverActivityRepository
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.Onboarding.YourEquipmentScreen.YourEquipmentData
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ClassesCoverScreen.ViewModel.ClassCoverActvityViewModel
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ClassesCoverScreen.ViewModel.ClassesCoverActivityViewModelFactory
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.TrainerProfileScreen.TrainerProfileScreenActivity
import com.litmethod.android.utlis.MarginItemDecoration
import com.siliconlabs.bledemo.bluetooth.data_types.Field
import com.siliconlabs.bledemo.bluetooth.parsing.Common
import com.siliconlabs.bledemo.bluetooth.parsing.Engine
import com.welie.blessed.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ClassesCoverActivity : BaseActivity(),
    ClassCoverEquipmentAdapter.EquipmentAdapterClickListener,
    View.OnClickListener {
    lateinit var binding: ActivityClassesCoverBinding
    val dataList: ArrayList<YourEquipmentData> = ArrayList<YourEquipmentData>()
    private var yourEquipmentAdapter: ClassCoverEquipmentAdapter? = null
    val eqipLevel: ArrayList<Int> = ArrayList<Int>()
    val dataListDeviceVideo: ArrayList<String> = ArrayList<String>()
    private var classesCoverDeviceVideoAdapter: ClassesCoverDeviceVideoAdapter? = null
    var equipmentList: MutableList<Data> = ArrayList<Data>()
    var equipMentVideoList: ArrayList<EquipmentVideo> = ArrayList<EquipmentVideo>()
    var instructorInfoist: ArrayList<InstructorInfo> = ArrayList<InstructorInfo>()
    lateinit var viewModel: ClassCoverActvityViewModel
    lateinit var item: InstructorInfo
    var isVideoSave = false
    private lateinit var litAxisDevicePair: LitAxisDevicePair;
    private var positionClicked = -1;
    var averageRSSI: HashMap<String, Triple<Int, Int, BluetoothPeripheral>> =
        HashMap()
    private val retrofitService = RetrofitDataSourceService.getInstance()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var centralManager: BluetoothCentralManager;
    private lateinit var field: Field
    private lateinit var fieldValue: ByteArray
    private var value: ByteArray = ByteArray(0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_classes_cover)
        viewModelSetup()
        setUpUi()
        setUpAdapter()
        setDeviceAdapter()
        progressBarAnimation()
        clickListener()
        litAxisDevicePair = LitAxisDevicePair();
        centralManager = BluetoothCentralManager(applicationContext)
        LitDeviceConstants.mBleCentralManager = centralManager
        handleBLEConnectionObserver()

    }

    private fun setUpUi() {
        equipMentVideoList =
            BaseResponseDataObject.getClassDetailsResponse.equipment_video as ArrayList<EquipmentVideo>
        instructorInfoist =
            BaseResponseDataObject.getClassDetailsResponse.instructor_info as ArrayList<InstructorInfo>
        item = instructorInfoist.get(0)
        binding.trainnerProfileSub.tvTrainerName.text = item.instructor_name
        binding.trainnerProfileSub.tvTrainerVideoCount.text = "${item.video_count} classes"
        binding.trainnerProfileSub.tvTrainerVideoDesc.text = item.instructor_details

        if (BaseResponseDataObject.getClassDetailsResponse.isSave) {
            isVideoSave = true
            binding.imageView.setImageResource(R.drawable.ic_star_active)
        } else {
            isVideoSave = false
            binding.imageView.setImageResource(R.drawable.ic_like)
        }

        if (BaseResponseDataObject.getClassDetailsResponse.isViewed == false) {
            binding.ivNew.visibility = View.VISIBLE
        } else {
            binding.ivNew.visibility = View.GONE
        }

        val originalFormat: DateFormat = SimpleDateFormat("yyyy-mm-dd")
        val targetFormat: DateFormat = SimpleDateFormat("dd MMMM, yyyy")
        val date: Date = originalFormat.parse(BaseResponseDataObject.getClassDetailsResponse.date)
        val formattedDate: String = targetFormat.format(date)
        binding.tvDate.text = "Aired On $formattedDate"

        Glide.with(this)
            .load(BaseResponseDataObject.getClassDetailsResponse.thumbnail)
            .into(binding.expandedImage)
        Glide.with(this)
            .load(item.instructor_image)
            .into(binding.trainnerProfileSub.ivInstructorImage)

        val firstWord = "${BaseResponseDataObject.getClassDetailsResponse.title}"
        val secondWord = " with ${BaseResponseDataObject.getClassDetailsResponse.getInstructor}"
        val spannable: Spannable = SpannableString(firstWord + secondWord)
        spannable.setSpan(
            ForegroundColorSpan(getResources().getColor(R.color.white)),
            0,
            firstWord!!.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(getResources().getColor(R.color.mono_grey_60)),
            firstWord!!.length,
            firstWord!!.length + secondWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvVideoTitleWithName.text = spannable

        val firstArray = BaseResponseDataObject.getClassDetailsResponse.equipment.map {
            it.name
        }
        val secondArray = BaseResponseDataObject.getClassDetailsResponse.accessories.map {
            it.name
        }
        val combinedEquipments = (firstArray + secondArray).joinToString()
        binding.tvCombinedEquip.text = combinedEquipments
        val firstWordtime = "${BaseResponseDataObject.getClassDetailsResponse.getDurations}"
        val secondWordtime = "  •  "
        val ThirdWordtime = "${BaseResponseDataObject.getClassDetailsResponse.getLevelName}"
        val FourthWordtime = "  •  "
        val FifthWordtime = "${BaseResponseDataObject.getClassDetailsResponse.class_type}"

        val spannabletime: Spannable =
            SpannableString(firstWordtime + secondWordtime + ThirdWordtime + FourthWordtime + FifthWordtime)
        spannabletime.setSpan(
            ForegroundColorSpan(getResources().getColor(R.color.white)),
            0,
            firstWordtime!!.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannabletime.setSpan(
            ForegroundColorSpan(getResources().getColor(R.color.mono_grey_60)),
            firstWordtime!!.length,
            firstWordtime!!.length + secondWordtime.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        when (BaseResponseDataObject.getClassDetailsResponse.getLevelName) {
            "Intermediate" -> {
                spannabletime.setSpan(
                    ForegroundColorSpan(getResources().getColor(R.color.intermediate)),
                    firstWordtime!!.length + secondWordtime.length,
                    firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            "Beginner" -> {
                spannabletime.setSpan(
                    ForegroundColorSpan(getResources().getColor(R.color.beginner)),
                    firstWordtime!!.length + secondWordtime.length,
                    firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            "Advanced" -> {
                spannabletime.setSpan(
                    ForegroundColorSpan(getResources().getColor(R.color.Advanced)),
                    firstWordtime!!.length + secondWordtime.length,
                    firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

        }



        spannabletime.setSpan(
            ForegroundColorSpan(getResources().getColor(R.color.mono_grey_60)),
            firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length,
            firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length + FourthWordtime.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannabletime.setSpan(
            ForegroundColorSpan(getResources().getColor(R.color.white)),
            firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length + FourthWordtime.length,
            firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length + FourthWordtime.length + FifthWordtime.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvVideoTime.text = spannabletime

        if (intent.extras != null) {
            val pagename = intent.extras!!.getString("pagename")
            if (pagename == "ProgramsCoverActivity") {

//                binding.trainnerProgressSub.trpRating2.progressBackgroundColor = resources.getColor(R.color.blue_light)
//                binding.trainnerProgressSub.trpRating2.progressColor = resources.getColor(R.color.blue)
            }
        }
        binding.btnStartWorkout.setOnClickListener {

                RepsCalculator.activity=this@ClassesCoverActivity
                if (LIT_AXIS_CONNECTION_STATE == "CONNECTED") {
                    observeLitData()
                }
                if (HR_CONNECTION_STATE == "CONNECTED") {
                    observeHrData()
                }
                var time = (100.0 / 360)
                ProcessedData.calculateCaloriesBurnt(time)
                LitVideoPlayerSDK.heartRate = MutableLiveData()
                LitVideoPlayerSDK.streamingUrl =
                    "https://d1p2c1ey61b4dk.cloudfront.net/f1f2bd39-07b9-4e78-91b7-38e439b15151/hls/TIFFLsmSpdBndCirTra40Min1013-22.m3u8"

                startActivity(Intent(this@ClassesCoverActivity, VideoPlayerActivity::class.java))
        }

    }


    private fun observeLitData() {

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
                            RepsCalculator.leftBandActivity((data.toFloat() * 0.005 * 2.20462))
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
                        RepsCalculator.rightBandActivity((data.toFloat() * 0.005 * 2.20462))
                        Log.d("TAG", "Right Weight: " + data.toFloat() * 0.005 + "KG")

                    }
                }
            }
        }
    }


    private fun observeHrData() {
        if (LitDeviceConstants.mHeartRateMonitorPeripheral != null) {
            LitVideoPlayerSDK.heartRate = MutableLiveData()

            var notifyingCharacteristic =
                LitDeviceConstants.mHeartRateMonitorPeripheral.getCharacteristic(
                    LitDeviceConstants.LIT_HEART_RATE_SERVICE,
                    LitDeviceConstants.HEART_RATE_CHARACTERISTIC_UUID
                )

            notifyingCharacteristic?.let {
                scope.launch {
                    LitDeviceConstants.mHeartRateMonitorPeripheral.observe(it) { value ->
                        HeartRateMeasurement.fromBytes(value).toString()
                        LitVideoPlayerSDK.heartRate.postValue(
                            DeviceDataCalculated(
                                "Hear Rate",
                                HeartRateMeasurement.fromBytes(value).sensorContactStatus.toString() == "SupportedAndContacted",
                                HeartRateMeasurement.fromBytes(value).createdAt.toString(),
                                "HeartRateType",
                                HeartRateMeasurement.fromBytes(value).pulse.toString(),
                            )
                        )
                        Log.d(
                            "TAG",
                            "Weight LOgged in testing: " + HeartRateMeasurement.fromBytes(value).pulse
                        )

                    }
                }
            }
        }
    }

    private fun setUpAdapter() {
        dataList.clear()
        eqipLevel.clear()
        binding.rvEquipmentType.layoutManager =
            RecyclerView.LinearLayoutManager(this@ClassesCoverActivity)
        BaseResponseDataObject.getClassDetailsResponse.devices.map {
            equipmentList.add(Data(it.uuid, it.name, it.uuid, it.imgUrl, it.name))
            dataList.add(YourEquipmentData(false, false))

        }
        yourEquipmentAdapter =
            ClassCoverEquipmentAdapter(dataList, this@ClassesCoverActivity, equipmentList)
        binding.rvEquipmentType.adapter = yourEquipmentAdapter
        binding.rvEquipmentType.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.dp_10
                )
            )
        )
        yourEquipmentAdapter!!.setAdapterListener(this)


    }

    private fun setDeviceAdapter() {
        dataListDeviceVideo.clear()
        binding.rvDeviceVideo.layoutManager =
            RecyclerView.LinearLayoutManager(this@ClassesCoverActivity)
        classesCoverDeviceVideoAdapter =
            ClassesCoverDeviceVideoAdapter(equipMentVideoList, this@ClassesCoverActivity)
        binding.rvDeviceVideo.adapter = classesCoverDeviceVideoAdapter
        binding.rvDeviceVideo.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.dp_10
                )
            )
        )
    }

    override fun onItemEquipClick(position: Int, data: String) {
        if (dataList[position].selectedItem) {
            dataList[position].selectedItem = false
        } else {
            dataList[position].selectedItem = true
        }
        positionClicked = position
        yourEquipmentAdapter!!.notifyDataSetChanged()
        equipmentList.get(position)
        if (equipmentList.get(position).id == "0x180D") {
            connectWithHrSensor(position)
        }
        if (equipmentList.get(position).id == "0x181D") {
            handleLitAxisConnection(position)
        }
    }

    private fun connectWithHrSensor(position: Int) {
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


    private fun connectWithRowingMachine() {

    }


    private fun handleLitAxisConnectionLogic(peripheral: BluetoothPeripheral) {
        if (litAxisDevicePair.leftLitAxisDevice == null) {
            /**
             * SAVING LEFT LIT AXIS DEVICE
             */
            runOnUiThread() {

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
            litAxisDevicePair.setLeftLitAxis(peripheral)

            scope.launch {
//                DataPreferenceObject(this@DeviceScannerActivity).save(
//                    "leftLitAxis",
//                    peripheral.address
//                )
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
//                            DataPreferenceObject(this@DeviceScannerActivity).save(
//                                "rightLitAxis",
//                                peripheral.address
//                            )
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


        }
        if (litAxisDevicePair.rightLitAxisDevice != null && litAxisDevicePair.leftLitAxisDevice != null) {
            LitDeviceConstants.mLitAxisDevicePair = litAxisDevicePair
            centralManager.stopScan()
            LIT_AXIS_CONNECTION_STATE = "CONNECTED"
            runOnUiThread() {
                dataList[positionClicked].connectionStatus = true
                yourEquipmentAdapter?.notifyItemChanged(positionClicked)
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
        handleLitAxisConnection(positionClicked)

    }


    private fun handleBLEConnectionObserver() {

        centralManager.observeConnectionState { peripheral, state ->
            Log.d("Peripheral", " ${peripheral.address} has $state")
            if (state.toString() == "CONNECTED") {

                if (peripheral.getService(LitDeviceConstants.LIT_HEART_RATE_SERVICE) != null) {
                    centralManager.stopScan()
                    LitDeviceConstants.mBleCentralManager = centralManager
                    LitDeviceConstants.mHeartRateMonitorPeripheral = peripheral
                    HR_CONNECTION_STATE = "CONNECTED"
                    runOnUiThread() {
                        Toast.makeText(this, "Connected with heart rate sensor", Toast.LENGTH_SHORT)
                            .show()
                    }


                } else if (peripheral.getService(LitDeviceConstants.LIT_AXIS_WEIGHT_SCALE_SERVICE) != null) {

                    handleLitAxisConnectionLogic(peripheral)

                } else if (peripheral.getService(LitDeviceConstants.LIT_STRENGTH_MACHINE_SERVICE) != null) {
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

                    if (peripheral.getService(LitDeviceConstants.LIT_HEART_RATE_SERVICE) != null) {
                        Log.d("Disconnection", "HR sensor disconnected")
                        HR_CONNECTION_STATE = "DISCONNECTED"
                        handleHeartRateConnection()
                    } else {
                        Log.d("Disconnection", "Lit Axis disconnected")
                        updateLitAxisConnectionPair(peripheral)
                        LIT_AXIS_CONNECTION_STATE = "DISCONNECTED"
                        runOnUiThread() {
                            dataList[positionClicked].connectionStatus = false
                            yourEquipmentAdapter?.notifyItemChanged(positionClicked)
                        }
                    }
                }

            }
        }
    }


    private fun handleHeartRateConnection() {
        var list = arrayOf<UUID>(LitDeviceConstants.LIT_HEART_RATE_SERVICE)
        scanAndConnectWithHeartRateSensor(list)
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

    private fun handleLitAxisConnection(position: Int) {

        var list = arrayOf<UUID>(LitDeviceConstants.LIT_AXIS_WEIGHT_SCALE_SERVICE)
        scanAndConnectLitAxisDevice(list)
    }


    private fun scanAndConnectLitAxisDevice(list: Array<UUID>) {

        Log.d("Device Scanner Activity", "Starting LE Scan")
        if (!centralManager.isScanning) {
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

    private fun progressBarAnimation() {
        val timer = object : CountDownTimer(1000, 500) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                val progressLikePercent =
                    BaseResponseDataObject.getClassDetailsResponse.classRatting.totalLikepersentage.toFloat()
                val totalRating =
                    BaseResponseDataObject.getClassDetailsResponse.classRatting.totalCount
                val difficultyRating =
                    BaseResponseDataObject.getClassDetailsResponse.difficultyRating
                val percent = (difficultyRating.toDouble() / 5) * 100
                Log.d("thepercentis", "the percent $percent and rating $difficultyRating")
                binding.trainnerProgressSub.progressbar1.progress = progressLikePercent
                binding.trainnerProgressSub.progressbar2.progress = percent.toFloat()
                binding.trainnerProgressSub.progressbar1.labelText =
                    "$totalRating RATINGS  (${progressLikePercent.toInt()}%)"
                binding.trainnerProgressSub.progressbar2.labelText =
                    "DIFFICULTY ${difficultyRating}/5"
                //                binding.trainnerProgressSub.trpRating.progress = 70f
//                binding.trainnerProgressSub.trpRating2.progress = 70f
            }
        }
        timer.start()


    }

    private fun clickListener() {
        binding.ibBackButton.setOnClickListener(this)
        binding.trainnerProfileSub.llTopLayout.setOnClickListener(this)
        binding.imageView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {
                finish()
            }
            R.id.ll_top_layout -> {
                binding.spLoading.visibility = View.VISIBLE
                viewModel.checkGetInstructorInfoRequest(
                    "${BaseResponseDataObject.accessToken}",
                    GetInstructorInfoRequest("10", "getInstructorinfo", item.instructor_id, "1")
                )
                Log.d("theINstrust", "the instructor id is ${item.instructor_id}")
            }
            R.id.imageView -> {
                binding.spLoading.visibility = View.VISIBLE
                if (isVideoSave) {
                    viewModel.checkgetClassBookmark(
                        BaseResponseDataObject.accessToken,
                        ClassBookmarkRequest(
                            "classBookmark",
                            "${BaseResponseDataObject.getClassDetailsResponse.id}",
                            "unsave"
                        )
                    )
                    isVideoSave = false
                    binding.imageView.setImageResource(R.drawable.ic_like)
                } else {
                    viewModel.checkgetClassBookmark(
                        BaseResponseDataObject.accessToken,
                        ClassBookmarkRequest(
                            "classBookmark",
                            "${BaseResponseDataObject.getClassDetailsResponse.id}",
                            "save"
                        )
                    )
                    isVideoSave = true
                    binding.imageView.setImageResource(R.drawable.ic_star_active)
                }

            }

        }
    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(
                this, ClassesCoverActivityViewModelFactory(
                    ClassesCoverActivityRepository(retrofitService), this
                )
            ).get(
                ClassCoverActvityViewModel::class.java
            )
        loginResponse()
    }


    private fun loginResponse() {

        viewModel.getInstructorInfoResponse.observe(this, Observer {
            Log.d("theSucccessData", "the succes data is ${it.result}")
            binding.spLoading.visibility = View.GONE
            BaseResponseDataObject.getInstructorInfoResponse = it.result.pagenation.data
            intentActivity(this@ClassesCoverActivity, TrainerProfileScreenActivity::class.java, "")
        })
        viewModel.errorMessage.observe(this, Observer {

        })

        viewModel.classBookmarkResponse.observe(this, Observer {
            binding.spLoading.visibility = View.GONE
            Log.d("getData255", "message is ${it.serverResponse.message}")
        })
        viewModel.errorMessage.observe(this, Observer {
            binding.spLoading.visibility = View.GONE
        })
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


