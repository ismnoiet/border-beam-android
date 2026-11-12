//
//  DemoSupport.kt
//  BorderBeamDemo
//
//  Author: ismnoiet
//  Minimum supported: Android 7.0 (API 24)
//
//  Small helpers shared by the demo screens (plain Compose foundation, no Material needed).
//
package com.ismnoiet.borderbeam.demo

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** Text with a size in sp. */
@Composable
internal fun Txt(
    text: String,
    size: Float,
    color: Color = Color.White,
    weight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TextStyle(color = color, fontSize = size.sp, fontWeight = weight),
        maxLines = maxLines,
    )
}

/** Plain text field with a placeholder. */
@Composable
internal fun PlainField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    size: Float,
    textColor: Color,
    hintColor: Color,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = singleLine,
        textStyle = TextStyle(color = textColor, fontSize = size.sp),
        cursorBrush = SolidColor(textColor),
        decorationBox = { inner ->
            Box {
                if (value.isEmpty()) Txt(placeholder, size, hintColor)
                inner()
            }
        },
    )
}

/**
 * A tappable control that does nothing by default (demo buttons). Like a plain `Button` on iOS,
 * it dims and shrinks slightly while pressed, so it feels clickable.
 */
internal fun Modifier.tappable(onClick: () -> Unit = {}): Modifier = composed {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val alpha by animateFloatAsState(if (pressed) 0.55f else 1f, tween(90), label = "pressAlpha")
    val scale by animateFloatAsState(if (pressed) 0.95f else 1f, tween(90), label = "pressScale")
    this
        .graphicsLayer { this.alpha = alpha; scaleX = scale; scaleY = scale }
        .clickable(interactionSource = source, indication = null, onClick = onClick)
}
