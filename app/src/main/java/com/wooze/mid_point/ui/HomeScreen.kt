package com.wooze.mid_point.ui

import android.annotation.SuppressLint
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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(openFloat: () -> Unit, closeFloat: () -> Unit, viewModel: MainViewModel) {
    val context = LocalContext.current
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

    LaunchedEffect(Unit) {
        viewModel.checkOverlayPermission(context)
    }

    val topWeight by animateFloatAsState(
        targetValue = viewModel.topWeight,
        spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
    )
    val bottomWeight by animateFloatAsState(
        targetValue = viewModel.bottomWeight,
        spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
    )

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
                Box(modifier = Modifier.weight(2f)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.fillMaxSize()

                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("中转站状态", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(100.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (viewModel.isShowing.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                                    .wrapContentSize(Alignment.Center)
                            ) {
                                Text(viewModel.label, fontSize = 20.sp, color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "点击右边按钮进行切换",
                                color = MaterialTheme.colorScheme.outline,
                                fontSize = 15.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(
                                        CircleShape
                                    )
                                    .background(if (viewModel.haveOverlayPermission.value) Color.Green else Color.Red)

                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("悬浮窗权限获取：${viewModel.permissionLabel}")

                        }

                    }
                }


                Column(Modifier.weight(1f)) {
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
            Box(modifier = Modifier.weight(1f)) // 占位符
        }

    }
}

