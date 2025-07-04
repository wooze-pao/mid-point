package com.wooze.mid_point.ui

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.wooze.mid_point.state.UiState

@Composable
fun HomeScreen(openFloat: () -> Unit, closeFloat: () -> Unit) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            if (Settings.canDrawOverlays(context)) {
                val toast = Toast.makeText(context, "已获取权限", Toast.LENGTH_SHORT)
                toast.show()
                openFloat()
            } else {
                val toast = Toast.makeText(context, "获取权限失败", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Button(onClick = {
            if (!Settings.canDrawOverlays(context)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    "package:${context.packageName}".toUri()
                )
                launcher.launch(intent)
            } else if (!UiState.isShowing) {
                openFloat()
            }
        }) { Text("开启悬浮窗") }
        Button(onClick = { closeFloat() }) { Text("关闭悬浮窗") }
    }
}

