package com.foxlabz.statisticvideoplayer

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Typeface
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.mediarouter.app.MediaRouteButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foxlabz.statisticvideoplayer.LitVideoPlayerSDK.HEART_RATE_CHARACTERISTIC_UUID
import com.foxlabz.statisticvideoplayer.LitVideoPlayerSDK.HR_CONNECTION_STATE
import com.foxlabz.statisticvideoplayer.LitVideoPlayerSDK.LIT_AXIS_CONNECTION_STATE
import com.foxlabz.statisticvideoplayer.LitVideoPlayerSDK.LIT_AXIS_WEIGHT_SCALE_CHARACHTERISTIC
import com.foxlabz.statisticvideoplayer.LitVideoPlayerSDK.LIT_AXIS_WEIGHT_SCALE_SERVICE
import com.foxlabz.statisticvideoplayer.LitVideoPlayerSDK.LIT_HEART_RATE_SERVICE
import com.foxlabz.statisticvideoplayer.LitVideoPlayerSDK.LIT_STRENGTH_MACHINE_SERVICE
import com.foxlabz.statisticvideoplayer.LitVideoPlayerSDK.videoPlaybackTimer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.ui.TimeBar
import com.google.android.gms.cast.framework.CastContext
import com.litmethod.android.BluetoothConnection.LitAxisDevicePair
import com.litmethod.android.BluetoothConnection.LitDeviceConstants
import com.litmethod.android.BluetoothConnection.LitDeviceConstants.mBleCentralManager
import com.litmethod.android.BluetoothConnection.LitDeviceConstants.mHeartRateMonitorPeripheral
import com.litmethod.android.BluetoothConnection.LitDeviceConstants.mLitAxisDevicePair
import com.litmethod.android.DataProcessing.RepsCalculator
import com.litmethod.android.Parsing.Converters
import com.mradzinski.caster.Caster
import com.mradzinski.caster.Caster.OnCastSessionStateChanged
import com.mradzinski.caster.MediaData
import com.siliconlabs.bledemo.bluetooth.data_types.Field
import com.siliconlabs.bledemo.bluetooth.parsing.Common
import com.siliconlabs.bledemo.bluetooth.parsing.Engine
import com.welie.blessed.*
import fm.feed.android.playersdk.AvailabilityListener
import fm.feed.android.playersdk.FeedAudioPlayer
import fm.feed.android.playersdk.FeedPlayerService
import fm.feed.android.playersdk.PlayListener
import fm.feed.android.playersdk.models.Play
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.util.*
import kotlin.math.round


class VideoPlayerActivity : AppCompatActivity(), Caster.OnConnectChangeListener,
    ClassCoverEquipmentAdapter.EquipmentAdapterClickListener {
    private var handler = Handler()
    private lateinit var updater: Runnable
    private var totalDurationInLong: Long = 0
    private lateinit var seekbar: com.google.android.exoplayer2.ui.DefaultTimeBar
    private lateinit var title: TextView
    private lateinit var remaining: TextView
    private lateinit var state: TextView
    private lateinit var totalDuration: TextView
    private lateinit var currentPosition: TextView
    private var durationSet: Boolean = false
    var player: ExoPlayer? = null
    var repsCount = 0;
    var selectedDevicePosition = -1
    private var caster: Caster? = null
    private lateinit var feedAudioPlayer: FeedAudioPlayer
    private var feedFmPaused = false;
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var centralManager: BluetoothCentralManager;
    private lateinit var field: Field
    var averageRSSI: HashMap<String, Triple<Int, Int, BluetoothPeripheral>> = HashMap()
    private lateinit var litAxisDevicePair: LitAxisDevicePair;
    private var yourEquipmentAdapter: ClassCoverEquipmentAdapter? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        litAxisDevicePair = LitAxisDevicePair();
        centralManager = BluetoothCentralManager(applicationContext)
        LitDeviceConstants.mBleCentralManager = centralManager
        handleBLEConnectionObserver()
        yourEquipmentAdapter =
            ClassCoverEquipmentAdapter(this@VideoPlayerActivity, LitVideoPlayerSDK.dataList)
        caster = Caster.create(this);
        caster?.addMiniController()
        val mediaRouteButton =
            findViewById<MediaRouteButton>(R.id.media_route_button) as MediaRouteButton
        mediaRouteButton.setRemoteIndicatorDrawable(
            ContextCompat.getDrawable(
                this, R.drawable.ic_baseline_cast_24
            )
        )
        caster?.setupMediaRouteButton(mediaRouteButton, true)
        if (caster?.isConnected!!) {
            val mediaData = MediaData.Builder(LitVideoPlayerSDK.streamingUrl)
                .setStreamType(MediaData.STREAM_TYPE_BUFFERED)
                .setContentType("application/x-mpegURL") // Or "videos/mp4"... or any supported content type
                .setMediaType(MediaData.MEDIA_TYPE_MOVIE).setTitle("Two birds, many stones.")
                .setDescription("Isaac searches for Rebekah to retrieve Arachnid's stolen XP.")
                .setThumbnailUrl("...").setPlaybackRate(MediaData.PLAYBACK_RATE_NORMAL)
                .setAutoPlay(true).build()

            caster!!.player.loadMediaAndPlay(mediaData)
        }
        caster?.setOnConnectChangeListener(this)

        seekbar = findViewById<com.google.android.exoplayer2.ui.DefaultTimeBar>(R.id.exo_progress)
        totalDuration = findViewById<TextView>(R.id.totalDuration)
        currentPosition = findViewById<TextView>(R.id.currentPosition)
        title = findViewById<TextView>(R.id.videoTitle)
        remaining = findViewById<TextView>(R.id.remaining)
        state = findViewById<TextView>(R.id.state)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        var styleExoPlayer = findViewById<StyledPlayerView>(R.id.exo_player_style)
        player = ExoPlayer.Builder(this).build()
        val castContext = CastContext.getSharedInstance(this)
        FeedPlayerService.initialize(
            this@VideoPlayerActivity,
            "b288683241dfda2ce056fe3357c3038817c8094c",
            "802c87feaa9a61bfe72d99065860037fbb77b0a1"
        )
        styleExoPlayer.setPlayer(player);
        val mediaItem: MediaItem = MediaItem.fromUri(LitVideoPlayerSDK.streamingUrl)
        player!!.addMediaItem(mediaItem)
        player!!.prepare()
        seekbarFeature()
        player!!.playWhenReady = true
        var staticList = ArrayList<StatisticDataModel>()
        staticList.add(StatisticDataModel("TUT", "- -", R.mipmap.ic_launcher))
        staticList.add(StatisticDataModel("LBS", "- -", R.mipmap.ic_launcher_round))
        staticList.add(StatisticDataModel("REPS", "- -", R.mipmap.ic_launcher_round))
        staticList.add(StatisticDataModel("Heart Rate", "- -", R.drawable.ic_baseline_favorite_24))


        title.text = LitVideoPlayerSDK.videoTitle

        Glide.with(this).load(LitVideoPlayerSDK.targetAreaImage)
            .into(findViewById<ImageView>(R.id.iv_target_muscle))

        player!!.addListener(object : Player.Listener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState === ExoPlayer.STATE_READY && !durationSet) {
                    durationSet = true
                    totalDuration.text = millisecondToTimer(player!!.duration)
                    totalDurationInLong = player!!.duration
                }
                if (playWhenReady) {
                    state.text = "Playing"
                    updateTimer()
                }
            }
        })


        // updateTimer()
        val playAndPause: ImageView = findViewById<ImageView>(R.id.playAndPause)
        playAndPause.setOnClickListener {
            if (player!!.isPlaying) {
                playAndPause.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_play_arrow))
                state.text = "Paused"
                player!!.pause()
                feedAudioPlayer.pause()
            } else {
                playAndPause.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_pause))
                player!!.play()
                state.text = "Playing"
                updateTimer()
                feedAudioPlayer.play()

            }
        }


        val bluetoothButton = findViewById<ImageView>(R.id.bluetoothButton)
        val displayMetrics: DisplayMetrics = this.getResources().getDisplayMetrics()
        val dpHeight: Float = displayMetrics.heightPixels / displayMetrics.density
        val dpWidth: Float = displayMetrics.widthPixels / displayMetrics.density

        bluetoothButton.setOnClickListener {
            val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
            val view = layoutInflater.inflate(R.layout.custom_dialog_bluetooth, null)
            val cancelBtn = view.findViewById<TextView>(R.id.bt_cancel)
            val recyclerView = view.findViewById<RecyclerView>(R.id.device_recycler_view)
            yourEquipmentAdapter?.setAdapterListener(this@VideoPlayerActivity)
            recyclerView.adapter = yourEquipmentAdapter
            recyclerView.layoutManager = LinearLayoutManager(this@VideoPlayerActivity)

            val lp = WindowManager.LayoutParams()
            lp.copyFrom(builder.getWindow()?.getAttributes())
            lp.width = round(dpWidth / 2.7).toInt()
            lp.height = round(dpHeight / 1.1).toInt()
            builder.setView(view)
            builder.setCanceledOnTouchOutside(false)
            val wmlp: WindowManager.LayoutParams = builder.getWindow()!!.getAttributes()
            wmlp.x = (170 * displayMetrics.density).toInt()//x position
            builder.show()
            val width = round(displayMetrics.widthPixels / 2.8).toInt()
            val height = round(displayMetrics.heightPixels / 1.3).toInt()
            builder.getWindow()?.setLayout(width, height)

            cancelBtn.setOnClickListener {
                builder.dismiss()
            }
        }
//        LitVideoPlayerSDK.heartRate?.observe(this, Observer {
//            staticList.get(3).value = it?.parameterValue.toString()
//        }

        var progress_view = findViewById<View>(R.id.progress)

        val soundButton = findViewById<ImageView>(R.id.soundButton)
        var audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        soundButton.setOnClickListener {
            val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
            val view = layoutInflater.inflate(R.layout.custom_dialog_sound, null)
            val defaultMix = view.findViewById<TextView>(R.id.tv_originalMix)
            val doneBtn = view.findViewById<TextView>(R.id.tv_done)
            val instructorSeekbar = view.findViewById<SeekBar>(R.id.seekbarForInstructor)
            val musicSeekbar = view.findViewById<SeekBar>(R.id.seekbarForMusic)
            val musicVolumeLabel = view.findViewById<TextView>(R.id.tv_music_volume)
            var instructorVolume = view.findViewById<TextView>(R.id.tv_instructor_volume)

            musicSeekbar.progress = 7
            instructorSeekbar.progress = 100
            instructorVolume.text = "100%"
            musicVolumeLabel.text = "70%"

            doneBtn.setOnClickListener {
                builder.dismiss()
            }

            defaultMix.setOnClickListener {
                musicSeekbar.setProgress(70, true)
                instructorSeekbar.setProgress(100, true)
            }


            instructorSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    player!!.volume = p1.toFloat()
                    instructorVolume.text = "${(p1 * 4)}%"
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, p1, 0);
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            })

            musicSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    feedAudioPlayer.setVolume(p1.toFloat())
                    musicVolumeLabel.text = "${(p1 * 4)}%"
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }

            })
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(builder.getWindow()?.getAttributes())
            lp.width = 300
            lp.height = 300
            builder.setView(view)
            builder.setCanceledOnTouchOutside(false)
            val wmlp: WindowManager.LayoutParams = builder.getWindow()!!.getAttributes()
            wmlp.x = (170 * displayMetrics.density).toInt()//x position
            builder.show()
            val width = round(displayMetrics.widthPixels / 2.8).toInt()
            val height = round(displayMetrics.heightPixels / 1.4).toInt()
            builder.getWindow()?.setLayout(width, height)
        }

        //totalDuration.text = millisecondToTimer(player!!.duration)

        LitVideoPlayerSDK.heartRateObservable!!.observe(this, Observer {
            findViewById<TextView>(R.id.tv_heart_rate).text = it?.parameterValue.toString()
            ProcessedData.calculateAverageHeartRate(it.parameterValue!!.toInt())

        })

        LitVideoPlayerSDK.timeUnderTensionObserver!!.observe(this, Observer {
            runOnUiThread {
                findViewById<TextView>(R.id.tv_tut_value).text =
                    (it?.first!! + it?.second!!).toString()
            }
        })

//

        LitVideoPlayerSDK.weightObservable?.observe(this) {
            runOnUiThread {
                findViewById<TextView>(R.id.tv_lbs_value).text = (it).toString()
            }
        }

        LitVideoPlayerSDK.resistanceObservable.observe(this) {

            var progressSize = 8.0;
            if (it.first < 9.0) {
                progressSize = it.first * 1.0
            }
            runOnUiThread {
                val dimensionInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    (27.5 * progressSize).toFloat(),
                    resources.displayMetrics
                ).toInt()

                slideView(view = progress_view, 0, dimensionInDp)
                progress_view.layoutParams.width = dimensionInDp
                findViewById<TextView>(R.id.tv_lbs_value).text = it.second.toString()
                if (it.second > 3) {
                    repsCount++;
                    findViewById<TextView>(R.id.tv_reps_value).text = repsCount.toString()
                }
                findViewById<TextView>(R.id.tv_resistance_value).text = it.first.toString()


            }
        }

        val layoutForEnd = findViewById<RelativeLayout>(R.id.layoutForEnd)
        layoutForEnd.setOnClickListener {
            if (player!!.isPlaying) {
                player?.pause()
            }
            if (!feedFmPaused) {
                feedAudioPlayer.pause()
                feedFmPaused = true
            }

            finish()

        }

        player!!.addListener(object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {

            }

            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                super.onTimelineChanged(timeline, reason)
                if (player!!.isPlaying) {
                    Log.d("Player Current Position", (player!!.currentPosition / 1000).toString())
                }
            }
        })

        FeedPlayerService.getInstance(object : AvailabilityListener {
            override fun onPlayerAvailable(player: FeedAudioPlayer) {
                feedAudioPlayer = player
                var station = player.stationList[0]
                player.setActiveStation(station, true)
                player.play()

                feedAudioPlayer.addPlayListener(object : PlayListener {
                    override fun onPlayStarted(play: Play?) {
                        findViewById<TextView>(R.id.tv_singer).text = play?.audioFile?.artist?.name
                        findViewById<TextView>(R.id.tv_tack_name).text =
                            play?.audioFile?.release?.title
                    }

                    override fun onProgressUpdate(play: Play, elapsedTime: Float, duration: Float) {

                    }

                    override fun onSkipStatusChanged(status: Boolean) {

                    }

                })
            }

            override fun onPlayerUnavailable(e: Exception) {
            }

        })

        caster!!.setOnCastSessionStateChanged(object : OnCastSessionStateChanged {
            override fun onCastSessionBegan() {
                Log.e("Caster", "Began playing video")
                player!!.play()
            }

            override fun onCastSessionFinished() {
                Log.e("Caster", "Finished playing video")
            }

            override fun onCastSessionPlaying() {
                val playingURL = caster!!.player.currentPlayingMediaUrl
                player!!.play()

                Log.e("Caster", "Playing video")
            }

            override fun onCastSessionPaused() {
                Log.e("Caster", "Paused video")
            }
        })


    }


    fun seekbarFeature() {
        findViewById<DefaultTimeBar>(com.google.android.exoplayer2.ui.R.id.exo_progress).addListener(
            object : TimeBar.OnScrubListener {
                override fun onScrubStart(timeBar: TimeBar, position: Long) {
                    player!!.pause()
                }

                override fun onScrubMove(timeBar: TimeBar, position: Long) {
                    player!!.seekTo(position)
                }

                override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                    player!!.play()
                }

            })
    }

    fun showRatingPopup() {

    }

    override fun onPause() {
        super.onPause()
        player!!.pause()
        feedFmPaused = true
        feedAudioPlayer.pause()
    }

    private fun millisecondToTimer(milliseconds: Long): String {
        var timerString: String = ""
        val secondString: String
        val hours: Int = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes: Int = ((milliseconds % (1000 * 60 * 60)) / (1000 * 60)).toInt()
        val seconds: Int = ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000).toInt()

        if (hours > 0) {
            timerString = "$hours:"
        }
        if (seconds < 10) {
            secondString = "0$seconds"
        } else {
            secondString = "" + seconds
        }
        timerString = "$timerString$minutes:$secondString"
        return timerString

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTimer() {
        if (player!!.isPlaying) {
            currentPosition.text = millisecondToTimer(player!!.currentPosition)
            videoPlaybackTimer.postValue(player?.currentPosition)
            var timeInHours = (player!!.currentPosition / 1000.0) / 3600
            findViewById<TextView>(R.id.tv_kcal).text =
                ProcessedData.calculateCaloriesBurnt(timeInHours).second.toString()
            remaining.text =
                millisecondToTimer(totalDurationInLong - player!!.currentPosition) + " Remaining"
            updater = Runnable {
                updateTimer()
            }
            handler.postDelayed(updater, 1000)
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        try {
            handler.removeCallbacks(updater)
        } catch (e: java.lang.Exception) {

        }
    }

    private fun slideView(
        view: View, currentHeight: Int, newHeight: Int
    ) {

        var slideAnimator = ValueAnimator.ofInt(currentHeight, newHeight).setDuration(300);

        slideAnimator.addUpdateListener() {
            var value = it.animatedValue;
            view.getLayoutParams().width = value as Int
            view.requestLayout();
        };

        /*  We use an animationSet to play the animation  */

        var animationSet = AnimatorSet();
        animationSet.setInterpolator(AccelerateDecelerateInterpolator());
        animationSet.play(slideAnimator);
        animationSet.start()

        var slideAnimator2 = ValueAnimator.ofInt(newHeight, 0).setDuration(300);

        slideAnimator2.addUpdateListener() {
            var value = it.animatedValue;
            view.getLayoutParams().width = value as Int
            view.requestLayout();
        };

        var animationSet2 = AnimatorSet();
        animationSet2.setInterpolator(AccelerateDecelerateInterpolator());
        animationSet2.play(slideAnimator2);
        animationSet2.start()
        runOnUiThread {
            findViewById<TextView>(R.id.tv_resistance_value).text = "0"
        }
    }

    override fun onResume() {
        super.onResume()
        if (feedFmPaused) {
            feedAudioPlayer.play()
            feedFmPaused = false
        }
    }

    override fun onConnected() {
        Log.d("Caster", "Connected with Chromecast");
    }

    override fun onDisconnected() {
        Log.d("Caster", "Disconnected from Chromecast");
    }


    private fun observeLitData() {

        var notificationLeftDevice = mLitAxisDevicePair.leftLitAxisDevice?.getCharacteristic(
            LIT_AXIS_WEIGHT_SCALE_SERVICE, LIT_AXIS_WEIGHT_SCALE_CHARACHTERISTIC
        )

        var notificationRightDevice = mLitAxisDevicePair.rightLitAxisDevice?.getCharacteristic(
            LIT_AXIS_WEIGHT_SCALE_SERVICE, LIT_AXIS_WEIGHT_SCALE_CHARACHTERISTIC
        )

        notificationLeftDevice?.let {
            scope.launch {
                mLitAxisDevicePair.leftLitAxisDevice?.observe(it) { value ->
                    Log.d("Data Size", value.size.toString())
                    "Received : $value".also {
                        dataParser()
                        val data = readValue(value)
                        runOnUiThread() {
                            RepsCalculator.leftBandActivity((data.toFloat() * 0.005))
                        }

                    }
                }
            }
        }

        notificationRightDevice?.let {
            scope.launch {
                mLitAxisDevicePair.rightLitAxisDevice?.observe(it) { value ->
                    Log.d("Data Size", value.size.toString())
                    "Received : $value".also {
                        dataParser()
                        val data = readValue(value)
                        runOnUiThread() {
                            RepsCalculator.rightBandActivity((data.toFloat() * 0.005))
                        }
                    }
                }
            }
        }


    }


    private fun observeHrData() {
        if (mHeartRateMonitorPeripheral != null) {
            LitVideoPlayerSDK.heartRateObservable = MutableLiveData()

            var notifyingCharacteristic = mHeartRateMonitorPeripheral.getCharacteristic(
                LIT_HEART_RATE_SERVICE, HEART_RATE_CHARACTERISTIC_UUID
            )

            notifyingCharacteristic?.let {
                scope.launch {
                    mHeartRateMonitorPeripheral.observe(it) { value ->
                        HeartRateMeasurement.fromBytes(value).toString()
                        LitVideoPlayerSDK.heartRateObservable!!.postValue(
                            DeviceDataCalculated(
                                "Hear Rate",
                                HeartRateMeasurement.fromBytes(value).sensorContactStatus.toString() == "SupportedAndContacted",
                                HeartRateMeasurement.fromBytes(value).createdAt.toString(),
                                "HeartRateType",
                                HeartRateMeasurement.fromBytes(value).pulse.toString(),
                            )
                        )
                    }
                }
            }
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
                            1, scanResult.rssi, peripheral
                        )
                    )
                } else {
                    var dataPayload = averageRSSI.getValue(peripheral.address)
                    var rssi = dataPayload.second
                    var count = dataPayload.first
                    var average = (rssi * count + scanResult.rssi) / (count + 1)
                    var updatedPacket = Triple(count + 1, average, peripheral)
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
                    this, "Connected with Left Lit AXIS sensor", Toast.LENGTH_SHORT
                ).show()
            }

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
                                this, "Connected with Right Lit AXIS sensor", Toast.LENGTH_SHORT
                            ).show()
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
            centralManager.stopScan()
            LIT_AXIS_CONNECTION_STATE = "CONNECTED"
            runOnUiThread() {
                LitVideoPlayerSDK.dataList.get(
                    selectedDevicePosition
                ).connectionStatus = true
                yourEquipmentAdapter?.notifyItemChanged(selectedDevicePosition)
                observeLitData()

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
        mLitAxisDevicePair = litAxisDevicePair
//        handleLitAxisConnection(positionClicked)

    }


    private fun handleBLEConnectionObserver() {

        centralManager.observeConnectionState { peripheral, state ->
            Log.d("Peripheral", " ${peripheral.address} has $state")
            if (state.toString() == "CONNECTED") {

                if (peripheral.getService(LIT_HEART_RATE_SERVICE) != null) {
                    centralManager.stopScan()
                    mBleCentralManager = centralManager
                    mHeartRateMonitorPeripheral = peripheral
                    HR_CONNECTION_STATE = "CONNECTED"
                    runOnUiThread() {
                        Toast.makeText(this, "Connected with heart rate sensor", Toast.LENGTH_SHORT)
                            .show()
                        observeHrData()

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

                    if (peripheral.getService(LIT_HEART_RATE_SERVICE) != null) {
                        Log.d("Disconnection", "HR sensor disconnected")
                        HR_CONNECTION_STATE = "DISCONNECTED"
                        handleHeartRateConnection()
                    } else {
                        Log.d("Disconnection", "Lit Axis disconnected")
                        updateLitAxisConnectionPair(peripheral)
                        LIT_AXIS_CONNECTION_STATE = "DISCONNECTED"
//                        runOnUiThread() {
//                            dataList[positionClicked].connectionStatus = false
//                            yourEquipmentAdapter?.notifyItemChanged(positionClicked)
//                        }
                    }
                }

            }
        }
    }


    private fun handleHeartRateConnection() {
        var list = arrayOf<UUID>(LIT_HEART_RATE_SERVICE)
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
                            1, scanResult.rssi, peripheral
                        )
                    )
                } else {
                    var dataPayload = averageRSSI.getValue(peripheral.address)
                    var rssi = dataPayload.second
                    var count = dataPayload.first
                    var average = (rssi * count + scanResult.rssi) / (count + 1)
                    var updatedPacket = Triple(count + 1, average, peripheral)
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

        var list = arrayOf<UUID>(LIT_AXIS_WEIGHT_SCALE_SERVICE)
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

    private fun readValue(fieldValue: ByteArray): String {
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
                field.isFullByteSintFormat() -> convertSintToString(fieldValue)
                field.isFullByteUintFormat() -> convertUintToString(formatLength, fieldValue)
                field.isFloatFormat() -> {
                    val fValue = readFloat(format, formatLength, fieldValue)
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

    private fun convertSintToString(fieldValue: ByteArray): String {
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

    private fun convertUintToString(formatLength: Int, fieldValue: ByteArray): String {
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

    private fun readFloat(format: String, formatLength: Int, fieldValue: ByteArray): Double {
        var result = 0.0
        when (format) {
            TYPE_SFLOAT -> result = Common.readSfloat(fieldValue).toDouble()
            TYPE_FLOAT -> result = Common.readFloat(fieldValue, 0, formatLength - 1).toDouble()
            TYPE_FLOAT_32 -> result = Common.readFloat32(fieldValue).toDouble()
            TYPE_FLOAT_64 -> result = Common.readFloat64(fieldValue)
        }
        return result
    }

    override fun onItemEquipClick(position: Int, data: String) {
        Log.d("Hello", "Clicked Item")
        LitVideoPlayerSDK.dataList[position].selectedItem =
            !LitVideoPlayerSDK.dataList[position].selectedItem!!
        yourEquipmentAdapter?.notifyItemChanged(position)
        selectedDevicePosition = position;
        LitVideoPlayerSDK.dataList.get(position)
        if (LitVideoPlayerSDK.dataList.get(position).id == "0x180D") {
            connectWithHrSensor(position)
        }
        if (LitVideoPlayerSDK.dataList.get(position).id == "0x181D") {
            handleLitAxisConnection(position)
        }
    }
}
