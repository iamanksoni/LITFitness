package com.foxlabz.statisticvideoplayer

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView

class VideoPlayerActivity : AppCompatActivity() {
    private var handler = Handler()
    private lateinit var updater: Runnable
    private lateinit var seekbar: com.google.android.exoplayer2.ui.DefaultTimeBar
    private lateinit var totalDuration: TextView
    private lateinit var currentPosition: TextView
    var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekbar = findViewById<com.google.android.exoplayer2.ui.DefaultTimeBar>(R.id.exo_progress)
        totalDuration = findViewById<TextView>(R.id.totalDuration)
        currentPosition = findViewById<TextView>(R.id.currentPosition)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        var styleExoPlayer = findViewById<StyledPlayerView>(R.id.exo_player)
        player = ExoPlayer.Builder(this).build()
        styleExoPlayer.setPlayer(player);
        val mediaItem: MediaItem = MediaItem.fromUri(LitVideoPlayerSDK.streamingUrl)
        player!!.addMediaItem(mediaItem)
        player!!.prepare()
        player!!.playWhenReady = true
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

        player!!.addListener(object : Player.Listener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if(playWhenReady){
                    updateTimer()
                }
            }
        })



       // updateTimer()
        val playAndPause: ImageView = findViewById<ImageView>(R.id.playAndPause)
        playAndPause.setOnClickListener{
            if(player!!.isPlaying) {
                playAndPause.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_play_arrow))
                player!!.pause()
            }else{
                playAndPause.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_pause))
                player!!.play()
                updateTimer()
            }
        }






        val bluetoothButton = findViewById<ImageView>(R.id.bluetoothButton)

        bluetoothButton.setOnClickListener {
            val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .create()
            val view = layoutInflater.inflate(R.layout.custom_dialog_bluetooth, null)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(builder.getWindow()?.getAttributes())
            lp.width = 300
            lp.height = 300
            builder.setView(view)
            builder.setCanceledOnTouchOutside(false)
            val wmlp: WindowManager.LayoutParams = builder.getWindow()!!.getAttributes()
            wmlp.x = 200 //x position
            builder.show()
            val width = (500).toInt()
            val height = (500).toInt()
            builder.getWindow()?.setLayout(width, height)
        }


        val soundButton = findViewById<ImageView>(R.id.soundButton)

        soundButton.setOnClickListener {
            val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .create()
            val view = layoutInflater.inflate(R.layout.custom_dialog_sound, null)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(builder.getWindow()?.getAttributes())
            lp.width = 300
            lp.height = 300
            builder.setView(view)
            builder.setCanceledOnTouchOutside(false)
            val wmlp: WindowManager.LayoutParams = builder.getWindow()!!.getAttributes()
            wmlp.x = 200 //x position
            builder.show()
            val width = (500).toInt()
            val height = (500).toInt()
            builder.getWindow()?.setLayout(width, height)
        }

        totalDuration.text = millisecondToTimer(player!!.contentDuration)



        LitVideoPlayerSDK.heartRate?.observe(this, Observer {
            staticList.get(3).value = it.parameterValue.toString()
            adapter.notifyItemChanged(3)
        })

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



    private fun updateTimer(){
        if (player!!.isPlaying) {
            currentPosition.text = millisecondToTimer(player!!.currentPosition)
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
