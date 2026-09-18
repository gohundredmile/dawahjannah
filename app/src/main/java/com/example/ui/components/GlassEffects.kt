package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Pure Glass Effect (Glassmorphism) Design System Utilities for Dawah to Jannah.
 *
 * Provides translucent frosted glass surfaces, specular light-catching borders,
 * top-edge reflective light highlights, and carefully calibrated contrast levels
 * ensuring text is ultra-readable and distinctly visible across all screen themes.
 */
object GlassEffects {

    /**
     * Pure Frosted Glass Background Brush:
     * High enough opacity (80-88%) to keep overlaid text pitch-perfect and legible,
     * while allowing the dynamic background aurora wallpaper to subtly illuminate through.
     */
    @Composable
    fun glassBackgroundBrush(
        isDark: Boolean = isSystemInDarkTheme(),
        tint: Color? = null,
        opacityMultiplier: Float = 1.0f
    ): Brush {
        return if (isDark) {
            val base1 = tint?.copy(alpha = (0.35f * opacityMultiplier).coerceIn(0f, 1f)) ?: Color(0xFF1E293B).copy(alpha = (0.84f * opacityMultiplier).coerceIn(0f, 1f))
            val base2 = tint?.copy(alpha = (0.22f * opacityMultiplier).coerceIn(0f, 1f)) ?: Color(0xFF0F172A).copy(alpha = (0.78f * opacityMultiplier).coerceIn(0f, 1f))
            Brush.verticalGradient(listOf(base1, base2))
        } else {
            if (tint != null) {
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = (0.88f * opacityMultiplier).coerceIn(0f, 1f)),
                        tint.copy(alpha = (0.14f * opacityMultiplier).coerceIn(0f, 1f)),
                        Color.White.copy(alpha = (0.82f * opacityMultiplier).coerceIn(0f, 1f))
                    )
                )
            } else {
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFFFFF).copy(alpha = (0.86f * opacityMultiplier).coerceIn(0f, 1f)),
                        Color(0xFFF8FAFC).copy(alpha = (0.76f * opacityMultiplier).coerceIn(0f, 1f)),
                        Color(0xFFFFFFFF).copy(alpha = (0.84f * opacityMultiplier).coerceIn(0f, 1f))
                    )
                )
            }
        }
    }

    /**
     * Specular Luminous Border:
     * Simulates light bouncing off the beveled edge of physical cut glass.
     */
    @Composable
    fun glassBorderBrush(
        isDark: Boolean = isSystemInDarkTheme(),
        accentColor: Color? = null
    ): Brush {
        return if (isDark) {
            if (accentColor != null) {
                Brush.verticalGradient(
                    listOf(
                        accentColor.copy(alpha = 0.65f),
                        accentColor.copy(alpha = 0.25f),
                        Color.White.copy(alpha = 0.12f)
                    )
                )
            } else {
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.38f),
                        Color.White.copy(alpha = 0.15f),
                        Color(0xFF38BDF8).copy(alpha = 0.22f)
                    )
                )
            }
        } else {
            if (accentColor != null) {
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.95f),
                        accentColor.copy(alpha = 0.45f),
                        Color.White.copy(alpha = 0.70f)
                    )
                )
            } else {
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.96f),
                        Color(0xFFE2E8F0).copy(alpha = 0.65f),
                        Color.White.copy(alpha = 0.85f)
                    )
                )
            }
        }
    }

    /**
     * Emerald Jewel Glass Background Brush for Header
     */
    fun emeraldHeaderGlassBrush(): Brush {
        return Brush.verticalGradient(
            listOf(
                Color(0xFF047857).copy(alpha = 0.94f),
                Color(0xFF065F46).copy(alpha = 0.88f),
                Color(0xFF064E3B).copy(alpha = 0.92f)
            )
        )
    }

    /**
     * Deep readable text colors for maximum visibility on frosted glass:
     */
    object TextColors {
        @Composable
        fun primary(isDark: Boolean = isSystemInDarkTheme()): Color {
            return if (isDark) Color(0xFFF8FAFC) else Color(0xFF0F172A)
        }

        @Composable
        fun secondary(isDark: Boolean = isSystemInDarkTheme()): Color {
            return if (isDark) Color(0xFFCBD5E1) else Color(0xFF334155)
        }

        @Composable
        fun muted(isDark: Boolean = isSystemInDarkTheme()): Color {
            return if (isDark) Color(0xFF94A3B8) else Color(0xFF475569)
        }

        val emeraldDeep = Color(0xFF065F46)
        val amberDeep = Color(0xFFB45309)
        val crimsonDeep = Color(0xFFBE123C)
    }
}

/**
 * Top Reflective Glare Line (Physical glass sheen on top edge)
 */
@Composable
fun GlassTopHighlight(
    modifier: Modifier = Modifier,
    isDark: Boolean = isSystemInDarkTheme(),
    opacity: Float = 0.85f
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color.Transparent,
                        if (isDark) Color.White.copy(alpha = (0.28f * opacity).coerceIn(0f, 1f))
                        else Color.White.copy(alpha = (0.85f * opacity).coerceIn(0f, 1f)),
                        Color.Transparent
                    )
                )
            )
    )
}

/**
 * Pure Glass Card Container
 * Wraps content in a frosted glass container with specular edge, soft ambient shadow,
 * and top sheen highlight.
 */
@Composable
fun PureGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    tint: Color? = null,
    accentBorderColor: Color? = null,
    borderWidth: Dp = 1.2.dp,
    elevation: Dp = 2.dp,
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable BoxScope.() -> Unit
) {
    val bgBrush = GlassEffects.glassBackgroundBrush(isDark = isDark, tint = tint)
    val borderBrush = GlassEffects.glassBorderBrush(isDark = isDark, accentColor = accentBorderColor)

    Surface(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = if (isDark) Color.Black.copy(alpha = 0.5f) else Color(0xFF0F172A).copy(alpha = 0.08f),
                spotColor = if (isDark) Color.Black.copy(alpha = 0.4f) else Color(0xFF0F172A).copy(alpha = 0.06f)
            )
            .clip(shape)
            .border(BorderStroke(borderWidth, borderBrush), shape),
        shape = shape,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(bgBrush)
        ) {
            // Top specular glare sheen
            GlassTopHighlight(
                modifier = Modifier.align(Alignment.TopCenter),
                isDark = isDark
            )

            // Overlaid content
            content()
        }
    }
}
