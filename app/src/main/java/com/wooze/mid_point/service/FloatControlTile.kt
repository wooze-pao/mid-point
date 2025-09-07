package com.wooze.mid_point.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.compose.runtime.snapshotFlow
import com.wooze.mid_point.MyApplication
import com.wooze.mid_point.activities.FloatActivity
import com.wooze.mid_point.activities.QSActivity
import com.wooze.mid_point.state.UiState
import com.wooze.mid_point.tools.FloatWindowAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FloatControlTile : TileService() {
    @SuppressLint("StartActivityAndCollapseDeprecated")
    override fun onClick() {
        super.onClick()
        val applicationContext = this.applicationContext as MyApplication
        val dataStoreManager = applicationContext.dataStoreManager
        CoroutineScope(Dispatchers.Main).launch {
            val willShow = !UiState.isShowing.value
            if (UiState.isShowing.value) {
                FloatWindowAction.closeFloatActivity()
                val autoCollapse = dataStoreManager.isAutoCollapse.first()
                if (autoCollapse) {
                    val intent = Intent(this@FloatControlTile, QSActivity::class.java)
                    openActivity(intent)
                }
            } else {
                val intent = Intent(this@FloatControlTile, FloatActivity::class.java)
                openActivity(intent)
            }
            qsTile.state = if (willShow) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                qsTile.subtitle = if (willShow) "开启中" else "未开启"
            }
            qsTile.updateTile()
        }

    }

    @SuppressLint("StartActivityAndCollapseDeprecated")
    fun openActivity(intent: Intent) {
        val pendingIntent = PendingIntent.getActivity(
            this@FloatControlTile,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startActivityAndCollapse(pendingIntent)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            @Suppress("DEPRECATION")
            startActivityAndCollapse(intent)
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        CoroutineScope(Dispatchers.Main).launch {
            val isShowingFlow = snapshotFlow { UiState.isShowing.value }
            isShowingFlow.collect { bool ->
                qsTile.state = if (bool) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                qsTile.updateTile()
            }
        }
    }
}