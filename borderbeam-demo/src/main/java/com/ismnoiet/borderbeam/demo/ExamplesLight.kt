//
//  ExamplesLight.kt
//  BorderBeamDemo
//
//  Author: ismnoiet
//  Minimum supported: Android 7.0 (API 24); AGSL shader on Android 13+ (API 33)
//
//  Light-theme demos on real-world UI. Open the project in Android Studio and use the Compose
//  preview (or call `BorderBeamExamplesLight()` from an activity).
//  Add demos by appending a `LightSection`.
//
package com.ismnoiet.borderbeam.demo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ismnoiet.borderbeam.BeamPalette
import com.ismnoiet.borderbeam.BeamShape
import com.ismnoiet.borderbeam.BeamStyle
import com.ismnoiet.borderbeam.BeamTiming
import com.ismnoiet.borderbeam.BorderBeam
import kotlin.math.abs
import kotlin.math.sin
import kotlinx.coroutines.delay

/** Saturated colors so the beam stays visible on white surfaces. */
private object LightPalette {
    val blue = Color(0.20f, 0.52f, 1.00f)
    val violet = Color(0.55f, 0.36f, 0.96f)
    val pink = Color(0.95f, 0.33f, 0.62f)
    val orange = Color(1.00f, 0.55f, 0.25f)
    val green = Color(0.12f, 0.72f, 0.45f)
    val teal = Color(0.10f, 0.72f, 0.78f)

    val aiGlow = listOf(blue, violet, pink, orange)
    val ocean = listOf(teal, blue, violet)
    val candy = listOf(pink, violet, blue)
    val focus = listOf(green, teal, blue)
    val sunrise = listOf(Color(1.00f, 0.45f, 0.30f), Color(1.00f, 0.70f, 0.20f), Color(0.98f, 0.35f, 0.55f))
    val recording = listOf(Color(1.00f, 0.27f, 0.33f), Color(0.98f, 0.40f, 0.55f), Color(1.00f, 0.60f, 0.30f))
}

private val Paper = Color(0.96f, 0.96f, 0.97f)
private val Ink = Color(0.10f, 0.11f, 0.14f)
private val Subtle = Color(0.45f, 0.47f, 0.52f)
private val Hairline = Color.Black.copy(alpha = 0.06f)

private fun gradient(a: Color, b: Color) = Brush.linearGradient(listOf(a, b))

/** Soft card shadow used on light surfaces. */
private fun Modifier.lightShadow(shape: Shape): Modifier =
    shadow(10.dp, shape, ambientColor = Color.Black.copy(alpha = 0.08f), spotColor = Color.Black.copy(alpha = 0.12f))

@Composable
private fun LightSection(title: String, content: @Composable () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Txt(title, 13f, Subtle, FontWeight.Medium)
        content()
    }
}

/** A circular control with a faint fill. */
@Composable
private fun Control(icon: Sym, size: Int, fill: Color, tint: Color, modifier: Modifier = Modifier) {
    Box(
        modifier.size(size.dp).background(fill, CircleShape).tappable(),
        contentAlignment = Alignment.Center,
    ) { Symbol(icon, size * 0.5f, tint, 2.4f) }
}

/** AI composer: large multi-color beam around a message box. */
@Composable
private fun ComposerCard() {
    var text by remember { mutableStateOf("") }
    val shape = RoundedCornerShape(28.dp)
    BorderBeam(
        modifier = Modifier.fillMaxWidth().lightShadow(shape),
        shape = BeamShape.rounded(28),
        colors = LightPalette.aiGlow,
        surface = Color.White,
        borderColor = Hairline,
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            PlainField(text, { text = it }, "Message Assistant…", 17f, Ink, Subtle, Modifier.fillMaxWidth())
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Control(Sym.Plus, 34, Color.Black.copy(alpha = 0.05f), Ink)
                Box(
                    Modifier
                        .height(34.dp)
                        .background(Color.Black.copy(alpha = 0.05f), CircleShape)
                        .padding(horizontal = 12.dp)
                        .tappable(),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Symbol(Sym.Globe, 16f, Ink, 1.9f)
                        Txt("Search", 14f, Ink, FontWeight.Medium)
                    }
                }
                Spacer(Modifier.weight(1f))
                Control(Sym.ArrowUp, 34, Ink, Color.White)
            }
        }
    }
}

/** Suggestion chips: small single-color beams on capsules. */
@Composable
private fun SuggestionChips() {
    val items = listOf(
        Triple("Plan a trip", Sym.Airplane, LightPalette.blue),
        Triple("Write code", Sym.Code, LightPalette.violet),
        Triple("Surprise me", Sym.Sparkles, LightPalette.pink),
        Triple("Recipe ideas", Sym.ForkKnife, LightPalette.orange),
    )
    Row(
        Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items.forEach { (title, icon, color) ->
            val shape = RoundedCornerShape(percent = 50)
            BorderBeam(
                modifier = Modifier.height(40.dp).lightShadow(shape),
                style = BeamStyle.small,
                shape = BeamShape.capsule,
                colors = BeamPalette.single(color),
                intensity = 0.9,
                timing = BeamTiming(rotateDuration = 3.2),
                surface = Color.White,
                borderColor = Hairline,
            ) {
                Box(Modifier.fillMaxHeight().padding(horizontal = 16.dp).tappable(), contentAlignment = Alignment.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Symbol(icon, 16f, Ink, 1.9f)
                        Txt(title, 14f, Ink, FontWeight.Medium)
                    }
                }
            }
        }
    }
}

/** Voice recorder: the beam runs only while recording, with a live waveform and timer. */
@Composable
private fun VoiceRecorder() {
    var recording by remember { mutableStateOf(true) }
    var elapsed by remember { mutableIntStateOf(0) }
    val shape = RoundedCornerShape(28.dp)
    val red = LightPalette.recording[0]
    val orange = LightPalette.recording[2]

    LaunchedEffect(recording) {
        while (recording) {
            delay(1000)
            if (recording) elapsed += 1
        }
    }
    val time by produceState(0f, recording) {
        if (recording) {
            while (true) {
                androidx.compose.runtime.withFrameNanos { value = it / 1_000_000_000f }
            }
        }
    }

    BorderBeam(
        modifier = Modifier.fillMaxWidth().lightShadow(shape),
        shape = BeamShape.rounded(28),
        active = recording,
        colors = LightPalette.recording,
        timing = BeamTiming(rotateDuration = 2.6),
        surface = Color.White,
        borderColor = Hairline,
    ) {
        Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.size(10.dp).background(if (recording) red else Subtle, CircleShape))
                Txt(if (recording) "Recording" else "Paused", 14f, Ink, FontWeight.SemiBold)
                Spacer(Modifier.weight(1f))
                Txt("%d:%02d".format(elapsed / 60, elapsed % 60), 14f, Subtle, FontWeight.Medium)
            }
            Canvas(Modifier.fillMaxWidth().height(56.dp)) {
                val t = time
                val bars = 38
                val barW = 3.dp.toPx()
                val gap = (size.width - bars * barW) / (bars - 1)
                val brush = gradient(red, orange)
                for (i in 0 until bars) {
                    val a = abs(sin(t * 3.2f + i * 0.55f))
                    val b = 0.45f + 0.55f * abs(sin(t * 1.4f + i * 0.21f))
                    val h = (if (recording) 6f + 50f * a * b else 6f).dp.toPx()
                    drawRoundRect(
                        brush = brush,
                        topLeft = Offset(i * (barW + gap), (size.height - h) / 2f),
                        size = Size(barW, h),
                        cornerRadius = CornerRadius(barW / 2f),
                    )
                }
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(34.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Control(Sym.Trash, 48, Color.Black.copy(alpha = 0.05f), Subtle)
                Box(
                    Modifier
                        .size(68.dp)
                        .shadow(12.dp, CircleShape, ambientColor = red.copy(alpha = 0.35f), spotColor = red.copy(alpha = 0.35f))
                        .background(gradient(red, orange), CircleShape)
                        .tappable { recording = !recording },
                    contentAlignment = Alignment.Center,
                ) { Symbol(if (recording) Sym.PauseFill else Sym.MicFill, 28f, Color.White, 2f) }
                Control(Sym.Checkmark, 48, Color.Black.copy(alpha = 0.05f), Ink)
            }
        }
    }
}

/** Stories: circular beams, each with its own palette and speed. */
@Composable
private fun StoriesRow() {
    val people = listOf(
        Triple("Mia", LightPalette.pink to LightPalette.orange, LightPalette.sunrise),
        Triple("Leo", LightPalette.blue to LightPalette.teal, LightPalette.ocean),
        Triple("Zoe", LightPalette.violet to LightPalette.pink, LightPalette.candy),
        Triple("Sam", LightPalette.green to LightPalette.teal, BeamPalette.single(LightPalette.green)),
        Triple("Ava", LightPalette.orange to LightPalette.pink, LightPalette.aiGlow),
    )
    Row(
        Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // The add button gets a beam too.
        Story("You", LightPalette.aiGlow, 2.8) {
            Symbol(Sym.Plus, 22f, Ink, 2.6f)
        }
        people.forEachIndexed { i, (name, tones, colors) ->
            Story(name, colors, 2.2 + i * 0.35) {
                Box(
                    Modifier.size(56.dp).background(gradient(tones.first, tones.second), CircleShape),
                    contentAlignment = Alignment.Center,
                ) { Txt(name.take(1), 22f, Color.White, FontWeight.Bold) }
            }
        }
    }
}

@Composable
private fun Story(name: String, colors: List<Color>, speed: Double, avatar: @Composable () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        BorderBeam(
            style = BeamStyle.small,
            shape = BeamShape.circle,
            colors = colors,
            timing = BeamTiming(rotateDuration = speed),
            surface = Color.White,
            borderColor = Hairline,
        ) {
            Box(Modifier.padding(4.dp).size(56.dp).tappable(), contentAlignment = Alignment.Center) { avatar() }
        }
        Txt(name, 12f, Subtle)
    }
}

/** Search bar: line beam sweeping along the bottom edge. */
@Composable
private fun SearchBar() {
    val shape = RoundedCornerShape(percent = 50)
    BorderBeam(
        modifier = Modifier.fillMaxWidth().height(56.dp).lightShadow(shape),
        style = BeamStyle.line,
        shape = BeamShape.capsule,
        colors = LightPalette.aiGlow,
        timing = BeamTiming(lineDuration = 2.4),
        surface = Color.White,
        borderColor = Hairline,
    ) {
        Row(
            Modifier.fillMaxSize().padding(horizontal = 20.dp).tappable(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Symbol(Sym.MagnifyingGlass, 18f, Subtle, 2.1f)
            Txt("Search destinations", 16f, Subtle, modifier = Modifier.weight(1f))
            Symbol(Sym.Sliders, 20f, Ink, 1.9f)
        }
    }
}

/** Focus timer: large beam around a circle. */
@Composable
private fun FocusTimer() {
    BorderBeam(
        modifier = Modifier.lightShadow(CircleShape),
        shape = BeamShape.circle,
        colors = LightPalette.focus,
        timing = BeamTiming(rotateDuration = 3.6),
        surface = Color.White,
        borderColor = Hairline,
    ) {
        Column(
            Modifier.size(200.dp).tappable(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Txt("25:00", 44f, Ink, FontWeight.Bold)
            Txt("Deep focus", 14f, Subtle, FontWeight.Medium)
        }
    }
}

@Composable
fun BorderBeamExamplesLight(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .background(Paper)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp),
    ) {
        ComposerCard()
        SuggestionChips()
        LightSection("Voice recording") { VoiceRecorder() }
        LightSection("Stories") { StoriesRow() }
        LightSection("Search") { SearchBar() }
        LightSection("Focus timer") { FocusTimer() }
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun BorderBeamExamplesLightPreview() {
    BorderBeamExamplesLight()
}
