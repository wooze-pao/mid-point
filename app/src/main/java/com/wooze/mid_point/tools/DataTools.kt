package com.wooze.mid_point.tools

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import com.wooze.mid_point.data.DragData
import com.wooze.mid_point.state.UiState

object DataTools {
    fun extractAndSave(uri: Uri, context: Context) {
        context.grantUriPermission(context.packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val mimetype = context.contentResolver.getType(uri)
        val dragData = DragData(uri, mimetype)
        UiState.groupDragList.add(mutableStateListOf(dragData))
        FloatWindowAction.openFloatActivity(context)
    }

    fun extractAndSave(text: String, context: Context) {
        val mimetype = ClipDescription.MIMETYPE_TEXT_PLAIN
        val dragData = DragData(plainText = text, mimetype = mimetype)
        UiState.groupDragList.add(mutableStateListOf(dragData))
        FloatWindowAction.openFloatActivity(context)
    }

    fun extractAndSave(list: List<Uri>, context: Context) {
        val newList = mutableStateListOf<DragData>()
        list.forEach { uri ->
            context.grantUriPermission(
                context.packageName,
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            val mimetype = context.contentResolver.getType(uri)
            val dragData = DragData(uri, mimetype)
            newList.add(dragData)
            FloatWindowAction.openFloatActivity(context)
        }
        UiState.groupDragList.add(newList)
    }

    fun extractAndSave(event: DragAndDropEvent, context: Context) {
        val clipData = event.toAndroidDragEvent().clipData
        val count = clipData.itemCount
        val newList = mutableStateListOf<DragData>()
        for (i in 0..count - 1) {
            val uri = clipData.getItemAt(i).uri
            val plainText = clipData.getItemAt(i).text
            uri?.let { uri ->
                val mimetype = context.contentResolver.getType(uri)
                val dragData = DragData(uri, mimetype)
                newList.add(dragData)
            }
            plainText?.let { text ->
                val mimeType = ClipDescription.MIMETYPE_TEXT_PLAIN
                val dragData = DragData(plainText = text.toString(), mimetype = mimeType)
                newList.add(dragData)
            }
        }
        UiState.groupDragList.add(newList)
    }


    fun sendData(context: Context): ClipData? {
        val groupList = UiState.groupDragList
        if (groupList.isEmpty()) {
            return null
        }
        var clipData: ClipData? = null
        for (group in 0..groupList.size - 1) {
            val list = groupList[group]
            var startIndex = 0


            if (clipData == null) {
                clipData = when (list[0].mimetype) {
                    ClipDescription.MIMETYPE_TEXT_PLAIN -> ClipData.newPlainText(
                        "text",
                        list[0].plainText
                    )

                    else -> ClipData.newUri(context.contentResolver, "uris", list[0].uri)
                }
                startIndex = 1
            }

            for (i in startIndex..list.size - 1) {
                val uri = list[i].uri
                val text = list[i].plainText
                val item = if (text != null) {
                    ClipData.Item(text)
                } else {
                    ClipData.Item(uri)
                }

                clipData.addItem(context.contentResolver, item) // 添加contentResolver 会自动添加 mimetype
            }
        }

        return clipData

    }

    fun sendData(context: Context, data: DragData): ClipData {
        val clipData = if (data.plainText != null) {
            ClipData.newPlainText("text", data.plainText)
        } else {
            ClipData.newUri(
                context.contentResolver,
                "file",
                data.uri
            )
        }
        return clipData
    }

    fun sendData(context: Context, data: List<DragData>): ClipData {
        val clipData = when (data[0].mimetype) {
            ClipDescription.MIMETYPE_TEXT_PLAIN -> ClipData.newPlainText(
                "text",
                data[0].plainText
            )

            else -> ClipData.newUri(context.contentResolver, "uris", data[0].uri)
        }
        data.drop(1).forEach { item ->
            val uri = item.uri
            val text = item.plainText
            val item = if (text != null) {
                ClipData.Item(text)
            } else {
                ClipData.Item(uri)
            }

            clipData.addItem(context.contentResolver, item)
        }
        return clipData!!
    }

    fun shareData(uriList: List<DragData>): Intent? {
        // TODO plaintext 的分享
        Log.d("mpDebug", "${uriList}")
        if (uriList.isEmpty()) {
            return null
        }

        var intent: Intent
        val uriArrays = ArrayList<Uri>()
        val textArrays = ArrayList<String>()
        for (i in 0..uriList.size - 1) {
            val text = uriList[i].plainText
            val uri = uriList[i].uri
            uri?.let { uriArrays.add(it) }
            text?.let { textArrays.add(it) }
        }

        intent = if (uriArrays.size == 1) {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_STREAM, uriArrays[0])
            }
        } else if (uriArrays.size > 1) {
            Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrays)
                putExtra(Intent.EXTRA_TEXT, "hihi")
            }
        } else {
            // 单独分享文字组合 因为似乎这种才能被其他应用识别
            val fullText = textArrays.joinToString("\n\n")
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, fullText)
            }
        }

        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.type = "*/*"
        return intent

    }
}
