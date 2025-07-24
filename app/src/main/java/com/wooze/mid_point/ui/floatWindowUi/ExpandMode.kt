package com.wooze.mid_point.ui.floatWindowUi

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.wooze.mid_point.R
import com.wooze.mid_point.data.Corner
import com.wooze.mid_point.state.UiState
import com.wooze.mid_point.tools.DataTools
import com.wooze.mid_point.tools.FloatWindowAction
import com.wooze.mid_point.typeCategory
import com.wooze.mid_point.viewModel.FloatViewModel

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun ExpandMode(context: Context, viewModel: FloatViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Box( // 菜单栏
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 100.dp,
                    shape = RoundedCornerShape(Corner.Inner),
                    clip = true
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
                IconButton(onClick = { FloatWindowAction.closeFloatActivity() }) {
                    Icon(painterResource(R.drawable.close_round), contentDescription = null)
                }
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(20.dp)
                        .width(40.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${UiState.dragDataList.size}")
                }
                IconButton(onClick = { viewModel.toggleMenu() }) {
                    Icon(painterResource(R.drawable.menu), contentDescription = null)
                }
                DropMenu(viewModel)
            }
        }

        LazyColumn(
            modifier = Modifier
                .clip(RoundedCornerShape(Corner.Inner)),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { Spacer(modifier = Modifier.height(50.dp)) }
            UiState.dragDataList.asReversed().forEach { data ->
                item {// TODO 完善悬浮窗
                    if (data.mimetype != null) {
                        val isSelected by derivedStateOf { viewModel.selectList.contains(data) }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(Corner.Inner))
                                .dragAndDropSource {
                                    detectTapGestures(onLongPress = {
                                        if (!viewModel.selectMode.value) {
                                            startTransfer(
                                                DragAndDropTransferData(
                                                    clipData = DataTools.sendData(context, data),
                                                    flags = View.DRAG_FLAG_GLOBAL or View.DRAG_FLAG_GLOBAL_URI_READ
                                                )
                                            )
                                        }
                                    }, onTap = {
                                        if (viewModel.selectMode.value && !isSelected) {
                                            viewModel.selectList.add(data)
                                        } else if (isSelected) {
                                            viewModel.selectList.remove(data)
                                        }
                                    })
                                }
                        ) {
                            typeCategory(data)
                            if (viewModel.selectMode.value) {
                                Box(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .clip(RoundedCornerShape(Corner.Selector))
                                        .size(40.dp)
                                        .border(
                                            width = 2.dp,
                                            color = Color.White,
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .background(
                                            if (!isSelected) Color(0x80000000) else Color(
                                                0xFF2196F3
                                            )
                                        )
                                        .align(alignment = Alignment.TopEnd)
                                        .wrapContentSize(Alignment.Center),
                                ) {
                                    Icon(Icons.Filled.Check, contentDescription = null)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropMenu(viewModel: FloatViewModel) {
    val context = LocalContext.current
    DropdownMenu(
        expanded = viewModel.menuExpanded,
        onDismissRequest = { viewModel.toggleMenu() },
        modifier = Modifier.width(150.dp),
        shape = RoundedCornerShape(Corner.Inner)
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.drop_menu_share)) },
            onClick = {
                viewModel.shareTo(context)
            },
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.drop_menu_clear)) },
            onClick = {
                viewModel.resetFloatData()
                viewModel.toggleMenu()
            },
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.drop_menu_select)) },
            onClick = {
                viewModel.toggleSelectMode()
                viewModel.toggleMenu()
            },
        )
    }
}
