package com.wooze.mid_point.data

import android.net.Uri
import java.util.UUID

data class DragData(
    val uri: Uri? = null,
    val mimetype: String?,
    val id: String = UUID.randomUUID().toString(),
    val plainText: String? = null
) // 新增唯一id
