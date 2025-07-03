package com.wooze.mid_point

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wooze.mid_point.ui.HomeScreen
import com.wooze.mid_point.ui.theme.MidPointTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MidPointTheme {
                HomeScreen({ openFloatActivity() }, { closeFloatActivity() })
            }
        }
    }

    fun openFloatActivity() {
        val intent = Intent(this, FloatActivity::class.java)
        startActivity(intent)
    }

    fun closeFloatActivity() {
        FloatActivity.Companion.closeFloat()
    }
}

