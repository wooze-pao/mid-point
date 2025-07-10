package com.wooze.mid_point.ui.floatWindowUi

import android.content.ClipData
import android.content.Context
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.wooze.mid_point.state.UiState
import com.wooze.mid_point.typeCategory
import com.wooze.mid_point.viewModel.FloatViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun ExpandMode (context: Context) {
    LazyColumn {
        UiState.dragDataList.forEach { data ->
            item {// TODO 完善悬浮窗
                if (data.mimetype != null) {
                    Box(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(20))
                            .dragAndDropSource {
                                detectTapGestures(onLongPress = {
                                    startTransfer(
                                        DragAndDropTransferData(
                                            clipData = ClipData.newUri(
                                                context.contentResolver,
                                                "file",
                                                data.uri
                                            ),
                                            flags = View.DRAG_FLAG_GLOBAL or View.DRAG_FLAG_GLOBAL_URI_READ
                                        )
                                    )
                                })
                            }
                    ) {
                        GlideImage(
                            model = typeCategory(data),
                            contentDescription = "picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}