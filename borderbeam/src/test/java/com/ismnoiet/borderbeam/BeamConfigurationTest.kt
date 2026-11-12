package com.ismnoiet.borderbeam

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BeamConfigurationTest {

    // Compose stores sRGB alpha in 8 bits, so 0.3 comes back as 77/255 = 0.302.
    private val ALPHA_TOLERANCE = 0.01f

    @Test
    fun singleColorPaletteFadesInAndOut() {
        val palette = BeamPalette.single(Color.Red)
        assertEquals(3, palette.size)
        assertEquals(0.3f, palette[0].alpha, ALPHA_TOLERANCE)
        assertEquals(1f, palette[1].alpha, 0.001f)
        assertEquals(0.3f, palette[2].alpha, ALPHA_TOLERANCE)
    }

    @Test
    fun longPalettesAreThinnedAndKeepTheirEnds() {
        val colors = List(20) { Color(it / 20f, 0f, 0f) }
        val fit = fitPalette(colors)
        assertEquals(MAX_BEAM_COLORS, fit.size)
        assertEquals(colors.first(), fit.first())
        assertEquals(colors.last(), fit.last())
    }

    @Test
    fun emptyPaletteFallsBackToWhite() {
        assertEquals(listOf(Color.White), fitPalette(emptyList()))
    }

    @Test
    fun packedPaletteIsPaddedForTheShader() {
        val packed = packPalette(BeamPalette.neon)
        assertEquals(BeamPalette.neon.size, packed.count)
        assertEquals(MAX_BEAM_COLORS * 4, packed.data.size)
    }

    @Test
    fun capsuleAndCircleUseAHugeRadiusThatTheShaderClamps() {
        assertTrue(BeamShape.capsule.cornerRadius > 1000f)
        assertTrue(BeamShape.circle.cornerRadius > 1000f)
        assertEquals(26f, BeamShape.rounded(26).cornerRadius, 0.001f)
    }

    @Test
    fun directionsMapToOppositeShaderSigns() {
        assertEquals(1f, BeamDirection.leftToRight.shaderValue, 0f)
        assertEquals(-1f, BeamDirection.rightToLeft.shaderValue, 0f)
    }
}
