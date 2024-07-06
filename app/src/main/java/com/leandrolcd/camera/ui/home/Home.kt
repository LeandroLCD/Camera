package com.leandrolcd.camera.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.leandrolcd.camera.R
import com.leandrolcd.camera.ui.addDevice.AddDevice
import com.leandrolcd.camera.ui.oneCamera.OneCamera
import com.leandrolcd.camera.ui.widgets.bottonBar.HomeBottomBar
import com.leandrolcd.camera.ui.widgets.topBar.HomeTopBar
import com.leandrolcd.domain.models.Device
import com.leandrolcd.presentation.device.DeviceListViewModel

class Home:Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigatorController = LocalNavigator.current
        val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior(rememberBottomAppBarState())
        val viewModel = hiltViewModel<DeviceListViewModel>()
        Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            floatingActionButton = {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary),
                onClick = {
                navigatorController?.push(AddDevice())
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add_device))
            }
        },
            topBar = {
                HomeTopBar()
            },
            bottomBar = {
                HomeBottomBar(navigatorController, scrollBehavior = scrollBehavior)
            }
            ) {padding ->
                Box(Modifier.padding(padding)){
                    val deviceList  by viewModel.deviceList.collectAsState()
                    Column {
                        LazyColumn {
                            items(deviceList){
                                ItemsHolder(it){
                                    navigatorController?.push(OneCamera(deviceId = it.id))
                                }
                            }
                        }
                    }
                }
        }
    }

    @Composable
    private fun ItemsHolder(device: Device, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
        val context = LocalContext.current
        val request = remember(device) {
            ImageRequest.Builder(context)
                .data(device.snapshot)
                .crossfade(true)
                .scale(Scale.FILL)
                .build()
        }
        Surface(modifier = modifier.fillMaxWidth(),
            onClick = {
                onClick.invoke()
        }) {
            Row(modifier.fillMaxWidth()) {
                AsyncImage(
                    model = request, modifier = Modifier.size(30.dp),
                    contentDescription = "Photo",
                    error = painterResource(id = R.drawable.one_cam)
                )
                Column(modifier.padding(horizontal = 16.dp)) {
                    Text(text = device.alias, fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 14.sp)
                    Text(text = stringResource(R.string.channel, device.id), fontWeight = FontWeight.Light, fontSize = 10.sp, lineHeight = 12.sp)
                    Text(text = device.serial, fontWeight = FontWeight.Light, fontSize = 10.sp, lineHeight = 12.sp)
                }
            }
        }
    }
}


