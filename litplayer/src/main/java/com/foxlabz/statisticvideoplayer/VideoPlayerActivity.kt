package com.foxlabz.statisticvideoplayer

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
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
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.ui.TimeBar
import kotlin.math.round

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        var styleExoPlayer = findViewById<StyledPlayerView>(R.id.exo_player_style)
        player = ExoPlayer.Builder(this).build()

        styleExoPlayer.setPlayer(player);
        val mediaItem: MediaItem = MediaItem.fromUri(LitVideoPlayerSDK.streamingUrl)
        player.addMediaItem(mediaItem)
        player.prepare()
        seekbarFeature()
        player.playWhenReady = true
        var staticList = ArrayList<StatisticDataModel>()
        staticList.add(StatisticDataModel("TUT", "- -", R.mipmap.ic_launcher))
        staticList.add(StatisticDataModel("LBS", "- -", R.mipmap.ic_launcher_round))
        staticList.add(StatisticDataModel("REPS", "- -", R.mipmap.ic_launcher_round))
        staticList.add(StatisticDataModel("Heart Rate", "- -", R.drawable.ic_baseline_favorite_24))
        var recyclerView = findViewById<RecyclerView>(R.id.rv_statistics)
        var adapter = StatisticRecyclerView(staticList, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

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
                    player.volume = p1.toFloat()
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


        LitVideoPlayerSDK.heartRate.observe(this, Observer {
            staticList.get(3).value = it?.parameterValue.toString()
            adapter.notifyItemChanged(3)
        })


        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {

            }

            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                super.onTimelineChanged(timeline, reason)
                if (player.isPlaying) {
                    Log.d("Player Current Position", (player.currentPosition / 1000).toString())
                }
            }
        })

    }


    fun seekbarFeature(){
        findViewById<DefaultTimeBar>(com.google.android.exoplayer2.ui.R.id.exo_progress).addListener(object:TimeBar.OnScrubListener{
            override fun onScrubStart(timeBar: TimeBar, position: Long) {
                player.pause()
            }

            override fun onScrubMove(timeBar: TimeBar, position: Long) {
                player.seekTo(position)
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                player.play()
            }

        })
    }


    override fun onPause() {
        super.onPause()
        player.pause()
    }
}