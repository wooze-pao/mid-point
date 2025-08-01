package com.wooze.mid_point.viewModel

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Point
import android.util.Log
import android.util.Log.i
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
import kotlinx.coroutines.flow.MutableStateFlow

class FloatViewModel : ViewModel() {
    // 因为在上面导入了WindowState.*所以忽略了WindowState.什么什么，直接Hidden或其他
    private val _windowState: MutableState<WindowState> = mutableStateOf(Hidden)
    val windowState: State<WindowState> = _windowState
    var menuExpanded: Boolean by mutableStateOf(false)
    private val _selectMode = mutableStateOf(false)
    val selectMode: State<Boolean> = _selectMode
    val selectList = mutableStateListOf<DragData>()
    val isAnimating = mutableStateOf(false)
    var clickedGroupIndex by mutableStateOf<Int?>(null)
    val selectedGroups = mutableStateListOf<Int>()
    val position = MutableStateFlow(Point(-130.dp.toPx(), 300))

    fun addIndex (index: Int) {
        clickedGroupIndex = index
        Log.d("mpDebug","$index")
    }

    fun removeThing () {
        if (clickedGroupIndex != null) {
            selectList.forEach { dragData ->
                UiState.groupDragList[clickedGroupIndex!!].remove(dragData)
            }
            if (UiState.groupDragList[clickedGroupIndex!!].isEmpty()) {
                UiState.groupDragList.removeAt(clickedGroupIndex!!)
                clickedGroupIndex = null
            }
            closeSelect()
        }

    }

    fun removeSelectedGroups() {
        selectedGroups.forEach {index ->
            UiState.groupDragList.removeAt(index)
        }
        selectedGroups.clear()
        closeSelect()
    }

    fun Dp.toPx(): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (this.value * density).toInt()
    }

    val targetHeight: State<Dp> = derivedStateOf {
        when (windowState.value) {
            Hidden -> 120.dp
            Collapsed -> 120.dp
            Expand -> 500.dp
        }
    }

    fun shareTo(context: Context) {
        val shareIntent = if (selectMode.value) {
            DataTools.shareData(selectList)
        } else {
            DataTools.shareData(UiState.groupDragList.flatten())
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
        UiState.groupDragList.clear()
    }

    fun hidden() {
        if (isAnimating.value) {
            return
        }
        _windowState.value = Hidden
        closeSelect()
        position.value = Point(-130.dp.toPx(), 300)
    }

    fun collapsed() {
        _windowState.value = Collapsed
        closeSelect()
        clickedGroupIndex = null
        position.value = Point(0, 300)
    }

    fun closeSelect() {
        _selectMode.value = false
        selectList.clear()
    }

    fun expand() {
        isAnimating.value = true
        _windowState.value = Expand
    }

    fun toggleState() {
        when (_windowState.value) {
            Hidden -> collapsed()
            Collapsed -> expand()
            Expand -> {
                isAnimating.value = true
                collapsed()
            }
        }
    }
}

