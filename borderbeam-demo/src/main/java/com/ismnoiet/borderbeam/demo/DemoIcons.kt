//
//  DemoIcons.kt
//  BorderBeamDemo
//
//  Author: ismnoiet
//  Minimum supported: Android 7.0 (API 24)
//
//  Small stroke icons that mirror the SF Symbols used by the iOS demo (chevron.down, arrow.up, …).
//  Each one is plain SVG path data on a 24 x 24 grid, drawn with a rounded stroke, so the demo
//  needs no icon library and the glyphs scale and tint like text.
//
package com.ismnoiet.borderbeam.demo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

/** One SVG path. `fill` paints it solid; `cut` draws it in the cut-out color (for "filled" symbols). */
internal class Part(val d: String, val fill: Boolean = false, val cut: Boolean = false)

/** The symbols used by the demos, named after their SF Symbols counterparts. */
internal enum class Sym(vararg val parts: Part) {
    ChevronDown(Part("M4.5 8.5l7.5 7.5 7.5-7.5")),
    ArrowUp(Part("M12 20V4"), Part("M5 11l7-7 7 7")),
    Plus(Part("M12 4v16"), Part("M4 12h16")),
    Checkmark(Part("M4.5 12.5l5 5L19.5 7")),
    CheckCircleFill(
        Part("M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20z", fill = true),
        Part("M7.5 12.5l3.2 3.2 5.8-6.4", cut = true),
    ),
    Cloud(Part("M17.5 19H9a7 7 0 1 1 6.71-9h1.79a4.5 4.5 0 1 1 0 9Z")),
    Mic(
        Part("M12 2a3 3 0 0 0-3 3v7a3 3 0 0 0 6 0V5a3 3 0 0 0-3-3z"),
        Part("M19 10v2a7 7 0 0 1-14 0v-2"),
        Part("M12 19v3"),
    ),
    MicFill(
        Part("M12 2a3 3 0 0 0-3 3v7a3 3 0 0 0 6 0V5a3 3 0 0 0-3-3z", fill = true),
        Part("M19 10v2a7 7 0 0 1-14 0v-2"),
        Part("M12 19v3"),
    ),
    PauseFill(Part("M6.5 4h3.5v16H6.5z", fill = true), Part("M14 4h3.5v16H14z", fill = true)),
    PlayFill(Part("M7 4.5v15a1 1 0 0 0 1.5.86l12-7.5a1 1 0 0 0 0-1.72l-12-7.5A1 1 0 0 0 7 4.5z", fill = true)),
    PaperplaneFill(Part("M3 11 21 3l-6.5 18-3.2-7.3z", fill = true)),
    Envelope(
        Part("M4 5h16a2 2 0 0 1 2 2v10a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V7a2 2 0 0 1 2-2z"),
        Part("M22 7l-10 6L2 7"),
    ),
    Sparkles(
        Part("M10 3l1.9 5.6 5.6 1.9-5.6 1.9L10 18l-1.9-5.6-5.6-1.9 5.6-1.9z", fill = true),
        Part("M19 14l.9 2.6 2.6.9-2.6.9L19 21l-.9-2.6-2.6-.9 2.6-.9z", fill = true),
    ),
    MagnifyingGlass(Part("M10.5 3a7.5 7.5 0 1 0 0 15 7.5 7.5 0 0 0 0-15z"), Part("M16 16l5 5")),
    Sliders(
        Part("M3 6h9"), Part("M16 6h5"), Part("M3 12h3"), Part("M10 12h11"), Part("M3 18h11"), Part("M18 18h3"),
        Part("M14 4a2 2 0 1 0 0 4 2 2 0 0 0 0-4z"),
        Part("M8 10a2 2 0 1 0 0 4 2 2 0 0 0 0-4z"),
        Part("M16 16a2 2 0 1 0 0 4 2 2 0 0 0 0-4z"),
    ),
    Globe(
        Part("M12 2.5a9.5 9.5 0 1 0 0 19 9.5 9.5 0 0 0 0-19z"),
        Part("M2.5 12h19"),
        Part("M12 2.5c2.6 2.6 4 5.9 4 9.5s-1.4 6.9-4 9.5c-2.6-2.6-4-5.9-4-9.5s1.4-6.9 4-9.5z"),
    ),
    Airplane(
        Part("M21 15.5v-2L13.5 9V4a1.5 1.5 0 0 0-3 0v5L3 13.5v2l7.5-2.2v4.2l-2 1.5V21l3.5-1 3.5 1v-2l-2-1.5v-4.2z", fill = true),
    ),
    Code(Part("M8 7l-5 5 5 5"), Part("M16 7l5 5-5 5"), Part("M14 4.5l-4 15")),
    ForkKnife(
        Part("M6 3v6a2.5 2.5 0 0 0 5 0V3"),
        Part("M8.5 3v18"),
        Part("M17 21V3c-2.2 1.2-3.5 4-3.5 7.5S14.8 14 17 14"),
    ),
    Trash(
        Part("M3.5 6.5h17"),
        Part("M9 6.5V4h6v2.5"),
        Part("M5.5 6.5l1 13.5a1.5 1.5 0 0 0 1.5 1.4h8a1.5 1.5 0 0 0 1.5-1.4l1-13.5"),
        Part("M10 11v6"),
        Part("M14 11v6"),
    ),
    At(
        Part("M16 12a4 4 0 1 1-8 0 4 4 0 0 1 8 0z"),
        Part("M16 8v5a3 3 0 0 0 6 0v-1a10 10 0 1 0-4 8"),
    ),
}

/**
 * Draws [sym] in a square of [size] dp. [weight] is the stroke width on the 24 grid (2 = regular,
 * 3 = semibold). [cut] is the color used for the check inside `CheckCircleFill` (match the surface).
 */
@Composable
internal fun Symbol(
    sym: Sym,
    size: Float,
    tint: Color,
    weight: Float = 2f,
    modifier: Modifier = Modifier,
    cut: Color = Color.Black,
) {
    val paths = remember(sym) { sym.parts.map { PathParser().parsePathString(it.d).toPath() } }
    Canvas(modifier.size(size.dp)) {
        val s = this.size.minDimension / 24f
        scale(s, s, pivot = Offset.Zero) {
            sym.parts.forEachIndexed { i, part ->
                if (part.fill) {
                    drawPath(paths[i], tint)
                } else {
                    drawPath(
                        paths[i],
                        if (part.cut) cut else tint,
                        style = Stroke(width = weight, cap = StrokeCap.Round, join = StrokeJoin.Round),
                    )
                }
            }
        }
    }
}
