package com.wooze.mid_point

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.wooze.mid_point.tools.DataTools

class ShareActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moveTaskToBack(true)
        handleShareIntent(intent)
        finish()
    }


    @SuppressLint("UnsafeIntentLaunch")
    private fun handleShareIntent(intent: Intent) {
        var uri: Uri?
        var text: String?
        when (intent.action) {
            Intent.ACTION_SEND -> { // 单个分享
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    uri = intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    uri = intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri
                }
                text = intent.getStringExtra(Intent.EXTRA_TEXT)
                text?.let { text ->
                    DataTools.extractAndSave(text, this)
                }
                uri?.let { uri ->
                    DataTools.extractAndSave(uri, this)
                }
            }

            Intent.ACTION_SEND_MULTIPLE -> { // 多个分享
                val uriList: List<Uri>? =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
                    }

                uriList?.let { uri ->
                    DataTools.extractAndSave(uri, this)
                }
            }

            Intent.ACTION_VIEW -> { // 打开方式分享
                val uri = intent.data
                uri?.let { uri ->
                    DataTools.extractAndSave(uri, this)
                }
            }
        }
    }
}