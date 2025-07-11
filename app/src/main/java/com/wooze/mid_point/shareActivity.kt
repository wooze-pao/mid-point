package com.wooze.mid_point

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.wooze.mid_point.data.DragData
import com.wooze.mid_point.objects.FloatWindowAction
import com.wooze.mid_point.state.UiState

class ShareActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ShareActivity", "onCreate called")
        moveTaskToBack(true)
        handleShareIntent(intent)
        finish()
    }


    @SuppressLint("UnsafeIntentLaunch")
    private fun handleShareIntent(intent: Intent?) {
        var uri: Uri?
        if (intent?.action == Intent.ACTION_SEND) {
            uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri
            }
            uri?.let { uri ->
                grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val mimetype = contentResolver.getType(uri)
                val dragData = DragData(uri, mimetype)
                UiState.dragDataList.add(dragData)
                FloatWindowAction.openFloatActivity(this)
            }
        }
        else if (intent?.action == Intent.ACTION_SEND_MULTIPLE) {
            val uriList: List<Uri>? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
            }

            uriList?.let {
                it.forEach {uri ->
                    grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    val mimetype = contentResolver.getType(uri)
                    val dragData = DragData(uri, mimetype)
                    UiState.dragDataList.add(dragData)
                    FloatWindowAction.openFloatActivity(this)
                }
            }
        }
    }
}