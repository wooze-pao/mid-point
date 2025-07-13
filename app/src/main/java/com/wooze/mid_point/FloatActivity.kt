package com.wooze.mid_point

import android.annotation.SuppressLint
import android.content.ComponentName
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.service.quicksettings.TileService
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
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
    private lateinit var viewModel: FloatViewModel

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) { // 创建
        super.onCreate(savedInstanceState)
        viewModel = FloatViewModel()
        instance = this
        showFloatWindow()
        lifecycle.coroutineScope.launch {
            delay(100) // 给予一点延时显示，防止系统销毁（配合excludeFromRecents）
            moveTaskToBack(true)
        }
    }

    override fun onDestroy() { // 清除
        super.onDestroy()
        hideFloatWindow() // 隐藏
    }

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

        @Suppress("DEPRECATION")
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  //版本检测
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT // 透明背景
        )
        params.gravity = Gravity.TOP or Gravity.START // 从左上角开始
        params.x = 0
        params.y = 300

        floatWindowManager.addView(floatComposeView, params)
        UiState.isShowing.value = true  // 设定为true
        TileService.requestListeningState(this, ComponentName(this, FloatControlTile::class.java))
        floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
        floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
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