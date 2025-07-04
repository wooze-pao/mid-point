package com.wooze.mid_point.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


object UiState {
    var isShowing = false
    var isOpen: MutableState<Boolean> = mutableStateOf(false)
}