package com.wooze.mid_point

import com.wooze.mid_point.data.DragData

fun typeCategory(data: DragData): Any {
    // TODO
    if (data.mimetype != null) {
        return when {
            data.mimetype.startsWith("image/") || data.mimetype.startsWith("video/") -> { // 图片和视频
                data.uri
            }

            data.mimetype.startsWith("audio/") -> { // 音频
                R.drawable.icon_color_audio
            }

            data.mimetype.startsWith("text/") -> { // 文本
                R.drawable.icon_color__doc
            }

            else -> {
                R.drawable.icon_flat_img__1_
            }

        }
    }
    return R.drawable.icon_color_skt
}