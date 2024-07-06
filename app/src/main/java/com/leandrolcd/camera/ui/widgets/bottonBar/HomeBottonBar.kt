package com.leandrolcd.camera.ui.widgets.bottonBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.leandrolcd.camera.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBottomBar(navigatorController: Navigator?, scrollBehavior: BottomAppBarScrollBehavior) {

    val items = buildList {
        add(ItemNavigation("OneCams", "OneCams", painterResource(id = R.drawable.one_cam)))
        add(ItemNavigation("TwoCams", "TwoCams", painterResource(id = R.drawable.two_cam)))
        add(ItemNavigation("ForCams", "ForCams", painterResource(id = R.drawable.for_cam)))
        add(ItemNavigation("NineCams", "NineCams", painterResource(id = R.drawable.nine_cam)))
    }

     BottomAppBar(scrollBehavior = scrollBehavior) {
         NavigationBarItem(selected = navigatorController?.lastItem?.key == "Home", onClick = {
             // navigatorController.push()
         }, icon = {
             Icon(Icons.Default.Home,
                 contentDescription = "",
                 modifier = Modifier.size(30.dp),
                 tint = Color.Black)
         })

        items.forEach { item ->

            NavigationBarItem(selected = navigatorController?.lastItem?.key == item.route, onClick = {
                   // navigatorController.push()
            }, icon = {
                Image(item.icon, contentDescription = "", modifier = Modifier.size(30.dp).padding(4.dp) )
            })
        }
    }
}