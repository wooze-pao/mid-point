package com.wooze.mid_point.state

import android.provider.Settings
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


object UiState {
    // TODO 快速点击状态不同步
    var isShowing = false
    var isOpen: MutableState<Boolean> = mutableStateOf(false)
}