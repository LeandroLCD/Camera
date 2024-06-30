package com.leandrolcd.camera.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.leandrolcd.camera.R
import com.leandrolcd.camera.ui.addDevice.AddDevice

class Home:Screen {
    @Composable
    override fun Content() {
        val navigatorController = LocalNavigator.current
        Scaffold(floatingActionButton = {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary),
                onClick = {
                navigatorController?.push(AddDevice())
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add_device))
            }
        }) {padding ->
                Box(Modifier.padding(padding)){
                    LazyColumn {

                    }
                }
        }
    }
}

