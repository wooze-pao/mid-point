package com.wooze.mid_point.ui.floatWindowUi

import android.content.Context
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.wooze.mid_point.R
import com.wooze.mid_point.TypeCategory
import com.wooze.mid_point.data.SizesDp
import com.wooze.mid_point.state.UiState
import com.wooze.mid_point.tools.DataTools
import com.wooze.mid_point.tools.FloatWindowAction
import com.wooze.mid_point.ui.addon.dragOutData
import com.wooze.mid_point.viewModel.FloatViewModel


@Composable
fun ExpandMode(context: Context, viewModel: FloatViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        MenuBar(viewModel)
        // 内容
        ContentList(viewModel, context)
    }
}

@Composable
fun ContentList(viewModel: FloatViewModel, context: Context) {
    LazyColumn(
        modifier = Modifier
            .clip(RoundedCornerShape(SizesDp.R_INNER)),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item { Spacer(modifier = Modifier.height(50.dp)) }
        UiState.groupDragList.asReversed().forEachIndexed { index, data ->
            val actualIndex = UiState.groupDragList.size - index - 1
            if (viewModel.clickedGroupIndex == null) {
                item(key = data.hashCode()) {
                    val isSelected by remember {
                        derivedStateOf {
                            viewModel.selectedGroups.contains(
                                actualIndex
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .requiredWidth(SizesDp.ITEM_WIDTH)
                            .clip(RoundedCornerShape(SizesDp.R_INNER))
                            .dragOutData(DataTools.sendData(context, data)) {
                                if (!viewModel.selectMode.value && data.size != 1) {
                                    viewModel.addIndex(actualIndex)
                                    return@dragOutData
                                }
                                viewModel.toggleSelection(actualIndex, isSelected)
                            }
                    ) {
                        if (data.size == 1) {
                            TypeCategory(data[0])
                        } else {
                            StackBox {
                                TypeCategory(data.last())
                            }
                        }
                        if (viewModel.selectMode.value) {
                            CheckBoxExp(isSelected, Modifier.align(Alignment.TopEnd))
                        }
                    }
                }
            } else if (viewModel.clickedGroupIndex == actualIndex) {
                // key 的作用不过我不是很懂 https://stackoverflow.com/questions/68790215/lazycolumn-items-key-parameter-purpose
                items(data.asReversed(), key = { it.id }) { dragData ->
                    val isSelected by remember(viewModel.selectList) {
                        derivedStateOf {
                            viewModel.selectList.contains(
                                dragData
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .animateItem()
                            .requiredHeight(SizesDp.ITEM_HEIGHT)
                            .requiredWidth(SizesDp.ITEM_WIDTH)
                            .clip(RoundedCornerShape(SizesDp.R_INNER))
                            .dragOutData(DataTools.sendData(context, dragData)) {
                                viewModel.toggleSelection(dragData, isSelected)
                            }
                    ) {
                        TypeCategory(dragData)
                        if (viewModel.selectMode.value) {
                            CheckBoxExp(isSelected, Modifier.align(Alignment.TopEnd))
                        }
                    }

                }


            }

        }
    }
}

@Composable
fun CheckBoxExp(isSelected: Boolean, modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(SizesDp.R_SELECTOR))
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
            .wrapContentSize(Alignment.Center),
    ) {
        Icon(Icons.Filled.Check, contentDescription = null)
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
                shape = RoundedCornerShape(SizesDp.R_INNER),
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
            if (viewModel.clickedGroupIndex != null && !viewModel.selectMode.value) {
                IconButton(onClick = { viewModel.clickedGroupIndex = null }) {
                    Icon(Icons.Filled.KeyboardArrowUp, contentDescription = null)
                }
            } else if (!viewModel.selectMode.value) {
                IconButton(onClick = { FloatWindowAction.closeFloatActivity() }) {
                    Icon(painterResource(R.drawable.close_round), contentDescription = null)
                }
            } else {
                IconButton(onClick = {
                    viewModel.removeSelectItem()
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                }
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .height(20.dp)
                    .width(40.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "${UiState.groupDragList.size}",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = { viewModel.toggleMenu() }) {
                Icon(painterResource(R.drawable.menu), contentDescription = null)
            }
            DropMenu(viewModel)
        }
    }
}


