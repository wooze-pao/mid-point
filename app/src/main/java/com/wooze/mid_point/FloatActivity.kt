package com.wooze.mid_point

import android.annotation.SuppressLint
import android.content.ComponentName
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.service.quicksettings.TileService
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.wooze.mid_point.service.FloatControlTile
import com.wooze.mid_point.state.UiState
import com.wooze.mid_point.ui.floatWindowUi.FloatWindow
import com.wooze.mid_point.viewModel.FloatViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FloatActivity : ComponentActivity() {
    private lateinit var floatWindowManager: WindowManager
    private lateinit var floatComposeView: ComposeView
    private lateinit var floatLifecycle: FloatComposeLifecycle
    private val viewModel by viewModels<FloatViewModel>()

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) { // 创建
        super.onCreate(savedInstanceState)
        instance = this
        showFloatWindow()
        moveTaskToBack(true)
    }

    override fun onDestroy() { // 清除
        super.onDestroy()
        hideFloatWindow() // 隐藏
    }

    // TODO
    @SuppressLint("ClickableViewAccessibility")
    fun showFloatWindow() {
        floatLifecycle = FloatComposeLifecycle()
        floatLifecycle.performRestore(null)
        floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        floatWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        floatComposeView = ComposeView(this)
        floatComposeView.setViewTreeLifecycleOwner(floatLifecycle)
        floatComposeView.setViewTreeSavedStateRegistryOwner(floatLifecycle)
        floatComposeView.setViewTreeViewModelStoreOwner(this)
        floatComposeView.setContent {
            FloatWindow(viewModel)
        }

        outsideTouch()

        @Suppress("DEPRECATION")
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT // 透明背景
        )
        params.gravity = Gravity.TOP or Gravity.START // 从左上角开始
        params.x = 0
        params.y = 300

        floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
        floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        floatWindowManager.addView(floatComposeView, params)
        UiState.isShowing.value = true  // 设定为true
        TileService.requestListeningState(this, ComponentName(this, FloatControlTile::class.java))
        // 在compose添加完了延时 触发重新测量 修复不触发bug（测试）
        floatComposeView.postDelayed({
            floatWindowManager.updateViewLayout(floatComposeView,params)
        },200)

    }

    @SuppressLint("ClickableViewAccessibility")
    fun outsideTouch () {
        floatComposeView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_OUTSIDE -> {
                        viewModel.hidden()
                        return true
                    }
                }
                return false
            }
        })
    }

    fun hideFloatWindow() {
        if (!UiState.isShowing.value) { // 如果悬浮窗没有再显示直接返回
            return
        }
        floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        floatWindowManager.removeView(floatComposeView)
        UiState.isShowing.value = false
        viewModel.resetFloatData()
        TileService.requestListeningState(this, ComponentName(this, FloatControlTile::class.java))
    }

    companion object {
        private var instance: FloatActivity? = null //实例

        fun closeFloat() {
            if (UiState.isShowing.value) {
                instance?.finish()
            }
        }
    }
}