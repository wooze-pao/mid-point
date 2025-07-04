package com.wooze.mid_point.data

import android.content.ClipData
import android.net.Uri

data class DragData(val uri: Uri, val clipData: ClipData, val mimetype: String?)
