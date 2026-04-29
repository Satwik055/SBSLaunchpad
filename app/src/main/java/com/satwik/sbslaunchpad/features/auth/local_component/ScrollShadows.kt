package com.satwik.sbslaunchpad.features.auth.local_component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ScrollShadows(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    shadowHeight: Dp = 24.dp,
    shadowColor: Color = Color.Black.copy(alpha = 0.08f),
    content: @Composable () -> Unit
) {
    val showTopShadow by remember { derivedStateOf { scrollState.canScrollBackward } }
    val showBottomShadow by remember { derivedStateOf { scrollState.canScrollForward } }

    val topAlpha by animateFloatAsState(
        targetValue = if (showTopShadow) 1f else 0f,
        label = "topShadow"
    )
    val bottomAlpha by animateFloatAsState(
        targetValue = if (showBottomShadow) 1f else 0f,
        label = "bottomShadow"
    )

    Box(modifier = modifier) {
        content()

        // Top shadow
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(shadowHeight)
                .graphicsLayer { alpha = topAlpha }
                .background(
                    Brush.verticalGradient(listOf(shadowColor, Color.Transparent))
                )
        )

        // Bottom shadow
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(shadowHeight)
                .graphicsLayer { alpha = bottomAlpha }
                .background(
                    Brush.verticalGradient(listOf(Color.Transparent, shadowColor))
                )
        )
    }
}

@Composable
fun LazyScrollShadows(
    scrollState: LazyListState,
    modifier: Modifier = Modifier,
    shadowHeight: Dp = 24.dp,
    shadowColor: Color = Color.Black.copy(alpha = 0.08f),
    content: @Composable () -> Unit
) {
    val showTopShadow by remember { derivedStateOf { scrollState.canScrollBackward } }
    val showBottomShadow by remember { derivedStateOf { scrollState.canScrollForward } }

    val topAlpha by animateFloatAsState(
        targetValue = if (showTopShadow) 1f else 0f,
        label = "topShadow"
    )
    val bottomAlpha by animateFloatAsState(
        targetValue = if (showBottomShadow) 1f else 0f,
        label = "bottomShadow"
    )

    Box(modifier = modifier) {
        content()

        // Top shadow
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(shadowHeight)
                .graphicsLayer { alpha = topAlpha }
                .background(
                    Brush.verticalGradient(listOf(shadowColor, Color.Transparent))
                )
        )

        // Bottom shadow
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(shadowHeight)
                .graphicsLayer { alpha = bottomAlpha }
                .background(
                    Brush.verticalGradient(listOf(Color.Transparent, shadowColor))
                )
        )
    }
}
