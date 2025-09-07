package com.wooze.mid_point.viewModel

import android.app.StatusBarManager
import android.content.ComponentName
import android.content.Context
import android.graphics.drawable.Icon
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wooze.mid_point.MyApplication
import com.wooze.mid_point.R
import com.wooze.mid_point.data.DataStoreManager
import com.wooze.mid_point.service.FloatControlTile
import com.wooze.mid_point.state.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private val _haveOverlayPermission = mutableStateOf(false)

    lateinit var dataStoreManager: DataStoreManager
    val haveOverlayPermission: State<Boolean> = _haveOverlayPermission

    private val _isAutoCollapse = mutableStateOf(true)
    val isAutoCollapse: State<Boolean> = _isAutoCollapse

    fun checkOverlayPermission(context: Context): Boolean {
        _haveOverlayPermission.value = Settings.canDrawOverlays(context)
        return haveOverlayPermission.value
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestAddTile(context: Context) {
        val statusBarManager: StatusBarManager =
            context.getSystemService(StatusBarManager::class.java)
        val executor = java.util.concurrent.Executors.newSingleThreadExecutor()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            statusBarManager.requestAddTileService(
                ComponentName(context, FloatControlTile::class.java),
                context.getString(R.string.tile_label),
                Icon.createWithResource(context, R.drawable.ic_launcher_monochrome),
                executor
            ) { callback ->
                Log.d("mpDebug", "$callback")
            }
        }
    }

    suspend fun initData(context: Context) {
        val applicationContext = context.applicationContext as MyApplication
        dataStoreManager = applicationContext.dataStoreManager
        dataStoreManager.isAutoCollapse.collectLatest { value ->
            _isAutoCollapse.value = value
        }
    }

    fun changeData(boolean: Boolean) {
        _isAutoCollapse.value = boolean
        viewModelScope.launch {
            dataStoreManager.setAutoCollapse(boolean)
        }
    }

    val permissionLabel by derivedStateOf { if (haveOverlayPermission.value) R.string.permission_open else R.string.permission_close }

    val isShowing: State<Boolean> = UiState.isShowing

    val topWeight by derivedStateOf { if (isShowing.value) 1f else 4f }
    val bottomWeight by derivedStateOf { if (isShowing.value) 4f else 1f }

    val label by derivedStateOf { if (isShowing.value) R.string.state_open else R.string.state_close }

}