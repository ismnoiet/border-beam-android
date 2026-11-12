//
//  Examples.kt
//  BorderBeamDemo
//
//  Author: ismnoiet
//  Minimum supported: Android 7.0 (API 24); AGSL shader on Android 13+ (API 33)
//
//  Dark-theme demos. Open the project in Android Studio and use the Compose preview
//  (or call `BorderBeamExamples()` from an activity). Add demos by appending a `DemoSection`.
//
package com.ismnoiet.borderbeam.demo

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ismnoiet.borderbeam.BeamDirection
import com.ismnoiet.borderbeam.BeamPalette
import com.ismnoiet.borderbeam.BeamShape
import com.ismnoiet.borderbeam.BeamStyle
import com.ismnoiet.borderbeam.BeamTiming
import com.ismnoiet.borderbeam.BorderBeam

private val Background = Color(0.09f, 0.09f, 0.09f)
private val Soft = Color.White.copy(alpha = 0.6f)
private val Faint = Color.White.copy(alpha = 0.4f)

@Composable
private fun DemoSection(title: String, content: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Txt(title, 12f, Faint, FontWeight.Medium)
        content()
    }
}

@Composable
private fun SwatchCard(label: String, colors: List<Color>, active: Boolean) {
    BorderBeam(shape = BeamShape.rounded(20), active = active, colors = colors) {
        Box(Modifier.size(width = 100.dp, height = 70.dp), contentAlignment = Alignment.Center) {
            Txt(label, 13f, Soft)
        }
    }
}

/** Rounded pill that looks like a menu button (static in this demo). */
@Composable
private fun Pill(text: String) {
    Row(
        Modifier
            .background(Color.White.copy(alpha = 0.07f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 7.dp)
            .tappable(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Txt(text, 12f, Color.White.copy(alpha = 0.75f))
        Symbol(Sym.ChevronDown, 9f, Color.White.copy(alpha = 0.75f), weight = 3.2f)
    }
}

@Composable
private fun RoundButton(icon: Sym, size: Int, iconSize: Float, fill: Color, tint: Color = Color.White, weight: Float = 2.4f) {
    Box(
        Modifier.size(size.dp).background(fill, CircleShape).tappable(),
        contentAlignment = Alignment.Center,
    ) {
        Symbol(icon, iconSize, tint, weight)
    }
}

@Composable
private fun PromptBox(placeholder: String, icon: Sym, colors: List<Color>, active: Boolean) {
    var text by remember { mutableStateOf("") }
    BorderBeam(
        modifier = Modifier.width(340.dp),
        shape = BeamShape.rounded(26),
        active = active,
        colors = colors,
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                Modifier.size(26.dp).background(Color.White.copy(alpha = 0.06f), CircleShape),
                contentAlignment = Alignment.Center,
            ) { Symbol(icon, 14f, Soft, 2.2f) }
            // Any control works inside: text fields, buttons, your own composables…
            PlainField(text, { text = it }, placeholder, 15f, Color.White, Faint, Modifier.fillMaxWidth())
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Pill("Agent")
                Pill("Auto")
                Spacer(Modifier.weight(1f))
                RoundButton(Sym.ArrowUp, 30, 14f, Color.White.copy(alpha = 0.08f))
            }
        }
    }
}

@Composable
private fun AskAnythingBox(active: Boolean) {
    var text by remember { mutableStateOf("") }
    // Muted tones sampled from the reference (a dim, soft glow, not a bright rainbow).
    // Along the bottom edge it reads teal -> blue -> lavender -> mauve -> red -> orange.
    val colors = listOf(
        Color(0.80f, 0.52f, 0.30f),  // orange
        Color(0.82f, 0.30f, 0.30f),  // red
        Color(0.78f, 0.42f, 0.55f),  // mauve
        Color(0.58f, 0.52f, 0.78f),  // lavender
        Color(0.26f, 0.46f, 0.76f),  // blue
        Color(0.22f, 0.58f, 0.72f),  // cyan-blue
        Color(0.22f, 0.66f, 0.60f),  // teal
    )
    BorderBeam(
        modifier = Modifier.width(360.dp),
        shape = BeamShape.rounded(28),
        direction = BeamDirection.rightToLeft,
        active = active,
        colors = colors,
        intensity = 0.75,
        surface = Color(0.06f, 0.06f, 0.06f),
        borderColor = Color.White.copy(alpha = 0.06f),
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(22.dp)) {
            PlainField(text, { text = it }, "Ask Anything...", 17f, Color.White, Faint, Modifier.fillMaxWidth())
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    Modifier
                        .background(Color.White.copy(alpha = 0.12f), CircleShape)
                        .padding(horizontal = 16.dp, vertical = 9.dp)
                        .tappable(),
                ) { Txt("Name/Model Name", 12.5f, Color.White.copy(alpha = 0.85f), maxLines = 1) }
                Spacer(Modifier.weight(1f))
                Symbol(Sym.Plus, 17f, Color.White.copy(alpha = 0.85f), 2f, Modifier.tappable())
                Symbol(Sym.Cloud, 19f, Color.White.copy(alpha = 0.85f), 1.8f, Modifier.tappable())
                Symbol(Sym.Mic, 17f, Color.White.copy(alpha = 0.85f), 1.8f, Modifier.tappable())
                RoundButton(Sym.ArrowUp, 40, 18f, Color.Black)
            }
        }
    }
}

/** Chat composer: neon beam travelling right to left. */
@Composable
private fun NeonComposer(active: Boolean) {
    var text by remember { mutableStateOf("") }
    BorderBeam(
        modifier = Modifier.width(340.dp),
        shape = BeamShape.rounded(22),
        direction = BeamDirection.rightToLeft,
        active = active,
        colors = BeamPalette.neon,
        surface = Color(0.05f, 0.05f, 0.05f),
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            PlainField(text, { text = it }, "Message...", 16f, Color.White, Faint, Modifier.fillMaxWidth())
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Summarize", "Translate", "Explain").forEach { chip ->
                    Box(
                        Modifier
                            .background(Color.White.copy(alpha = 0.08f), CircleShape)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .tappable(),
                    ) { Txt(chip, 12f, Color.White.copy(alpha = 0.75f)) }
                }
                Spacer(Modifier.weight(1f))
                RoundButton(Sym.PaperplaneFill, 32, 16f, Color.White, tint = Color.Black)
            }
        }
    }
}

/** Beam only while the field has focus: any state can drive `active`. */
@Composable
private fun FocusField() {
    var email by remember { mutableStateOf("") }
    // The beam shows by default, then follows focus once the field has been used.
    var focused by remember { mutableStateOf(true) }
    var used by remember { mutableStateOf(false) }
    BorderBeam(
        modifier = Modifier.size(width = 300.dp, height = 52.dp),
        shape = BeamShape.rounded(16),
        active = focused,
        colors = BeamPalette.aurora,
        surface = Color(0.1f, 0.1f, 0.1f),
    ) {
        Row(
            Modifier.fillMaxSize().padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Symbol(Sym.Envelope, 17f, Color.White.copy(alpha = 0.5f), 1.8f)
            PlainField(
                email, { email = it }, "you@example.com", 15f, Color.White, Faint,
                Modifier.fillMaxWidth().onFocusChanged {
                    if (it.isFocused) { used = true; focused = true } else if (used) focused = false
                },
            )
        }
    }
}

/** Pricing card with a warm sunset beam. */
@Composable
private fun SunsetCard(active: Boolean) {
    BorderBeam(
        modifier = Modifier.width(300.dp),
        shape = BeamShape.rounded(28),
        active = active,
        colors = BeamPalette.sunset,
        surface = Color(0.08f, 0.08f, 0.08f),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Txt("Pro", 13f, Color.White.copy(alpha = 0.7f), FontWeight.SemiBold)
                Spacer(Modifier.weight(1f))
                Box(Modifier.background(Color.White.copy(alpha = 0.1f), CircleShape).padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Txt("Popular", 11f, Color.White.copy(alpha = 0.8f), FontWeight.Medium)
                }
            }
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Txt("\$19", 40f, Color.White, FontWeight.SemiBold)
                Txt("/ month", 13f, Color.White.copy(alpha = 0.5f), modifier = Modifier.padding(bottom = 8.dp))
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Unlimited projects", "Priority support", "Team sharing").forEach { feature ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Symbol(Sym.Checkmark, 11f, Color.White.copy(alpha = 0.7f), 3.2f)
                        Txt(feature, 13f, Color.White.copy(alpha = 0.7f))
                    }
                }
            }
            Box(
                Modifier.fillMaxWidth().background(Color.White, CircleShape).padding(vertical = 12.dp).tappable(),
                contentAlignment = Alignment.Center,
            ) { Txt("Get started", 14f, Color.Black, FontWeight.SemiBold) }
        }
    }
}

/** Now-playing capsule: slow, subtle silver beam. */
@Composable
private fun NowPlaying(active: Boolean) {
    BorderBeam(
        modifier = Modifier.width(320.dp),
        shape = BeamShape.capsule,
        active = active,
        colors = BeamPalette.silver,
        intensity = 0.85,
        timing = BeamTiming(rotateDuration = 4.5),
        surface = Color(0.07f, 0.07f, 0.07f),
    ) {
        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                Modifier.size(48.dp).background(
                    Brush.linearGradient(listOf(Color(0.63f, 0.13f, 0.94f), Color(0.29f, 0f, 0.51f), Color(0f, 0f, 1f))),
                    RoundedCornerShape(12.dp),
                ),
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Txt("Midnight City", 14f, Color.White, FontWeight.SemiBold)
                Txt("M83", 12f, Color.White.copy(alpha = 0.5f))
            }
            RoundButton(Sym.PlayFill, 40, 16f, Color.White, tint = Color.Black)
        }
    }
}

/** Toast: single green color, faster lap. */
@Composable
private fun Toast(active: Boolean) {
    val green = Color(0.25f, 0.85f, 0.5f)
    BorderBeam(
        modifier = Modifier.height(44.dp),
        shape = BeamShape.capsule,
        active = active,
        colors = BeamPalette.single(green),
        timing = BeamTiming(rotateDuration = 1.6),
        surface = Color(0.09f, 0.09f, 0.09f),
    ) {
        Row(
            Modifier.fillMaxHeight().padding(horizontal = 18.dp).tappable(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Symbol(Sym.CheckCircleFill, 17f, green, cut = Color(0.09f, 0.09f, 0.09f))
            Txt("Saved to library", 14f, Color.White, FontWeight.Medium)
        }
    }
}

/** Avatar ring. */
@Composable
private fun AvatarBeam(active: Boolean) {
    BorderBeam(
        style = BeamStyle.small,
        shape = BeamShape.circle,
        active = active,
        colors = BeamPalette.neon,
        surface = Color(0.12f, 0.12f, 0.12f),
    ) {
        Box(Modifier.size(68.dp).tappable(), contentAlignment = Alignment.Center) {
            Txt("IS", 20f, Color.White.copy(alpha = 0.85f), FontWeight.SemiBold)
        }
    }
}

/** Gradient-border button: tight beam on a capsule. */
@Composable
private fun UpgradeButton(active: Boolean) {
    BorderBeam(
        modifier = Modifier.height(48.dp),
        style = BeamStyle.small,
        shape = BeamShape.capsule,
        active = active,
        colors = BeamPalette.sunset,
        surface = Color(0.07f, 0.07f, 0.07f),
    ) {
        Row(
            Modifier.fillMaxHeight().padding(horizontal = 28.dp).tappable(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Symbol(Sym.Sparkles, 16f, Color.White)
            Txt("Upgrade", 15f, Color.White, FontWeight.SemiBold)
        }
    }
}

@Composable
fun BorderBeamExamples(modifier: Modifier = Modifier) {
    var active by remember { mutableStateOf(true) }
    var query by remember { mutableStateOf("") }

    Column(
        modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        DemoSection("Large") {
            PromptBox("Build anything...", Sym.At, BeamPalette.spectrum, active)
        }
        DemoSection("Ask Anything (right to left)") { AskAnythingBox(active) }
        DemoSection("Small") {
            BorderBeam(style = BeamStyle.small, shape = BeamShape.circle, active = active) {
                Box(Modifier.size(36.dp).tappable(), contentAlignment = Alignment.Center) {
                    Box(Modifier.size(9.dp).background(Color.White.copy(alpha = 0.75f), RoundedCornerShape(2.dp)))
                }
            }
        }
        DemoSection("Line") {
            BorderBeam(
                modifier = Modifier.size(width = 280.dp, height = 42.dp),
                style = BeamStyle.line,
                shape = BeamShape.capsule,
                active = active,
            ) {
                Row(
                    Modifier.fillMaxSize().padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Symbol(Sym.MagnifyingGlass, 15f, Faint, 2.2f)
                    PlainField(query, { query = it }, "Search", 14f, Color.White, Faint, Modifier.fillMaxWidth())
                }
            }
        }
        DemoSection("Chat composer (neon, right to left)") { NeonComposer(active) }
        DemoSection("Beam on by default, then while focused") { FocusField() }
        DemoSection("Pricing card (sunset)") { SunsetCard(active) }
        DemoSection("Now playing (slow silver)") { NowPlaying(active) }
        DemoSection("Toast (single color, fast)") { Toast(active) }
        DemoSection("Avatar + gradient button") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
                AvatarBeam(active)
                UpgradeButton(active)
            }
        }
        DemoSection("Search (aurora line)") {
            BorderBeam(
                modifier = Modifier.size(width = 300.dp, height = 44.dp),
                style = BeamStyle.line,
                shape = BeamShape.capsule,
                active = active,
                colors = BeamPalette.aurora,
                surface = Color(0.08f, 0.08f, 0.08f),
            ) {
                Row(
                    Modifier.fillMaxSize().padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Symbol(Sym.MagnifyingGlass, 15f, Faint, 2.2f)
                    Txt("Search the docs", 14f, Faint, modifier = Modifier.weight(1f))
                    Box(
                        Modifier
                            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(5.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                    ) { Txt("⌘K", 11f, Faint, FontWeight.Medium) }
                }
            }
        }
        DemoSection("Single color") {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                SwatchCard("Blue", BeamPalette.single(Color.Blue), active)
                SwatchCard("Green", BeamPalette.single(Color.Green), active)
                SwatchCard("White", BeamPalette.single(Color.White), active)
            }
        }
        Box(
            Modifier
                .background(Color.White.copy(alpha = 0.08f), CircleShape)
                .padding(horizontal = 18.dp, vertical = 8.dp)
                .tappable { active = !active },
        ) { Txt(if (active) "Pause" else "Play", 13f, Color.White.copy(alpha = 0.85f), FontWeight.Medium) }
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun BorderBeamExamplesPreview() {
    BorderBeamExamples()
}
