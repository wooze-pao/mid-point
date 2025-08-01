package com.wooze.mid_point.ui.floatWindowUi

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.wooze.mid_point.StackBox
import com.wooze.mid_point.data.Corner
import com.wooze.mid_point.state.UiState
import com.wooze.mid_point.tools.DataTools
import com.wooze.mid_point.tools.FloatWindowAction
import com.wooze.mid_point.typeCategory
import com.wooze.mid_point.ui.addon.dragOutData
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
        MenuBar(viewModel)
        // 内容
        LazyColumn(
            modifier = Modifier
                .clip(RoundedCornerShape(Corner.Inner)),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item { Spacer(modifier = Modifier.height(50.dp)) }
            UiState.groupDragList.asReversed().forEachIndexed { index, data ->
                val actualIndex = UiState.groupDragList.size - index - 1
                if (viewModel.clickedGroupIndex == null) {
                    item (key = data.hashCode()){
                        val isSelected by derivedStateOf { viewModel.selectedGroups.contains(actualIndex) }
                        Box(
                            modifier = Modifier
                                .requiredWidth(130.dp)
                                .clip(RoundedCornerShape(Corner.Inner))
                                .dragOutData(DataTools.sendData(context, data)) {
                                    if (!viewModel.selectMode.value) {
                                        viewModel.addIndex(actualIndex)
                                    } else if (isSelected) {
                                        viewModel.selectedGroups.remove(actualIndex)
                                    } else {
                                        viewModel.selectedGroups.add(actualIndex)
                                    }
                                }
                        ) {
                            StackBox {
                                typeCategory(data.last())
                            }
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
                } else if (viewModel.clickedGroupIndex == actualIndex) {
                    // key 的作用不过我不是很懂 https://stackoverflow.com/questions/68790215/lazycolumn-items-key-parameter-purpose

                    items(data.asReversed(), key = { it.id }) { dragData ->
                        val isSelected by derivedStateOf { viewModel.selectList.contains(dragData) }
                        Box(
                            modifier = Modifier
                                .animateItem()
                                .requiredHeight(100.dp)
                                .requiredWidth(130.dp)
                                .clip(RoundedCornerShape(Corner.Inner))
                                .dragOutData(DataTools.sendData(context, dragData)) {
                                    if (viewModel.selectMode.value && !isSelected) {
                                        viewModel.selectList.add(dragData)
                                    } else if (isSelected) {
                                        viewModel.selectList.remove(dragData)
                                    }
                                }
                        ) {
                            typeCategory(dragData)
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

// 菜单栏
@Composable
fun MenuBar(viewModel: FloatViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
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
            if ( viewModel.clickedGroupIndex != null && !viewModel.selectMode.value) {
                IconButton(onClick = { viewModel.clickedGroupIndex = null}) {
                    Icon(Icons.Filled.KeyboardArrowUp, contentDescription = null)
                }
            } else if (!viewModel.selectMode.value) {
                IconButton(onClick = { FloatWindowAction.closeFloatActivity() }) {
                    Icon(painterResource(R.drawable.close_round), contentDescription = null)
                }
            }
            else {
                IconButton(onClick = { viewModel.removeThing()
                viewModel.removeSelectedGroups()}) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                }
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .height(20.dp)
                    .width(40.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("${UiState.groupDragList.size}")
            }
            IconButton(onClick = { viewModel.toggleMenu() }) {
                Icon(painterResource(R.drawable.menu), contentDescription = null)
            }
            DropMenu(viewModel)
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
