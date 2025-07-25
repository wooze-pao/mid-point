package com.wooze.mid_point.viewModel

import android.content.Context
import android.provider.Settings
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.wooze.mid_point.R
import com.wooze.mid_point.state.UiState

class MainViewModel : ViewModel() {

    private val _haveOverlayPermission = mutableStateOf(false)
    val haveOverlayPermission: State<Boolean> = _haveOverlayPermission

    fun checkOverlayPermission(context: Context): Boolean {
        _haveOverlayPermission.value = Settings.canDrawOverlays(context)
        return haveOverlayPermission.value
    }

    val permissionLabel by derivedStateOf { if (haveOverlayPermission.value) R.string.permission_open else R.string.permission_close }

    val isShowing: State<Boolean> = UiState.isShowing

    val topWeight by derivedStateOf { if (isShowing.value) 1f else 4f }
    val bottomWeight by derivedStateOf { if (isShowing.value) 4f else 1f }

    val label by derivedStateOf { if (isShowing.value) R.string.state_open else R.string.state_close }
}