package com.aaqanddev.youtubeplayer

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

const val YOUTUBE_VIDEO_ID = "5jjMzWBMJ1s"
const val YOUTUBE_PLAYLIST = "PL9jCwTXYWjDJAthCsXmP1x2b6K6xFJOIW"

class YoutubeActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {


    private val TAG = "YoutubeActivity"
    private val DIALOG_REQUEST_CODE = 1

    val playerView by lazy { YouTubePlayerView(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_youtube)
//        val layout = findViewById<ConstraintLayout>(R.id.activity_youtube)
        val layout = layoutInflater.inflate(R.layout.activity_youtube, null) as ConstraintLayout
        setContentView(layout)

//        val button1 = Button(this)
//        button1.layoutParams = ConstraintLayout.LayoutParams(600, 180)
//        button1.text = "Button added"
//        layout.addView(button1)

        playerView.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layout.addView(playerView)
        playerView.initialize(getString(R.string.GOOGLE_API_KEY), this)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        Log.d(TAG, "onInitSuccess: privder is ${provider?.javaClass}")
        Log.d(TAG, "onInitSuccess: player is ${player?.javaClass}")
        Toast.makeText(this, "Initialized player successfully", Toast.LENGTH_SHORT).show()

        player?.setPlayerStateChangeListener(playerStateChangeListener)
        player?.setPlaybackEventListener(playbackEventListener)

        player?.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION)
        player?.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE)
        if (!wasRestored){
            player?.loadVideo(YOUTUBE_VIDEO_ID)
        } else{
            player?.play()
        }

    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        youtubeInitResult: YouTubeInitializationResult?
    ) {


        if (youtubeInitResult?.isUserRecoverableError == true){
            youtubeInitResult.getErrorDialog(this, DIALOG_REQUEST_CODE).show()
        } else{
            val errorMessage = "There was an error initializing the YouTube Player ($youtubeInitResult)"
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private val playbackEventListener = object: YouTubePlayer.PlaybackEventListener{
        override fun onSeekTo(p0: Int) {

        }

        override fun onBuffering(p0: Boolean) {

        }

        override fun onPlaying() {
            Toast.makeText(this@YoutubeActivity, "Good, video playing ok", Toast.LENGTH_SHORT ).show()
        }

        override fun onStopped() {
            Toast.makeText(this@YoutubeActivity, "video has stopped", Toast.LENGTH_SHORT).show()
        }

        override fun onPaused() {
            Toast.makeText(this@YoutubeActivity, "video has paused", Toast.LENGTH_SHORT).show()
        }
    }

    private val playerStateChangeListener = object: YouTubePlayer.PlayerStateChangeListener{
        override fun onAdStarted() {
            Toast.makeText(this@YoutubeActivity, "Click ad now, make the video creator rich", Toast.LENGTH_SHORT).show()
        }

        override fun onLoading() {

        }

        override fun onVideoStarted() {
            Toast.makeText(this@YoutubeActivity, "video has started", Toast.LENGTH_SHORT).show()
        }

        override fun onLoaded(p0: String?) {
        }

        override fun onVideoEnded() {
            Toast.makeText(this@YoutubeActivity,"Congrats! You've completed another video", Toast.LENGTH_SHORT).show()
        }

        override fun onError(p0: YouTubePlayer.ErrorReason?) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult called with response code $resultCode for request $requestCode")

        if (requestCode == DIALOG_REQUEST_CODE){
            Log.d(TAG, intent?.toString())
            Log.d(TAG, intent?.extras.toString())
            try{
            playerView.initialize(getString(R.string.GOOGLE_API_KEY), this)

            } catch (e: ActivityNotFoundException){
                e.printStackTrace()
                Log.d(TAG, "activity not found ${e.message}")
            }

        }
    }
}
