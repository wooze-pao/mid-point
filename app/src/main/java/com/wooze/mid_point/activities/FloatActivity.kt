package com.wooze.mid_point.activities

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.wooze.mid_point.FloatComposeLifecycle
import com.wooze.mid_point.data.WindowState
import com.wooze.mid_point.service.FloatControlTile
import com.wooze.mid_point.state.UiState
import com.wooze.mid_point.ui.floatWindowUi.FloatWindow
import com.wooze.mid_point.viewModel.FloatViewModel
import kotlinx.coroutines.launch

class FloatActivity : ComponentActivity() {
    private lateinit var floatWindowManager: WindowManager
    private lateinit var floatComposeView: ComposeView
    private lateinit var floatLifecycle: FloatComposeLifecycle
    private val viewModel by viewModels<FloatViewModel>()
    private var hasBeenOpen = false

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

    override fun onResume() {
        // 在后台打开回到app
        super.onResume()
        if (hasBeenOpen) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            hasBeenOpen = true
        }
        moveTaskToBack(true)
    }

    @SuppressLint("ClickableViewAccessibility", "Recycle")
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
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT,// 透明背景

        )
        params.gravity = Gravity.TOP or Gravity.START // 从左上角开始
        params.x = viewModel.position.value.x
        params.y = viewModel.position.value.y
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // 当安卓9以上
            // 可以参考官方的edgeToEdge函数
            // 在安卓15以上会自动解读为 ALWAYS 所以不用特地检测
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
        floatLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        floatWindowManager.addView(floatComposeView, params)


        var animate: ValueAnimator? = null
        UiState.isShowing.value = true  // 设定为true
        TileService.requestListeningState(this, ComponentName(this, FloatControlTile::class.java))
        lifecycleScope.launch {
            viewModel.position.collect { point ->
                if (!viewModel.inDrag) {

                    val currentX = params.x
                    if (animate != null) {
                        animate?.cancel()
                    }
                    animate = ValueAnimator.ofInt(currentX, point.x)
                    animate.duration = 200
                    animate.addUpdateListener {
                        val x = it.animatedValue as Int
                        params.x = x
                        params.y = point.y
                        floatWindowManager.updateViewLayout(floatComposeView, params)
                    }
                    animate.start()
                } else {
                    params.x = point.x
                    params.y = point.y
                    floatWindowManager.updateViewLayout(floatComposeView, params)
                }

            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun outsideTouch() {
        floatComposeView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_OUTSIDE -> {
                        if (viewModel.windowState.value == WindowState.Expand) {
                            viewModel.collapsed()
                            viewModel.isAnimating.value = true // 如果在动画移动中改变大小会导致卡顿，所以使用这个来检测完成
                        } else {
                            viewModel.hidden()
                        }
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