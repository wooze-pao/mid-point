package com.wooze.mid_point

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wooze.mid_point.objects.FloatWindowAction.closeFloatActivity
import com.wooze.mid_point.objects.FloatWindowAction.openFloatActivity
import com.wooze.mid_point.ui.homeScreenUi.HomeScreen
import com.wooze.mid_point.ui.theme.MidPointTheme
import com.wooze.mid_point.viewModel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TaskTest", "MainActivity taskId = $taskId")
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            MidPointTheme {
                HomeScreen({ openFloatActivity(this) }, { closeFloatActivity() }, viewModel)
            }
        }
    }
}

