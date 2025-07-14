package com.wooze.mid_point.tools

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.toAndroidDragEvent
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
            uri?.let { uri ->
                val mimetype = context.contentResolver.getType(uri)
                val dragData = DragData(uri, mimetype)
                UiState.dragDataList.add(dragData)
            }
        }
    }
}
