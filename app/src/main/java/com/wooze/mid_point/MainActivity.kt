package com.wooze.mid_point

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wooze.mid_point.objects.FloatWindowAction.closeFloatActivity
import com.wooze.mid_point.objects.FloatWindowAction.openFloatActivity
import com.wooze.mid_point.ui.HomeScreen
import com.wooze.mid_point.ui.theme.MidPointTheme
import com.wooze.mid_point.viewModel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel = MainViewModel()
        Log.d("TaskTest", "MainActivity taskId = $taskId")
        enableEdgeToEdge()
        setContent {
            MidPointTheme {
                HomeScreen({ openFloatActivity(this) }, { closeFloatActivity() }, viewModel)
            }
        }
    }
}

