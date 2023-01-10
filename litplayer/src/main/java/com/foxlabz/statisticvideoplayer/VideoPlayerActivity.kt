package com.foxlabz.statisticvideoplayer

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
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
import androidx.lifecycle.Observer
import androidx.mediarouter.app.MediaRouteButton
import com.bumptech.glide.Glide
import com.foxlabz.statisticvideoplayer.LitVideoPlayerSDK.videoPlaybackTimer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.ui.TimeBar
import com.google.android.gms.cast.framework.CastContext
import com.mradzinski.caster.Caster
import com.mradzinski.caster.Caster.OnCastSessionStateChanged
import com.mradzinski.caster.MediaData
import fm.feed.android.playersdk.AvailabilityListener
import fm.feed.android.playersdk.FeedAudioPlayer
import fm.feed.android.playersdk.FeedPlayerService
import fm.feed.android.playersdk.PlayListener
import fm.feed.android.playersdk.models.Play
import kotlin.math.round


class VideoPlayerActivity : AppCompatActivity(), Caster.OnConnectChangeListener {
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
    private var caster: Caster? = null
    private lateinit var feedAudioPlayer: FeedAudioPlayer
    private var feedFmPaused = false;

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        caster = Caster.create(this);
        caster?.addMiniController()
        val mediaRouteButton =
            findViewById<MediaRouteButton>(R.id.media_route_button) as MediaRouteButton
        mediaRouteButton.setRemoteIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_cast_24))
        caster?.setupMediaRouteButton(mediaRouteButton, true)
        if (caster?.isConnected!!) {
            val mediaData = MediaData.Builder(LitVideoPlayerSDK.streamingUrl)
                .setStreamType(MediaData.STREAM_TYPE_BUFFERED)
                .setContentType("application/x-mpegURL") // Or "videos/mp4"... or any supported content type
                .setMediaType(MediaData.MEDIA_TYPE_MOVIE)
                .setTitle("Two birds, many stones.")
                .setDescription("Isaac searches for Rebekah to retrieve Arachnid's stolen XP.")
                .setThumbnailUrl("...")
                .setPlaybackRate(MediaData.PLAYBACK_RATE_NORMAL)
                .setAutoPlay(true)
                .build()

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
            val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .create()
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
                findViewById<TextView>(R.id.tv_lbs_value).text =
                    (it).toString()
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
            Toast.makeText(this, "End Session", Toast.LENGTH_SHORT).show()
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

                feedAudioPlayer.addPlayListener(object :PlayListener{
                    override fun onPlayStarted(play: Play?) {
                        findViewById<TextView>(R.id.tv_singer).text=play?.audioFile?.artist?.name
                        findViewById<TextView>(R.id.tv_tack_name).text=play?.audioFile?.release?.title
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
        handler.removeCallbacks(updater)

    }

    private fun slideView(
        view: View,
        currentHeight: Int,
        newHeight: Int
    ) {

        var slideAnimator = ValueAnimator.ofInt(currentHeight, newHeight)
            .setDuration(300);

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

        var slideAnimator2 = ValueAnimator.ofInt(newHeight, 0)
            .setDuration(300);

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
}
