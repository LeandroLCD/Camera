package com.leandrolcd.camera.ui.oneCamera

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import com.leandrolcd.camera.ui.widgets.videoPlayer.VideoPlayer
import com.leandrolcd.presentation.device.OneCameraViewModel


class OneCamera(val deviceId: Int) :Screen{
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<OneCameraViewModel>()
        val device by viewModel.device.collectAsState()
        val streamUrl by viewModel.urlStream.collectAsState()
        LaunchedEffect(device) {
            viewModel.load(deviceId)
            if(device!=null){
                viewModel.connectDevice(device!!)
            }
        }
        ConstraintLayout(modifier = Modifier.fillMaxSize()){
            val (camera) = createRefs()
            Box(modifier = Modifier.constrainAs(camera) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }){
                VideoPlayer(video =streamUrl,
                    title = device?.alias.orEmpty(), modifier = Modifier) {



                }

            }
        }
    }
}