package com.wooze.mid_point.objects

import android.content.Context
import android.content.Intent
import com.wooze.mid_point.FloatActivity
import com.wooze.mid_point.state.UiState

object FloatWindowAction {
    fun toggleFloatWindow(context: Context, flag: Int? = null) {
        if (UiState.isShowing) {
            closeFloatActivity()
        } else {
            openFloatActivity(context, flag)
        }
    }

    fun openFloatActivity(context: Context, flag: Int? = null) {
        if (!UiState.isShowing) {
            val intent = Intent(context, FloatActivity::class.java)
            if (flag != null) {
                intent.addFlags(flag)
            }
            context.startActivity(intent)
        }
    }

    fun closeFloatActivity() {
        if (UiState.isShowing) {
            FloatActivity.Companion.closeFloat()
        }
    }
}