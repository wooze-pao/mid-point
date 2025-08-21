package com.wooze.mid_point.data

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object SizesDp {
    // 窗口尺寸
    val WINDOW_WIDTH: Dp = 150.dp
    val WINDOW_HEIGHT: Dp = 120.dp
    val WINDOW_EXPAND: Dp = 500.dp
    val WINDOW_WID_GAP: Dp = 20.dp
    val WINDOW_GAP_R: Dp = - (WINDOW_WIDTH - WINDOW_WID_GAP)

    // 窗口内卡片尺寸
    val ITEM_WIDTH: Dp = 130.dp
    val ITEM_HEIGHT: Dp = 100.dp

    // 圆角
    val R_SHADOW: Dp = 35.dp
    val R_OUTER: Dp = 30.dp
    val R_INNER: Dp = 20.dp
    val R_SELECTOR: Dp = 15.dp
}