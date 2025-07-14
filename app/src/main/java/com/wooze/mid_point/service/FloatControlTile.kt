package com.wooze.mid_point.service

import android.content.Intent
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.wooze.mid_point.tools.FloatWindowAction
import com.wooze.mid_point.state.UiState

class FloatControlTile : TileService() {
    override fun onClick() {
        super.onClick()
        FloatWindowAction.toggleFloatWindow(
            this,
            Intent.FLAG_ACTIVITY_NEW_TASK
        ) // objects -> FloatWindowAction 中定义
    }

    override fun onStartListening() {
        super.onStartListening()
        qsTile.state = if (UiState.isShowing.value) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile.updateTile()
    }
}