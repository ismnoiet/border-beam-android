//
//  DemoScreen.kt
//
//  Author: Repgem
//  Minimum supported: Android 7.0 (API 24)
//
//  Shared demo frame. Title on the left, a plain "@repgem" label on the right, your content below.
//  Pass `onBack` to show a back chevron before the title (an app that opens the demo does; a package
//  preview leaves it out).
//  Source of truth: common/android/DemoScreen.kt in the catalog repo. Do not edit the copies inside
//  items; edit this file and run `bun run sync-common` (it rewrites the package line per item).
//
package com.repgem.borderbeam.demo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Spacing shared by every demo screen. The iOS frame uses the same numbers in points. */
internal object DemoLayout {
    val side = 20.dp
    val top = 12.dp
    val bottom = 16.dp
    val gap = 12.dp
}

@Composable
internal fun DemoScreen(
    title: String,
    background: Color,
    titleColor: Color,
    modifier: Modifier = Modifier,
    scrolls: Boolean = true,
    /** When set, a back chevron appears before the title and calls this. Null by default. */
    onBack: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier
            .fillMaxSize()
            .background(background)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = DemoLayout.side, end = DemoLayout.side, top = DemoLayout.top, bottom = DemoLayout.bottom),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DemoLayout.gap),
        ) {
            if (onBack != null) {
                BackChevron(titleColor, onBack)
            }
            BasicText(
                title,
                Modifier.weight(1f),
                style = TextStyle(color = titleColor, fontSize = 17.sp, fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            BasicText(
                "@repgem",
                style = TextStyle(color = titleColor.copy(alpha = 0.5f), fontSize = 13.sp, fontWeight = FontWeight.Medium),
                maxLines = 1,
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .then(if (scrolls) Modifier.verticalScroll(rememberScrollState()) else Modifier),
            content = content,
        )
    }
}

/** A drawn chevron, so the frame needs no icon library. */
@Composable
private fun BackChevron(color: Color, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Canvas(
        modifier
            .size(32.dp)
            .clickable(onClickLabel = "Back", role = Role.Button, onClick = onBack),
    ) {
        val w = 2.2.dp.toPx()
        val x = size.width * 0.38f
        val y = size.height / 2f
        val r = 6.dp.toPx()
        val chevron = Path().apply {
            moveTo(x + r * 0.55f, y - r)
            lineTo(x - r * 0.45f, y)
            lineTo(x + r * 0.55f, y + r)
        }
        drawPath(chevron, color, style = Stroke(width = w, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}
