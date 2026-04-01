package com.leandrolcd.camera.ui.widgets.videoPlayer

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@androidx.annotation.OptIn(UnstableApi::class)
@SuppressLint("OpaqueUnitKey")
@ExperimentalAnimationApi
@Composable
fun VideoPlayer(
    video: String,
    title:String,
    modifier: Modifier = Modifier,
    onVideoChange: () -> Unit
) {
    // Get the current context
    val context = LocalContext.current
    (context as? ComponentActivity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND
    // Mutable state to control the visibility of the video title
    val visible = remember { mutableStateOf(true) }

    // Mutable state to hold the video title
    val videoTitle = remember { mutableStateOf(title) }

    // Create a list of MediaItems for the ExoPlayer
    val mediaItem = MediaItem.Builder()
            .setUri(video)
            .setTag(title)
            .setMediaMetadata(MediaMetadata.Builder().setDisplayTitle(title).build())
            .build()
    val mediaSourceFactory = DefaultMediaSourceFactory(context)
    val rtspMediaSource = RtspMediaSource.Factory().createMediaSource(mediaItem)


    // Initialize ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaSource(rtspMediaSource)
            prepare()
            this.addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    // Hide video title after playing for 200 milliseconds
                    if (player.contentPosition >= 200) visible.value = false
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    // Callback when the video changes
                    onVideoChange()
                    visible.value = true
                    videoTitle.value = mediaItem?.mediaMetadata?.displayTitle.toString()
                }
            })
        }
    }


    exoPlayer.playWhenReady = true

    // Add a lifecycle observer to manage player state based on lifecycle events
    LocalLifecycleOwner.current.lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_START -> {
                    // Start playing when the Composable is in the foreground
                    if (exoPlayer.isPlaying.not()) {
                        exoPlayer.play()
                    }
                }

                Lifecycle.Event.ON_STOP -> {
                    // Pause the player when the Composable is in the background
                    exoPlayer.pause()
                }

                else -> {
                    // Nothing
                }
            }
        }
    })

    // Column Composable to contain the video player
    Column(modifier = modifier.background(Color.Black)) {
        // DisposableEffect to release the ExoPlayer when the Composable is disposed
        DisposableEffect(
            AndroidView(
                modifier = modifier,
                factory = {
                    // AndroidView to embed a PlayerView into Compose
                    PlayerView(context).apply {
                        player = exoPlayer
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        // Set resize mode to fill the available space
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                        // Hide unnecessary player controls
                        setShowNextButton(false)
                        setShowPreviousButton(false)
                        setShowFastForwardButton(false)
                        setShowRewindButton(false)
                    }
                })
        ) {
            // Dispose the ExoPlayer when the Composable is disposed
            onDispose {
                exoPlayer.release()
            }
        }
    }
}