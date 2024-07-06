package com.leandrolcd.camera.ui.addDevice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.leandrolcd.camera.R
import com.leandrolcd.presentation.device.AddDeviceViewModel
import com.leandrolcd.presentation.device.model.DeviceInfo
import com.leandrolcd.presentation.device.states.AddDeviceUiState
import com.leandrolcd.presentation.device.states.DialogUiState

class AddDevice : Screen {
    @Composable
    override fun Content() {
        val viewModel: AddDeviceViewModel = hiltViewModel()

        val deviceList by viewModel.deviceList.collectAsState()

        val uiState by viewModel.uiState.collectAsState(AddDeviceUiState.Loaded)

        val dialogUiState by viewModel.dialogUiState.collectAsState(DialogUiState.None)
        Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
            Surface(modifier = Modifier.padding(paddingValues)) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    when (val state = uiState) {
                        is AddDeviceUiState.Discovering -> {
                            DiscoveringScreen(Modifier){
                                viewModel.stopDiscovery()
                            }
                        }
                        is AddDeviceUiState.Error -> {
                            ErrorScreen(state.message, Modifier){
                                viewModel.retry()
                            }
                        }
                        AddDeviceUiState.Loaded -> {
                            LoadedScreen(modifier = Modifier, deviceList = deviceList, onItemSelected = {
                                viewModel.showDialogAddDevice(it)
                            }) {
                                viewModel.startDiscovery()
                            }
                            if(dialogUiState is DialogUiState.Show){
                                AddDeviceDialog(dialogUiState as DialogUiState.Show)
                            }
                        }
                    }


                }

            }
        }


    }

    @Composable
    private fun AddDeviceDialog(show: DialogUiState.Show) {
        TODO("Not yet implemented")
    }

    @Composable
    private fun DiscoveringScreen(modifier: Modifier, onClick:()->Unit) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.animation_radar))

            LottieAnimation(
                composition = composition,
                modifier = Modifier.size(300.dp),
                speed = 0.5f,
            )

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .align(Alignment.BottomCenter)) {
                Button(onClick = {
                    onClick.invoke()
                }) {
                    Text(text = stringResource(R.string.stop))
                }
            }
        }
    }

    @Composable
    private fun ErrorScreen(message: String, modifier: Modifier, onClick:()->Unit) {
        Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.animation_cam))
            Row(modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {

                LottieAnimation(
                    composition = composition,
                    modifier = Modifier.size(15.dp),
                    speed = 0.5f,
                )
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)) {
                Text(text = message)
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)) {
                Button(onClick = {
                    onClick.invoke()
                }) {
                    Text(text = stringResource(R.string.accept))
                }
            }

        }
    }

    @Composable
    private fun LoadedScreen(deviceList: List<DeviceInfo>, modifier: Modifier, onItemSelected:(DeviceInfo)->Unit, startDiscovery: () -> Unit) {
        Box(modifier,contentAlignment = Alignment.Center) {
            LazyColumn {
                items(deviceList, key = { it.serial}){item->
                    Row {
                        Column {
                            Text(text = item.serial)
                            Text(text = item.host)
                        }
                        IconButton(onClick = {
                            onItemSelected.invoke(item)
                        }) {
                            Icon(painterResource(id = R.drawable.connect), contentDescription = "connect")
                        }
                    }
                }
            }
            if (!deviceList.any()) {
                IconButton(modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp),
                    onClick = {
                        startDiscovery()
                    }) {
                    Icon(
                        imageVector = Icons.Default.Radar,
                        contentDescription = "search devices",
                        modifier = Modifier.size(100.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

    }


}