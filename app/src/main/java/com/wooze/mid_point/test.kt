package com.wooze.mid_point

import android.icu.number.Scale
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.wooze.mid_point.data.Corner

data class BoxItem(val zIndex: Float, val scale: Float, val color: Color, val offset: Dp)




//@OptIn(ExperimentalSharedTransitionApi::class)
//@Composable
//@Preview
//fun StackUiDemo() {
//    var clicked by remember { mutableStateOf(false) }
//    LazyColumn(modifier = Modifier.fillMaxSize()) {
//        item {
//            SharedTransitionLayout {
//                AnimatedContent(
//                    targetState = clicked,
//                    transitionSpec = { fadeIn().togetherWith(fadeOut()) }
//                ) { state ->
//                    if (!state) {
//                        Box(Modifier.padding(100.dp)) {
//                            BoxItems.reversed().forEach { item ->
//                                if (!state) {
//
//                                }
//
//                            }
//                        }
//                    } else {
//                        Column (Modifier.padding(100.dp)) {
//                            BoxItems.forEach { item ->
//                                BoxExp(
//                                    Modifier
//                                        .sharedElement(
//                                            rememberSharedContentState(item.zIndex),
//                                            this@AnimatedContent
//                                        )
//                                        .clickable(onClick = {clicked = !clicked}),
//                                    item.color
//                                )
//                                Spacer(Modifier.height(10.dp))
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}


@Composable
fun BoxExp(modifier: Modifier = Modifier, color: Color) {
    Box(
        modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(color)

    )
}