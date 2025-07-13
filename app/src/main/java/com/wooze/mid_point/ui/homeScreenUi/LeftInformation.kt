package com.wooze.mid_point.ui.homeScreenUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wooze.mid_point.viewModel.MainViewModel

@Composable
fun LeftInformation(viewModel: MainViewModel, modifier: Modifier) {
    Box(modifier = modifier) {
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
}

