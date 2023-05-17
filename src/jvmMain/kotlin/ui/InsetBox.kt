package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InsetBox(modifier: Modifier = Modifier,
             title: String = "",
             shadowAlpha: Float = 0.1f, shadowWidth: Dp = 8.dp,
             content: @Composable ()->Unit
) = Box(modifier) {
    Shadow(Modifier.align(Alignment.TopStart), alpha = shadowAlpha, width = shadowWidth)
    Shadow(Modifier.align(Alignment.TopStart), Orientation.Vertical, alpha = shadowAlpha, width = shadowWidth)
    Shadow(Modifier.align(Alignment.TopEnd), Orientation.Vertical, true, alpha = shadowAlpha, width = shadowWidth)
    Shadow(Modifier.align(Alignment.BottomStart), reversed = true, alpha = shadowAlpha, width = shadowWidth)
    Box(Modifier.fillMaxSize().padding(shadowWidth)) { content() }
    Box(Modifier.align(Alignment.TopCenter).offset(y = (-8).dp).background(Color.White)) {
        Text(title, color = Color.Gray, fontSize = 16.sp)
    }
}

@Composable
fun Shadow(modifier: Modifier = Modifier,
           orientation: Orientation = Orientation.Horizontal, reversed: Boolean = false,
           alpha: Float = 0.1f, width: Dp = 8.dp
) {
    val mod = remember(orientation, width) {
        if (orientation == Orientation.Vertical)
            modifier.fillMaxHeight().width(width)
        else
            modifier.fillMaxWidth().height(width)
    }
    val colors = remember(reversed, alpha) {
        if (reversed)
            listOf(Color.Transparent, Color.Black.copy(alpha))
        else
            listOf(Color.Black.copy(alpha), Color.Transparent)
    }
    val gradient by remember(orientation) { derivedStateOf {
        if (orientation == Orientation.Vertical)
            Brush.horizontalGradient(colors)
        else
            Brush.verticalGradient(colors)
    } }
    Box(mod.background(gradient))
}