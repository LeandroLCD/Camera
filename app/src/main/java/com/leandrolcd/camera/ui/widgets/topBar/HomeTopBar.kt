package com.leandrolcd.camera.ui.widgets.topBar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leandrolcd.camera.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(){
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.device_list)
            )
        }
    )
}