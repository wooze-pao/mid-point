package com.wooze.mid_point

import com.wooze.mid_point.data.DragData

fun typeCategory(data: DragData): Any {
    // TODO
    if (data.mimetype != null) {
        return when {
            data.mimetype.startsWith("image/") -> {
                data.uri
            }

            else -> {
                R.drawable.icon_flat_img__1_
            }

        }
    }
    return 1
}