package com.wooze.mid_point.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.wooze.mid_point.data.DragData


object UiState {
    var isShowing = mutableStateOf(false)
    val groupDragList = mutableStateListOf<SnapshotStateList<DragData>>()
}