package com.wooze.mid_point.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
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
        var autoCollapse: Boolean

        CoroutineScope(Dispatchers.Main).launch {
            autoCollapse = dataStoreManager.isAutoCollapse.first()
            val willShow = !UiState.isShowing.value
            if (UiState.isShowing.value) {
                FloatWindowAction.closeFloatActivity()
                if (autoCollapse) {
                    val intent = Intent(this@FloatControlTile, QSActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(
                        this@FloatControlTile,
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        startActivityAndCollapse(pendingIntent)
                    }
                }
            } else {
                val intent = Intent(this@FloatControlTile, FloatActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    this@FloatControlTile,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    @SuppressLint("StartActivityAndCollapseDeprecated")
                    startActivityAndCollapse(pendingIntent)
                } else {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    @Suppress("DEPRECATION")
                    startActivityAndCollapse(intent)
                }
            }
            qsTile.state = if (willShow) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                qsTile.subtitle = if (willShow) "开启中" else "未开启"
            }
            qsTile.updateTile()
        }

    }

    override fun onStartListening() {
        super.onStartListening()
        qsTile.state = if (UiState.isShowing.value) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile.updateTile()
    }
}