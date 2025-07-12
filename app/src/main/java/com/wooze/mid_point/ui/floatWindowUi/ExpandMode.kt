package com.wooze.mid_point.ui.floatWindowUi

import android.content.ClipData
import android.content.Context
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.wooze.mid_point.R
import com.wooze.mid_point.objects.FloatWindowAction
import com.wooze.mid_point.state.UiState
import com.wooze.mid_point.typeCategory
import com.wooze.mid_point.viewModel.FloatViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun ExpandMode(context: Context,viewModel: FloatViewModel) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Box( // 菜单栏
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 100.dp,
                    shape = RoundedCornerShape(20.dp),
                    clip = true // ✅ 自动 clip 形状
                )
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.primary)
                .height(50.dp)
                .zIndex(1f)

        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var expanded by remember { mutableStateOf(false) }
                IconButton(onClick = { FloatWindowAction.closeFloatActivity()}) {
                    Icon(painterResource(R.drawable.close_round), contentDescription = null)
                }
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(20.dp)
                        .width(40.dp)
                        .background(MaterialTheme.colorScheme.onPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${UiState.dragDataList.size}")
                }
                IconButton(onClick = {expanded = true},) {
                    Icon(painterResource(R.drawable.menu),contentDescription = null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(100.dp)
                        .height(300.dp)
                ) {
                    repeat(10) {
                        DropdownMenuItem(
                            text = { Text("分享到 $it") },
                            onClick = {
                                // 选中逻辑
                                viewModel.resetFloatData()
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .clip(RoundedCornerShape(20)),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { Spacer(modifier = Modifier.height(50.dp)) }
            UiState.dragDataList.forEach { data ->
                item {// TODO 完善悬浮窗
                    if (data.mimetype != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(20))
                                .dragAndDropSource {
                                    detectTapGestures(onLongPress = {
                                        startTransfer(
                                            DragAndDropTransferData(
                                                clipData = ClipData.newUri(
                                                    context.contentResolver,
                                                    "file",
                                                    data.uri
                                                ),
                                                flags = View.DRAG_FLAG_GLOBAL or View.DRAG_FLAG_GLOBAL_URI_READ
                                            )
                                        )
                                    })
                                }
                        ) {
                            typeCategory(data)
                        }
                    }
                }
            }
        }
    }
}

