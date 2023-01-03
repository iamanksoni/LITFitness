package com.foxlabz.statisticvideoplayer

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView

class VideoPlayerActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        var styleExoPlayer = findViewById<StyledPlayerView>(R.id.exo_player)
        val player = ExoPlayer.Builder(this).build()
        styleExoPlayer.setPlayer(player);
        val mediaItem: MediaItem = MediaItem.fromUri(LitVideoPlayerSDK.streamingUrl)
        player.addMediaItem(mediaItem)
        player.prepare()
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
        LitVideoPlayerSDK.heartRate.observe(this, Observer {
            staticList.get(3).value = it.parameterValue.toString()
            adapter.notifyItemChanged(3)
        })

    }
}