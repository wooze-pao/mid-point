package com.wooze.mid_point.service

import android.content.Intent
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import com.wooze.mid_point.FloatActivity
import com.wooze.mid_point.state.UiState

class FloatControlTile : TileService() {
    override fun onClick() {
        super.onClick()
        if (!UiState.isShowing) {
            val intent = Intent(this, FloatActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }else {
            FloatActivity.closeFloat()
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        qsTile.state = if(UiState.isShowing) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile.updateTile()
    }
}