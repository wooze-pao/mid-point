package com.wooze.mid_point.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.wooze.mid_point.data.WindowState
import com.wooze.mid_point.data.WindowState.*

class FloatViewModel : ViewModel() {
    // 因为在上面导入了WindowState.*所以忽略了WindowState.什么什么，直接Hidden或其他
    private val _windowState: MutableState<WindowState> = mutableStateOf(Hidden)
    val windowState : State<WindowState> = _windowState

    val targetHeight: State<Dp> = derivedStateOf {
        when(windowState.value) {
            Hidden -> 120.dp
            Collapsed -> 120.dp
            Expand -> 300.dp
        }
    }

    val targetWidth: State<Dp> = derivedStateOf {
        when(windowState.value) {
            Hidden -> 20.dp
            Collapsed -> 150.dp
            Expand -> 150.dp
        }
    }

    fun hidden() {
        _windowState.value = Hidden
    }

    fun collapsed() {
        _windowState.value = Collapsed
    }

    fun expand() {
        _windowState.value = Expand
    }

    fun toggleState() {
        _windowState.value = when(_windowState.value) {
            Hidden -> Collapsed
            Collapsed -> Expand
            Expand -> Collapsed
        }
    }
}

