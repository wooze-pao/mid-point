package com.wooze.mid_point

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.wooze.mid_point.tools.DataTools
import org.w3c.dom.Text

class ShareActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ShareActivity", "onCreate called")
        moveTaskToBack(true)
        handleShareIntent(intent)
        finish()
    }


    @SuppressLint("UnsafeIntentLaunch")
    private fun handleShareIntent(intent: Intent) {
        var uri: Uri?
        var text: String?
        if (intent.action == Intent.ACTION_SEND) {
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                uri = intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                uri = intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri
            }
            text = intent.getStringExtra(Intent.EXTRA_TEXT)
            text?.let {text ->
                DataTools.extractAndSave(text,this)
            }
            uri?.let { uri ->
                DataTools.extractAndSave(uri, this)
            }
        } else if (intent.action == Intent.ACTION_SEND_MULTIPLE) {
            val uriList: List<Uri>? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
            }

            uriList?.let { uri ->
                DataTools.extractAndSave(uri, this)
            }
        }
    }
}