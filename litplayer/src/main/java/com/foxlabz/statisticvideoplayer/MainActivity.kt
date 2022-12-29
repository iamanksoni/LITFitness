package com.foxlabz.statisticvideoplayer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView

class MainActivity : AppCompatActivity() {


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
        var recyclerView = findViewById<RecyclerView>(R.id.rv_statistics)
        var adapter = StatisticRecyclerView(staticList, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }
}