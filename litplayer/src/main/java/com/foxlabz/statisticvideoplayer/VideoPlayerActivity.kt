package com.foxlabz.statisticvideoplayer

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.*
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

        bluetoothButton.setOnClickListener {
            val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .create()
            val view = layoutInflater.inflate(R.layout.custom_dialog_bluetooth, null)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(builder.getWindow()?.getAttributes())

            val displayMetrics: DisplayMetrics = this.getResources().getDisplayMetrics()
            val dpHeight: Float = displayMetrics.heightPixels / displayMetrics.density
            val dpWidth: Float = displayMetrics.widthPixels / displayMetrics.density
            lp.width = round(dpWidth / 2.5).toInt()
            lp.height = round(dpHeight / 1.2).toInt()
            builder.setView(view)
            builder.setCanceledOnTouchOutside(false)
            val wmlp: WindowManager.LayoutParams = builder.getWindow()!!.getAttributes()
            wmlp.x = (100 * displayMetrics.density).toInt()//x position
            builder.show()
            val width = round(displayMetrics.widthPixels / 2.2).toInt()
            val height = round(displayMetrics.heightPixels / 1.4).toInt()
            builder.getWindow()?.setLayout(width, height)
        }
        LitVideoPlayerSDK.heartRate?.observe(this, Observer {
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