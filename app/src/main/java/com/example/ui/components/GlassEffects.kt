package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import com.example.data.model.ScreenEffectMode
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Pure Glass Effect (Glassmorphism) Design System Utilities for Dawah to Jannah.
 *
 * Provides translucent frosted glass surfaces, specular light-catching borders,
 * top-edge reflective light highlights, living fluid wave animation inside the glass,
 * and carefully calibrated contrast levels ensuring text is ultra-readable and distinctly visible.
 */
object GlassEffects {

    /**
     * Pure Frosted Glass Background Brush:
     * High enough opacity to keep overlaid text pitch-perfect and legible,
     * while allowing the living glass waves and dynamic background to subtly illuminate through.
     */
    @Composable
    fun glassBackgroundBrush(
        isDark: Boolean = isSystemInDarkTheme(),
        tint: Color? = null,
        opacityMultiplier: Float = 1.0f
    ): Brush {
        return if (isDark) {
            val base1 = tint?.copy(alpha = (0.28f * opacityMultiplier).coerceIn(0f, 1f)) ?: Color(0xFF1E293B).copy(alpha = (0.78f * opacityMultiplier).coerceIn(0f, 1f))
            val base2 = tint?.copy(alpha = (0.16f * opacityMultiplier).coerceIn(0f, 1f)) ?: Color(0xFF0F172A).copy(alpha = (0.72f * opacityMultiplier).coerceIn(0f, 1f))
            Brush.verticalGradient(listOf(base1, base2))
        } else {
            if (tint != null) {
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = (0.84f * opacityMultiplier).coerceIn(0f, 1f)),
                        tint.copy(alpha = (0.12f * opacityMultiplier).coerceIn(0f, 1f)),
                        Color.White.copy(alpha = (0.78f * opacityMultiplier).coerceIn(0f, 1f))
                    )
                )
            } else {
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFFFFF).copy(alpha = (0.82f * opacityMultiplier).coerceIn(0f, 1f)),
                        Color(0xFFF8FAFC).copy(alpha = (0.70f * opacityMultiplier).coerceIn(0f, 1f)),
                        Color(0xFFFFFFFF).copy(alpha = (0.80f * opacityMultiplier).coerceIn(0f, 1f))
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
                        accentColor.copy(alpha = 0.55f),
                        accentColor.copy(alpha = 0.20f),
                        Color.White.copy(alpha = 0.10f)
                    )
                )
            } else {
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.32f),
                        Color.White.copy(alpha = 0.12f),
                        Color(0xFF38BDF8).copy(alpha = 0.18f)
                    )
                )
            }
        } else {
            if (accentColor != null) {
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.90f),
                        accentColor.copy(alpha = 0.35f),
                        Color.White.copy(alpha = 0.60f)
                    )
                )
            } else {
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.92f),
                        Color(0xFFE2E8F0).copy(alpha = 0.50f),
                        Color.White.copy(alpha = 0.75f)
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
 * Animated Living Wave Background for Frosted Glass Surfaces:
 * Simulates undulating light waves and refractive fluid ribbons flowing through the glass,
 * making the glass effect outstanding, alive, and eye-catchy while preserving pristine legibility.
 */
@Composable
fun GlassWaveBackground(
    modifier: Modifier = Modifier,
    isDark: Boolean = isSystemInDarkTheme(),
    tint: Color? = null,
    waveSpeed: Float = 1.0f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glassWaveAnimation")

    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween((6500 / waveSpeed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "glassWavePhase1"
    )

    val phase2 by infiniteTransition.animateFloat(
        initialValue = 2f * PI.toFloat(),
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween((9200 / waveSpeed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "glassWavePhase2"
    )

    val waveColor = tint ?: if (isDark) Color(0xFF10B981) else Color(0xFF059669)
    val highlightColor = if (isDark) Color.White.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.65f)

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        if (w <= 0 || h <= 0) return@Canvas

        val steps = 32
        val stepX = w / steps

        // 1. Primary Sinusoidal Wave Ribbon
        val wave1Path = Path()
        val wave1Crest = Path()
        val wave1BaseY = h * 0.44f
        val wave1Amp = h * 0.22f

        val startY1 = wave1BaseY + sin(phase1) * wave1Amp
        wave1Path.moveTo(0f, h)
        wave1Path.lineTo(0f, startY1)
        wave1Crest.moveTo(0f, startY1)

        for (i in 1..steps) {
            val currX = i * stepX
            val prevX = (i - 1) * stepX
            val normProgress = i.toFloat() / steps
            val prevNorm = (i - 1).toFloat() / steps

            val currY = wave1BaseY +
                    sin(phase1 + normProgress * 2f * PI.toFloat()) * wave1Amp +
                    cos(phase2 + normProgress * 2.5f * PI.toFloat()) * (wave1Amp * 0.35f)
            val prevY = wave1BaseY +
                    sin(phase1 + prevNorm * 2f * PI.toFloat()) * wave1Amp +
                    cos(phase2 + prevNorm * 2.5f * PI.toFloat()) * (wave1Amp * 0.35f)

            val midX = (prevX + currX) / 2f
            val midY = (prevY + currY) / 2f
            wave1Path.quadraticTo(prevX, prevY, midX, midY)
            wave1Crest.quadraticTo(prevX, prevY, midX, midY)
        }
        wave1Path.lineTo(w, h)
        wave1Path.close()

        val waveAlpha = if (isDark) 0.10f else 0.15f
        drawPath(
            path = wave1Path,
            brush = Brush.verticalGradient(
                colors = listOf(
                    waveColor.copy(alpha = waveAlpha),
                    highlightColor.copy(alpha = waveAlpha * 0.8f),
                    Color.Transparent
                ),
                startY = wave1BaseY - wave1Amp,
                endY = h
            )
        )

        // Wave crest shimmer line
        drawPath(
            path = wave1Crest,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    highlightColor.copy(alpha = if (isDark) 0.22f else 0.45f),
                    waveColor.copy(alpha = if (isDark) 0.30f else 0.50f),
                    highlightColor.copy(alpha = if (isDark) 0.22f else 0.45f),
                    Color.Transparent
                )
            ),
            style = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round)
        )

        // 2. Harmonic Counter-Flow Wave Ribbon
        val wave2Path = Path()
        val wave2BaseY = h * 0.68f
        val wave2Amp = h * 0.16f

        val startY2 = wave2BaseY + cos(phase2) * wave2Amp
        wave2Path.moveTo(0f, h)
        wave2Path.lineTo(0f, startY2)

        for (i in 1..steps) {
            val currX = i * stepX
            val prevX = (i - 1) * stepX
            val normProgress = i.toFloat() / steps
            val prevNorm = (i - 1).toFloat() / steps

            val currY = wave2BaseY +
                    cos(phase2 + normProgress * 2.2f * PI.toFloat()) * wave2Amp +
                    sin(phase1 + normProgress * 2f * PI.toFloat()) * (wave2Amp * 0.3f)
            val prevY = wave2BaseY +
                    cos(phase2 + prevNorm * 2.2f * PI.toFloat()) * wave2Amp +
                    sin(phase1 + prevNorm * 2f * PI.toFloat()) * (wave2Amp * 0.3f)

            val midX = (prevX + currX) / 2f
            val midY = (prevY + currY) / 2f
            wave2Path.quadraticTo(prevX, prevY, midX, midY)
        }
        wave2Path.lineTo(w, h)
        wave2Path.close()

        val wave2Alpha = if (isDark) 0.08f else 0.12f
        drawPath(
            path = wave2Path,
            brush = Brush.verticalGradient(
                colors = listOf(
                    highlightColor.copy(alpha = wave2Alpha),
                    waveColor.copy(alpha = wave2Alpha * 0.6f),
                    Color.Transparent
                ),
                startY = wave2BaseY - wave2Amp,
                endY = h
            )
        )
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

val LocalScreenEffectMode = compositionLocalOf { ScreenEffectMode.GLASS }

/**
 * Pure Glass Card Container:
 * In ScreenEffectMode.NORMAL: Reverts to a clean, solid, classic Material surface card as before.
 * In ScreenEffectMode.GLASS: Wraps content in a frosted glass container with specular edge, soft ambient shadow,
 * top sheen highlight, and dynamic living wave effect inside the glass.
 */
@Composable
fun PureGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    tint: Color? = null,
    accentBorderColor: Color? = null,
    borderWidth: Dp = 1.0.dp,
    elevation: Dp = 2.dp,
    isDark: Boolean = isSystemInDarkTheme(),
    showWaveEffect: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val effectMode = LocalScreenEffectMode.current

    if (effectMode == ScreenEffectMode.NORMAL) {
        // Normal Mode: Clean solid Material surface card as before (revert to normal home screen)
        val normalBgColor = if (isDark) Color(0xFF1E293B) else Color.White
        val normalBorderColor = accentBorderColor?.copy(alpha = 0.35f) ?: if (isDark) Color(0xFF334155) else Color(0xFFE2E8F0)

        Surface(
            modifier = modifier
                .shadow(
                    elevation = elevation,
                    shape = shape,
                    ambientColor = if (isDark) Color.Black.copy(alpha = 0.35f) else Color(0xFF0F172A).copy(alpha = 0.08f),
                    spotColor = if (isDark) Color.Black.copy(alpha = 0.25f) else Color(0xFF0F172A).copy(alpha = 0.05f)
                )
                .clip(shape)
                .border(BorderStroke(borderWidth, normalBorderColor), shape),
            shape = shape,
            color = normalBgColor
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
            ) {
                content()
            }
        }
        return
    }

    val bgBrush = GlassEffects.glassBackgroundBrush(isDark = isDark, tint = tint)
    val borderBrush = GlassEffects.glassBorderBrush(isDark = isDark, accentColor = accentBorderColor)

    Surface(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = if (isDark) Color.Black.copy(alpha = 0.35f) else Color(0xFF0F172A).copy(alpha = 0.06f),
                spotColor = if (isDark) Color.Black.copy(alpha = 0.25f) else Color(0xFF0F172A).copy(alpha = 0.04f)
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
            // Living Wave Effect in the glass
            if (showWaveEffect) {
                GlassWaveBackground(
                    modifier = Modifier.matchParentSize(),
                    isDark = isDark,
                    tint = tint ?: accentBorderColor
                )
            }

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

