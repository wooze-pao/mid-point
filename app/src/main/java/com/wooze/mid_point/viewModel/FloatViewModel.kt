package com.wooze.mid_point.viewModel

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.wooze.mid_point.data.DragData
import com.wooze.mid_point.data.WindowState
import com.wooze.mid_point.data.WindowState.Collapsed
import com.wooze.mid_point.data.WindowState.Expand
import com.wooze.mid_point.data.WindowState.Hidden
import com.wooze.mid_point.state.UiState
import com.wooze.mid_point.tools.DataTools

class FloatViewModel : ViewModel() {
    // 因为在上面导入了WindowState.*所以忽略了WindowState.什么什么，直接Hidden或其他
    private val _windowState: MutableState<WindowState> = mutableStateOf(Hidden)
    val windowState: State<WindowState> = _windowState

    var menuExpanded: Boolean by mutableStateOf(false)

    private val _selectMode = mutableStateOf(false)
    val selectMode: State<Boolean> = _selectMode
    val selectList = mutableStateListOf<DragData>()


    val targetHeight: State<Dp> = derivedStateOf {
        when (windowState.value) {
            Hidden -> 120.dp
            Collapsed -> 120.dp
            Expand -> 500.dp
        }
    }

    val targetWidth: State<Dp> = derivedStateOf {
        when (windowState.value) {
            Hidden -> 20.dp
            Collapsed -> 150.dp
            Expand -> 150.dp
        }
    }

    fun shareTo(context: Context) {
        val shareIntent = if (selectMode.value) {
            DataTools.shareData(selectList)
        } else {
            DataTools.shareData(UiState.dragDataList)
        }

        if (shareIntent == null) {
            toggleMenu()
            return
        }
        toggleMenu()
        hidden()
        context.startActivity(Intent.createChooser(shareIntent, null))
    }


    fun toggleSelectMode() {
        _selectMode.value = !_selectMode.value
    }

    fun toggleMenu() {
        menuExpanded = !menuExpanded
    }

    fun resetFloatData() {
        UiState.dragDataList.clear()
    }

    fun hidden() {
        _windowState.value = Hidden
        _selectMode.value = false
        selectList.clear()
    }

    fun collapsed() {
        _windowState.value = Collapsed
    }

    fun expand() {
        _windowState.value = Expand
    }

    fun toggleState() {
        when (_windowState.value) {
            Hidden -> collapsed()
            Collapsed -> expand()
            Expand -> {
                collapsed()
                _selectMode.value = false
                selectList.clear()
            }
        }
    }
}

