package com.wooze.mid_point.tools

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wooze.mid_point.data.DragData
import com.wooze.mid_point.state.UiState

object DataTools {
    fun extractAndSave(uri: Uri, context: Context) {
        context.grantUriPermission(context.packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val mimetype = context.contentResolver.getType(uri)
        val dragData = DragData(uri, mimetype)
        UiState.dragDataList.add(dragData)
        FloatWindowAction.openFloatActivity(context)
    }

    fun extractAndSave(list: List<Uri>, context: Context) {
        list.forEach { uri ->
            context.grantUriPermission(
                context.packageName,
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            val mimetype = context.contentResolver.getType(uri)
            val dragData = DragData(uri, mimetype)
            UiState.dragDataList.add(dragData)
            FloatWindowAction.openFloatActivity(context)
        }
    }

    fun extractAndSave(event: DragAndDropEvent, context: Context) {
        val clipData = event.toAndroidDragEvent().clipData
        val count = clipData.itemCount
        for (i in 0..count - 1) {
            val uri = clipData.getItemAt(i).uri
            val plainText = clipData.getItemAt(i).text
            uri?.let { uri ->
                val mimetype = context.contentResolver.getType(uri)
                val dragData = DragData(uri, mimetype)
                UiState.dragDataList.add(dragData)
            }
            plainText?.let { text ->
                val mimeType = ClipDescription.MIMETYPE_TEXT_PLAIN
                val dragData = DragData(plainText = text.toString(), mimetype = mimeType)
                UiState.dragDataList.add(dragData)
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendData(context: Context): ClipData? {
        val list = UiState.dragDataList
        if (list.isEmpty()) {
            return null
        }

        val clipData = when(list[0].mimetype) {
            ClipDescription.MIMETYPE_TEXT_PLAIN -> ClipData.newPlainText("text",list[0].plainText)
            else -> ClipData.newUri(context.contentResolver,"uris",list[0].uri)
        }

        for (i in 1..list.size - 1) {
            val uri = list[i].uri
            val text = list[i].plainText
            val item = if (text != null) {
                ClipData.Item(text)
            } else {
                ClipData.Item(uri)
            }

            clipData.addItem(context.contentResolver,item) // 添加contentResolver 会自动添加 mimetype
        }
        return clipData

    }

    fun sendData(context: Context,data: DragData): ClipData {
        val clipData = if (data.plainText != null) {
            ClipData.newPlainText("text",data.plainText)
        } else {
            ClipData.newUri(
                context.contentResolver,
                "file",
                data.uri
            )
        }
        return clipData
    }

    fun shareData(uriList: SnapshotStateList<DragData>): Intent? {
        var intent : Intent
        var uriArrays = ArrayList<Uri>()
        for (i in 0..uriList.size - 1) {
            val uri = uriList[i].uri
            uri?.let { uriArrays.add(it) }
        }

        if (uriList.isEmpty()) {
            return null
        }

        if (uriArrays.size == 1) {
            intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_STREAM,uriArrays[0])
            }
        } else  {
            intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrays)
            }
        }

        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.type = "*/*"
        return intent

    }
}
