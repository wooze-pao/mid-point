package com.wooze.mid_point.ui.floatWindowUi

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wooze.mid_point.R
import com.wooze.mid_point.data.Corner
import com.wooze.mid_point.state.UiState
import com.wooze.mid_point.tools.DataTools
import com.wooze.mid_point.typeCategory
import com.wooze.mid_point.ui.addon.dragOutData
import com.wooze.mid_point.viewModel.FloatViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
@OptIn(ExperimentalFoundationApi::class)
fun CollapsedMode(context: Context, viewModel: FloatViewModel) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .requiredHeight(100.dp)
            .requiredWidth(130.dp)
            .clip(RoundedCornerShape(Corner.Inner))
            .background(Color.Gray)
            .dragOutData(DataTools.sendData(context)) {
                viewModel.toggleState()
            },
        contentAlignment = Alignment.Center
    ) {
        if (UiState.groupDragList.isNotEmpty()) {
            val lastData = UiState.groupDragList.flatten().last()
            typeCategory(lastData)

        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.drag_tip_text_top))
                Text(stringResource(R.string.drag_tip_text_bottom))
            }
        }
    }
}