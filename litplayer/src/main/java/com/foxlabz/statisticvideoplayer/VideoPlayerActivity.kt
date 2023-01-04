package com.foxlabz.statisticvideoplayer

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.ui.TimeBar
import kotlin.math.min
import kotlin.math.round

class VideoPlayerActivity : AppCompatActivity() {
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
//        var recyclerView = findViewById<RecyclerView>(R.id.rv_statistics)
//        var adapter = StatisticRecyclerView(staticList, this)
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        title.text = LitVideoPlayerSDK.videoTitle

        Glide.with(this).load("https://dev.assets.litmethod.com/MuscleGroup/SgM8Bp8T_Core.png")
            .into(findViewById<ImageView>(R.id.iv_target_muscle))

        player!!.addListener(object : Player.Listener {
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
            } else {
                playAndPause.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_pause))
                player!!.play()
                state.text = "Playing"
                updateTimer()
            }
        }


        val bluetoothButton = findViewById<ImageView>(R.id.bluetoothButton)
        val displayMetrics: DisplayMetrics = this.getResources().getDisplayMetrics()
        val dpHeight: Float = displayMetrics.heightPixels / displayMetrics.density
        val dpWidth: Float = displayMetrics.widthPixels / displayMetrics.density

        bluetoothButton.setOnClickListener {
            val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .create()
            val view = layoutInflater.inflate(R.layout.custom_dialog_bluetooth, null)
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
        }
//        LitVideoPlayerSDK.heartRate?.observe(this, Observer {
//            staticList.get(3).value = it?.parameterValue.toString()
//        }

        val soundButton = findViewById<ImageView>(R.id.soundButton)
        var audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        soundButton.setOnClickListener {
            val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .create()
            val view = layoutInflater.inflate(R.layout.custom_dialog_sound, null)
            val instructorSeekbar = view.findViewById<SeekBar>(R.id.seekbarForInstructor)
            var instructorVolume = view.findViewById<TextView>(R.id.tv_instructor_volume)

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

//        LitVideoPlayerSDK.heartRate!!.observe(this, Observer {
//            staticList.get(3).value = it?.parameterValue.toString()
//            adapter.notifyItemChanged(3)
//        })
//
//        LitVideoPlayerSDK.timeUnderTension!!.observe(this, Observer {
//            staticList.get(0).value = (min(it?.first!!, it?.second!!)).toString()
//            adapter.notifyItemChanged(0)
//        })

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

    }


    fun seekbarFeature(){
        findViewById<DefaultTimeBar>(com.google.android.exoplayer2.ui.R.id.exo_progress).addListener(object:TimeBar.OnScrubListener{
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


    override fun onPause() {
        super.onPause()
        player!!.pause()
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


    private fun updateTimer() {
        if (player!!.isPlaying) {
            currentPosition.text = millisecondToTimer(player!!.currentPosition)
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
        handler.removeCallbacks(updater)
    }
}
