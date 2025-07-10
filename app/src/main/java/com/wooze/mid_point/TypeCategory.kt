package com.wooze.mid_point

import android.annotation.SuppressLint
import android.content.Context
import android.provider.OpenableColumns
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.wooze.mid_point.data.DragData

@SuppressLint("Recycle")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun typeCategory(data: DragData) {
    val context = LocalContext.current
    // TODO
    if (data.mimetype != null) {
        when {
            data.mimetype.startsWith("image/") || data.mimetype.startsWith("video/") -> { // 图片和视频
                GlideImage(
                    model = data.uri,
                    contentDescription = "picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            data.mimetype.startsWith("audio/") -> {
                FileWithName(context, data, "音频文件", R.drawable.icon_color_audio)
            }

//            data.mimetype.startsWith("application/") -> {
//                when (data.mimetype) {
//                    "application/zip" -> FileWithName(context,data,"压缩文件",R.drawable.icon_color_file)
//                }
//            }

            else -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column {
                        GlideImage(
                            model = R.drawable.icon_color_audio,
                            contentDescription = "audio",
                            contentScale = ContentScale.Crop
                        )
                        Text("音频文件")
                    }
                }
            }

        }

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FileWithName(context: Context, data: DragData, description: String, icon: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            GlideImage(
                model = icon,
                contentDescription = description,
                modifier = Modifier.size(50.dp)
            )
            var name: String = description
            val cursor = context.contentResolver.query(
                data.uri,
                arrayOf(OpenableColumns.DISPLAY_NAME),
                null,
                null,
                null
            )?.use {
                if (it.moveToFirst()) {
                    val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    name = it.getString(index)
                }
            }
            Text(description)
            Text("$name", fontSize = 10.sp)
        }
    }
}