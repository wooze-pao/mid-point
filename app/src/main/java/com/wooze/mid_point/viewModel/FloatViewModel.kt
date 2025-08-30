package com.wooze.mid_point.viewModel

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Point
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.wooze.mid_point.data.DragData
import com.wooze.mid_point.data.SizesDp
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
    val position = MutableStateFlow(Point(SizesDp.WINDOW_GAP_R.toPx(), 100))

    enum class Side { LEFT, RIGHT }

    private var side: Side = Side.LEFT

    fun getPosition(state: WindowState): Point {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val offsetX = SizesDp.WINDOW_GAP_R.toPx() // 打开关闭时流出20.dp的宽度
        val y = position.value.y

        return when (state) {
            Hidden -> if (side == Side.LEFT) Point(
                offsetX,
                y
            ) else Point(screenWidth - SizesDp.WINDOW_WID_GAP.toPx(), y)

            Expand, Collapsed -> if (side == Side.LEFT) Point(
                0,
                y
            ) else Point(screenWidth - SizesDp.WINDOW_WIDTH.toPx(), y)
        }
    }

    fun moveToPosition(point: Point) {
        position.value = point
    }

    fun goToNewState(state: WindowState) {
        _windowState.value = state
        moveToPosition(getPosition(state))
    }

    var inDrag = false

    fun addIndex(index: Int) {
        clickedGroupIndex = index
        Log.d("mpDebug", "$index")
    }

    // 对于单个卡片
    fun toggleSelection(data: DragData, selected: Boolean) {
        if (selectMode.value) {
            if (!selected) {
                selectList.add(data)
            } else {
                selectList.remove(data)
            }
        }
    }

    //对于外面组卡片
    fun toggleSelection(index: Int, selected: Boolean) {
        if (selectMode.value) {
            if (!selected) {
                selectedGroups.add(index)
            } else {
                selectedGroups.remove(index)
            }
        }
    }

    fun removeSelectItem() {
        if (selectList.isNotEmpty()) {
            selectList.forEach { dragData ->
                UiState.groupDragList[clickedGroupIndex!!].remove(dragData)
            }
            if (UiState.groupDragList[clickedGroupIndex!!].isEmpty()) {
                UiState.groupDragList.removeAt(clickedGroupIndex!!)
                clickedGroupIndex = null
            }
        }

        if (selectedGroups.isNotEmpty()) {
            selectedGroups.forEach { index ->
                UiState.groupDragList.removeAt(index)
            }
        }
        closeSelect()
    }

    fun Dp.toPx(): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (this.value * density).toInt()
    }

    val targetHeight: State<Dp> = derivedStateOf {
        when (windowState.value) {
            Hidden -> SizesDp.WINDOW_HEIGHT
            Collapsed -> SizesDp.WINDOW_HEIGHT
            Expand -> SizesDp.WINDOW_EXPAND
        }
    }

    fun shareTo(context: Context) {
        val shareIntent = if (selectMode.value && selectList.isNotEmpty()) {
            DataTools.shareData(selectList)
        } else if (selectMode.value && selectedGroups.isNotEmpty()) {
            val newList = mutableListOf<DragData>()
            selectedGroups.forEach { index ->
                newList += UiState.groupDragList[index]
            }
            DataTools.shareData(newList)
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
        clickedGroupIndex = null
        UiState.groupDragList.clear()
    }

    // 拖动更新位置
    fun dragToChangePosition(offset: Offset) {
        if (inDrag) {
            val current = position.value
            position.value = Point(current.x + offset.x.toInt(), current.y + offset.y.toInt())
        }
    }

    // 当拖动结束
    fun endDrag() {
        inDrag = false
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val currentX = position.value.x

        inDrag = false
        side = if (currentX + 65.dp.toPx() < screenWidth / 2) Side.LEFT else Side.RIGHT

        goToNewState(windowState.value)
    }

    fun closeSelect() {
        _selectMode.value = false
        selectedGroups.clear()
        selectList.clear()
    }

    // 控制悬浮窗状态 --

    fun hidden() {
        if (isAnimating.value) return
        closeSelect()
        goToNewState(Hidden)
    }

    fun collapsed() {
        if (isAnimating.value) return
        goToNewState(Collapsed)
        closeSelect()
        clickedGroupIndex = null
    }

    fun expand() {
        if (isAnimating.value) return
        goToNewState(Expand)
    }

    fun toggleState() {
        when (_windowState.value) {
            Hidden -> collapsed()
            Collapsed -> expand()
            Expand -> {
                collapsed()
                isAnimating.value = true
            }

        }
    }

    // -- 结束
}

