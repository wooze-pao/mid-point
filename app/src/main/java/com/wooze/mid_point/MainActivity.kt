package com.wooze.mid_point

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.wooze.mid_point.tools.FloatWindowAction.closeFloatActivity
import com.wooze.mid_point.tools.FloatWindowAction.openFloatActivity
import com.wooze.mid_point.ui.homeScreenUi.HomeScreen
import com.wooze.mid_point.ui.theme.MidPointTheme
import com.wooze.mid_point.viewModel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TaskTest", "MainActivity taskId = $taskId")
        enableEdgeToEdge()
        setContent {
            MidPointTheme {
                HomeScreen({ openFloatActivity(this) }, { closeFloatActivity() }, viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkOverlayPermission(this)
    }
}

