package com.wooze.mid_point

import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.wooze.mid_point.state.FloatState
import com.wooze.mid_point.ui.FloatWindow


class FloatActivity : ComponentActivity() {
    private lateinit var floatWindowManager: WindowManager
    private lateinit var floatComposeView: ComposeView
    private lateinit var floatLifecycle: FloatComposeLifecycle

    override fun onCreate(savedInstanceState: Bundle?) { // 创建
        super.onCreate(savedInstanceState)
        instance = this
        moveTaskToBack(true)
        showFloatWindow()
    }

    override fun onDestroy() { // 清除
        super.onDestroy()
        hideFloatWindow() // 隐藏
    }

    fun showFloatWindow() {
        floatLifecycle = FloatComposeLifecycle()
        floatLifecycle.performRestore(null)
        floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        floatWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        floatComposeView = ComposeView(this)
        floatComposeView.setContent {
            FloatWindow()
        }
        floatComposeView.setViewTreeLifecycleOwner(floatLifecycle)
        floatComposeView.setViewTreeSavedStateRegistryOwner(floatLifecycle)

        @Suppress("DEPRECATION")
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  //版本检测
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT // 透明背景
        )
        params.gravity = Gravity.TOP or Gravity.START // 从左上角开始
        params.x = 0
        params.y = 300

        floatWindowManager.addView(floatComposeView, params)
        FloatState.isShowing = true
        floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
        floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    fun hideFloatWindow() {
        if (FloatState.isShowing) {
            floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            floatWindowManager.removeView(floatComposeView)
            FloatState.isShowing = false
        } else {
            // TODO 对于在未开启状态下的关闭
        }
    }

    companion object {
        private var instance: FloatActivity? = null //示例

        fun closeFloat() {
            if (FloatState.isShowing) {
                instance?.finish()
            }
        }
    }
}