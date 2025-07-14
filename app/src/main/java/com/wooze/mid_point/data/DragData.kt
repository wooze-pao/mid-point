package com.wooze.mid_point.data

import android.net.Uri
import java.util.UUID

data class DragData(val uri: Uri, val mimetype: String?,val id: String = UUID.randomUUID().toString()) // 新增唯一id
