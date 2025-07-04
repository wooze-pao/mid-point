package com.wooze.mid_point

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wooze.mid_point.objects.FloatWindowAction.closeFloatActivity
import com.wooze.mid_point.objects.FloatWindowAction.openFloatActivity
import com.wooze.mid_point.ui.HomeScreen
import com.wooze.mid_point.ui.theme.MidPointTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MidPointTheme {
                HomeScreen({ openFloatActivity(this) }, { closeFloatActivity() })
            }
        }
    }
}

