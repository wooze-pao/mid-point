package com.wooze.mid_point.ui.homeScreenUi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.wooze.mid_point.viewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(openFloat: () -> Unit, closeFloat: () -> Unit, viewModel: MainViewModel) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.checkOverlayPermission(context)
    }

    Scaffold(bottomBar = {
        NavigationBar() {
            NavigationBarItem(
                selected = true,
                onClick = {},
                icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                label = { Text("主页") }
            )
        }
    }, topBar = { TopAppBar(title = { Text("中点站 -- demo") }) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 5.dp, end = 5.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(it)
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                LeftInformation(viewModel, modifier = Modifier.weight(2f))

                RightButtonGroup(
                    modifier = Modifier.weight(1f),
                    viewModel,
                    context,
                    { openFloat() },
                    { closeFloat() })
            }
            Box(modifier = Modifier.weight(1f)) // 占位符
        }

    }
}

