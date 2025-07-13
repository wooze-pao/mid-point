package com.wooze.mid_point.ui.homeScreenUi

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
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

