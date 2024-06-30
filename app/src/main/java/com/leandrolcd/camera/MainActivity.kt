package com.leandrolcd.camera

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leandrolcd.onvifcamera.OnvifDevice
import com.leandrolcd.camera.ui.theme.CameraTheme
import dagger.hilt.EntryPoint
import kotlinx.coroutines.launch

@EntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            


            val coroutineScope = rememberCoroutineScope()
            CameraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Button(
                        modifier = Modifier.padding(innerPadding),
                        onClick = {
                            coroutineScope.launch {
                                    try {
                                        val address = "192.168.0.161:8899"
                                        
                                    
                                        // Get camera services
                                         val url =
                                            if (address.contains("://")) address
                                            else "http://$address/onvif/device_service"
                                        val device = OnvifDevice.requestDevice(url, "admin", "", true)
                                        

                                        val deviceInformation = device.getDeviceInformation()
                                        Log.d("OnvifDevice", "deviceInformation: $deviceInformation")

                                        val profiles = device.getProfiles()

                                        profiles.firstOrNull { it.canStream() }?.let {
                                            
                                            device.getStreamURI(it).let { uri ->
                                                Log.d("OnvifDevice", "uri: $uri")
                                            }
                                        }
                                    } catch (e: Exception) {
                                        Log.d("OnvifDevice", "Exception: $e")
                                    }
                            
                        }
                            }
                    ){
                        Text(text = "load")
                    }



                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CameraTheme {
        Greeting("Android")
    }
}