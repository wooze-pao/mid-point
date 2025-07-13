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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.wooze.mid_point.viewModel.MainViewModel

@Composable
fun RightButtonGroup(
    modifier: Modifier,
    viewModel: MainViewModel,
    context: Context,
    openFloat: () -> Unit,
    closeFloat: () -> Unit
) {

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            if (viewModel.checkOverlayPermission(context)) {
                val toast = Toast.makeText(context, "已获取权限", Toast.LENGTH_SHORT)
                toast.show()
                openFloat()
            } else {
                val toast = Toast.makeText(context, "获取权限失败", Toast.LENGTH_SHORT)
                toast.show()
            }
        }


    val topWeight by animateFloatAsState(
        targetValue = viewModel.topWeight,
        spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
    )
    val bottomWeight by animateFloatAsState(
        targetValue = viewModel.bottomWeight,
        spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
    )

    Column(modifier = modifier) {
        Button(
            onClick = {
                if (!viewModel.checkOverlayPermission(context)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        "package:${context.packageName}".toUri()
                    )
                    launcher.launch(intent)
                } else if (!viewModel.isShowing.value) {
                    openFloat()
                }
            },
            modifier = Modifier
                .weight(topWeight)
                .fillMaxSize(),
            colors = ButtonDefaults.buttonColors(
                MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { closeFloat() },
            modifier = Modifier
                .weight(bottomWeight)
                .fillMaxSize(),
            colors = ButtonDefaults.buttonColors(
                MaterialTheme.colorScheme.error
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Icon(Icons.Filled.Clear, contentDescription = null)
        }
    }
}