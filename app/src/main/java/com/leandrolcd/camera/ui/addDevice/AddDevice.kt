package com.leandrolcd.camera.ui.addDevice

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lan
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
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
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val viewModel: AddDeviceViewModel = hiltViewModel()

        val deviceList by viewModel.deviceList.collectAsState()

        val uiState by viewModel.uiState.collectAsState(AddDeviceUiState.Loaded)

        val dialogUiState by viewModel.dialogUiState.collectAsState(DialogUiState.None)

        Scaffold(modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    viewModel.showDialogAddDevice()
                }){
                    Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                }
            }
            ) { paddingValues ->
            Surface(modifier = Modifier.padding(paddingValues)
                ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                    when (val state = uiState) {
                        is AddDeviceUiState.Discovering -> {
                            DiscoveringScreen(Modifier) {
                                viewModel.stopDiscovery()
                            }
                        }

                        is AddDeviceUiState.Error -> {
                            ErrorScreen(state.message, Modifier) {
                                viewModel.retry()
                            }
                        }

                        AddDeviceUiState.Loaded -> {
                            LoadedScreen(
                                modifier = Modifier,
                                deviceList = deviceList,
                                onItemSelected = {
                                    viewModel.showDialogAddDevice(it)
                                }) {
                                viewModel.startDiscovery()
                            }
                            AnimatedVisibility(visible = dialogUiState is DialogUiState.Show) {
                                when(val stateUI = dialogUiState){
                                    DialogUiState.None -> {}
                                    is DialogUiState.Show -> {
                                        AddDeviceDialog(stateUI)
                                    }
                                }
                            }
                        }
                    }
                    val isLoading by viewModel.isLoading.collectAsState(false)
                    AnimatedVisibility(visible = isLoading) {
                        BasicAlertDialog(onDismissRequest = { },
                            properties = DialogProperties(
                                dismissOnBackPress = false,
                                dismissOnClickOutside = false,
                                usePlatformDefaultWidth = false)) {
                            LoadingScreen(Modifier)
                        }
                    }

                }

            }
        }

    }

    @Composable
    private fun LoadingScreen(@SuppressLint("ModifierParameter") modifier: Modifier.Companion) {
    Box(modifier = modifier
        .fillMaxSize(), contentAlignment = Alignment.Center){
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.animation_cam))
        LottieAnimation(
            composition = composition,
            modifier = Modifier.size(200.dp),
            speed = 0.5f,
            iterations = Int.MAX_VALUE
        )
    }
    }
    @Composable
    private fun AddDeviceDialog(state: DialogUiState.Show) {
        var user by remember {
            mutableStateOf(state.user)
        }
        var password by remember {
            mutableStateOf(state.password)
        }
        var host by remember {
            mutableStateOf(state.url)
        }
        var isVisible by remember {
            mutableStateOf(false)
        }

        AlertDialog(
            onDismissRequest = {
                state.onDismiss.invoke()
            },
            title = {
                Text(text = stringResource(R.string.add_device_title))
            },
            text = {
                Column(Modifier.fillMaxWidth()) {
                    TextBox(value = host, onValueChange = {
                        host = it
                    },
                        label = stringResource(R.string.url),
                        supportedHelp = stringResource(R.string.url_supported_help),
                        leadingIcon = {
                            Icon(Icons.Default.Lan, contentDescription = "")

                        })
                    Spacer(modifier = Modifier.size(8.dp))

                    TextBox(value = user, onValueChange = {
                        user = it
                    },
                        label = stringResource(R.string.user),
                        supportedHelp = stringResource(R.string.msj_admin_default),
                        leadingIcon = {
                            Icon(Icons.Default.Person2, contentDescription = "")

                        })
                    Spacer(modifier = Modifier.size(8.dp))
                    TextBox(
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        label = stringResource(R.string.password),
                        supportedHelp = stringResource(R.string.password_supported_help),
                        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(Icons.Default.Key, contentDescription = "")

                        },
                        trailingIcon = {
                            if (isVisible) {
                                IconButton(onClick = {
                                    isVisible = false
                                }) {
                                    Icon(Icons.Default.VisibilityOff, contentDescription = "")
                                }
                            } else {
                                IconButton(onClick = {
                                    isVisible = true
                                }) {
                                    Icon(Icons.Default.Visibility, contentDescription = "")
                                }
                            }
                        }
                    )

                }
            },
            confirmButton = {
                Button(onClick = {
                    state.onComplete(host, user, password)
                }) {
                    Text(text = stringResource(R.string.connect))
                }
            },
            dismissButton = {
                Button(onClick = {
                    state.onDismiss.invoke()
                }) {
                    Text(text = stringResource(R.string.cancelar))
                }
            }
        )
    }
    @Composable
    private fun TextBox(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        label: String? = null,
        supportedHelp: String? = null,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,

        ) {
        OutlinedTextField(
            value = value,
            modifier = modifier,
            onValueChange = {
                onValueChange.invoke(it)
            },
            label = {
                label?.let {
                    Text(text = it)
                }
            },
            supportingText = {
                supportedHelp?.let {
                    Text(text = it)
                }
            },
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
        )
    }
    @Composable
    private fun DiscoveringScreen(modifier: Modifier, onClick: () -> Unit) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.animation_radar))

            LottieAnimation(
                composition = composition,
                modifier = Modifier.size(300.dp),
                speed = 0.5f,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Button(onClick = {
                    onClick.invoke()
                }) {
                    Text(text = stringResource(R.string.stop))
                }
            }
        }
    }
    @Composable
    private fun ErrorScreen(message: String, modifier: Modifier, onClick: () -> Unit) {
        Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.animation_cam))
            Row(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                LottieAnimation(
                    composition = composition,
                    modifier = Modifier.size(150.dp),
                    speed = 0.5f,
                    iterations = 3
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(text = message)
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    onClick.invoke()
                }) {
                    Text(text = stringResource(R.string.accept))
                }
            }

        }
    }
    @Composable
    private fun LoadedScreen(
        deviceList: List<DeviceInfo>,
        modifier: Modifier,
        onItemSelected: (DeviceInfo) -> Unit,
        startDiscovery: () -> Unit
    ) {
        Box(modifier, contentAlignment = Alignment.Center) {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.list_device),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                LazyColumn(
                    Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),

                    ) {
                    items(deviceList, key = { it.serial }) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.outline
                            )
                        ) {
                            Row(modifier = Modifier.padding(8.dp)) {
                                Column {
                                    Text(text = item.serial, fontWeight = FontWeight.Bold)
                                    Text(text = item.host)
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                    onClick = {
                                        onItemSelected.invoke(item)
                                    }) {
                                    Image(
                                        painterResource(id = R.drawable.connect),
                                        contentDescription = "connect"
                                    )
                                }
                            }
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
