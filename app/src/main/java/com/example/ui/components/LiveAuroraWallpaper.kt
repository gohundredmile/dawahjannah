package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.data.model.AuroraWallpaperConfig
import com.example.data.model.AuroraWavePreset
import com.example.data.model.ThemeStyle
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Enriched Magnificent Live Aurora Wave Wallpaper:
 * An ethereal, living northern-lights atmosphere with vivid undulating sinusoidal wave ribbons,
 * glowing wave crest contours, luminous drifting radiant nodes, auroral light pillars,
 * and floating celestial starlight particles & Islamic stars (নূরানি আলোককণা ও তারকা).
 */
@Composable
fun LiveAuroraWallpaperBackground(
    modifier: Modifier = Modifier,
    config: AuroraWallpaperConfig = AuroraWallpaperConfig(),
    themeStyle: ThemeStyle = ThemeStyle.EMERALD_JANNAH,
    isDark: Boolean = false,
    baseColor: Color? = null
) {
    val effectiveBaseColor = baseColor ?: if (isDark) {
        Color(0xFF070D14)
    } else {
        Color(0xFFFBFDFB)
    }

    if (!config.isEnabled) {
        Canvas(modifier = modifier.fillMaxSize()) {
            drawRect(color = effectiveBaseColor)
        }
        return
    }

    // Animation transition modulated by speed setting
    val speedMultiplier = config.speed.coerceIn(0.4f, 2.5f)
    val baseDuration = (15000 / speedMultiplier).toInt().coerceAtLeast(4000)

    val infiniteTransition = rememberInfiniteTransition(label = "auroraLiveWaves")

    val wavePhase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(baseDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wavePhase1"
    )

    val wavePhase2 by infiniteTransition.animateFloat(
        initialValue = 2f * PI.toFloat(),
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween((baseDuration * 1.35f).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wavePhase2"
    )

    val wavePhase3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween((baseDuration * 0.85f).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wavePhase3"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween((4500 / speedMultiplier).toInt(), easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val particleRise by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween((12000 / speedMultiplier).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particleRise"
    )

    val starRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((20000 / speedMultiplier).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "starRotation"
    )

    // Retrieve active colors based on config and theme
    val waveColors = remember(config.preset, themeStyle) {
        config.getEffectiveColors(themeStyle)
    }
    val highlightColor = remember(config.preset, themeStyle) {
        config.getEffectiveHighlight(themeStyle)
    }

    val intensity = config.intensity.coerceIn(0.3f, 1.0f)

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // 1. BASE BACKGROUND LAYER
        drawRect(color = effectiveBaseColor)

        if (width <= 0 || height <= 0 || waveColors.isEmpty()) return@Canvas

        val color1 = waveColors.getOrElse(0) { Color(0xFF10B981) }
        val color2 = waveColors.getOrElse(1) { Color(0xFF06B6D4) }
        val color3 = waveColors.getOrElse(2) { Color(0xFFF59E0B) }
        val color4 = waveColors.getOrElse(3) { Color(0xFF8B5CF6) }

        // Boost opacity slightly in light mode for vivid visibility
        val visibilityBoost = if (isDark) 1.0f else 1.25f

        // 2. BROAD CELESTIAL AMBIENT GLOW ORBS (Atmospheric Depth)
        val centerGlow1 = Offset(
            x = width * 0.28f + cos(wavePhase1 * 0.9f) * (width * 0.22f),
            y = height * 0.22f + sin(wavePhase1 * 0.7f) * (height * 0.12f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    color1.copy(alpha = (0.52f * intensity * pulse * visibilityBoost).coerceAtMost(0.9f)),
                    color1.copy(alpha = 0.22f * intensity * visibilityBoost),
                    Color.Transparent
                ),
                center = centerGlow1,
                radius = width * 0.90f
            ),
            center = centerGlow1,
            radius = width * 0.90f
        )

        val centerGlow2 = Offset(
            x = width * 0.74f + sin(wavePhase2 * 0.85f) * (width * 0.24f),
            y = height * 0.50f + cos(wavePhase2 * 0.8f) * (height * 0.16f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    color2.copy(alpha = (0.46f * intensity * pulse * visibilityBoost).coerceAtMost(0.85f)),
                    color2.copy(alpha = 0.20f * intensity * visibilityBoost),
                    Color.Transparent
                ),
                center = centerGlow2,
                radius = width * 0.85f
            ),
            center = centerGlow2,
            radius = width * 0.85f
        )

        val centerGlow3 = Offset(
            x = width * 0.44f + cos(wavePhase2 * 1.1f) * (width * 0.26f),
            y = height * 0.80f + sin(wavePhase1 * 0.9f) * (height * 0.14f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    color3.copy(alpha = (0.42f * intensity * pulse * visibilityBoost).coerceAtMost(0.8f)),
                    color3.copy(alpha = 0.18f * intensity * visibilityBoost),
                    Color.Transparent
                ),
                center = centerGlow3,
                radius = width * 0.85f
            ),
            center = centerGlow3,
            radius = width * 0.85f
        )

        // 3. VERTICAL AURORAL LIGHT PILLARS (Northern Lights Rays)
        if (config.showAuroraRays) {
            val rayCount = 4
            for (r in 0 until rayCount) {
                val rayNormX = ((r.toFloat() / rayCount) + (wavePhase1 * 0.05f)) % 1.0f
                val rayX = rayNormX * width
                val rayWidth = width * 0.28f
                val rayAlpha = (0.28f * intensity * (0.8f + 0.2f * sin(wavePhase2 + r))).coerceAtMost(0.6f) * visibilityBoost

                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            color1.copy(alpha = rayAlpha),
                            highlightColor.copy(alpha = rayAlpha * 0.85f),
                            color2.copy(alpha = rayAlpha * 0.6f),
                            Color.Transparent
                        ),
                        startX = rayX - rayWidth / 2f,
                        endX = rayX + rayWidth / 2f
                    ),
                    topLeft = Offset(rayX - rayWidth / 2f, 0f),
                    size = androidx.compose.ui.geometry.Size(rayWidth, height)
                )
            }
        }

        // 4. FLUID SINUSOIDAL AURORA WAVE RIBBON 1 (Upper Celestial Stream)
        val steps = 44
        val stepX = width / steps

        val wave1Path = Path()
        val wave1CrestPath = Path()
        val wave1BaseY = height * 0.26f
        val wave1Amp = height * 0.095f * pulse

        val startY1 = wave1BaseY + sin(wavePhase1) * wave1Amp
        wave1Path.moveTo(0f, height * 0.65f)
        wave1Path.lineTo(0f, startY1)
        wave1CrestPath.moveTo(0f, startY1)

        for (i in 1..steps) {
            val currentX = i * stepX
            val prevX = (i - 1) * stepX
            val currentY = wave1BaseY +
                    sin(wavePhase1 + (i.toFloat() / steps) * 2f * PI.toFloat()) * wave1Amp +
                    cos(wavePhase2 + (i.toFloat() / steps) * 4f * PI.toFloat()) * (wave1Amp * 0.35f)
            val prevY = wave1BaseY +
                    sin(wavePhase1 + ((i - 1).toFloat() / steps) * 2f * PI.toFloat()) * wave1Amp +
                    cos(wavePhase2 + ((i - 1).toFloat() / steps) * 4f * PI.toFloat()) * (wave1Amp * 0.35f)

            val midX = (prevX + currentX) / 2f
            val midY = (prevY + currentY) / 2f
            wave1Path.quadraticTo(prevX, prevY, midX, midY)
            wave1CrestPath.quadraticTo(prevX, prevY, midX, midY)
        }
        wave1Path.lineTo(width, height * 0.65f)
        wave1Path.close()

        drawPath(
            path = wave1Path,
            brush = Brush.verticalGradient(
                colors = listOf(
                    color1.copy(alpha = (0.60f * intensity * pulse * visibilityBoost).coerceAtMost(0.92f)),
                    color2.copy(alpha = (0.38f * intensity * visibilityBoost).coerceAtMost(0.75f)),
                    Color.Transparent
                ),
                startY = wave1BaseY - wave1Amp * 1.5f,
                endY = wave1BaseY + wave1Amp * 3.8f
            )
        )

        // Glowing Wave Crest Contour 1 (Clearly visible wave line)
        if (config.showWaveContours) {
            drawPath(
                path = wave1CrestPath,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        color1.copy(alpha = 0.2f),
                        highlightColor.copy(alpha = (0.85f * intensity).coerceAtMost(1f)),
                        color2.copy(alpha = (0.75f * intensity).coerceAtMost(1f)),
                        highlightColor.copy(alpha = (0.85f * intensity).coerceAtMost(1f)),
                        color1.copy(alpha = 0.2f)
                    )
                ),
                style = Stroke(width = 2.8.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // 5. MID-SCREEN TIDAL WAVE RIBBON (Harmonic counter-flow)
        val wave3Path = Path()
        val wave3CrestPath = Path()
        val wave3BaseY = height * 0.45f
        val wave3Amp = height * 0.08f

        val startY3 = wave3BaseY + cos(wavePhase3) * wave3Amp
        wave3Path.moveTo(0f, height * 0.8f)
        wave3Path.lineTo(0f, startY3)
        wave3CrestPath.moveTo(0f, startY3)

        for (i in 1..steps) {
            val currentX = i * stepX
            val prevX = (i - 1) * stepX
            val currentY = wave3BaseY +
                    sin(wavePhase3 + (i.toFloat() / steps) * 3f * PI.toFloat()) * wave3Amp +
                    cos(wavePhase1 + (i.toFloat() / steps) * 2f * PI.toFloat()) * (wave3Amp * 0.4f)
            val prevY = wave3BaseY +
                    sin(wavePhase3 + ((i - 1).toFloat() / steps) * 3f * PI.toFloat()) * wave3Amp +
                    cos(wavePhase1 + ((i - 1).toFloat() / steps) * 2f * PI.toFloat()) * (wave3Amp * 0.4f)

            val midX = (prevX + currentX) / 2f
            val midY = (prevY + currentY) / 2f
            wave3Path.quadraticTo(prevX, prevY, midX, midY)
            wave3CrestPath.quadraticTo(prevX, prevY, midX, midY)
        }
        wave3Path.lineTo(width, height * 0.8f)
        wave3Path.close()

        drawPath(
            path = wave3Path,
            brush = Brush.verticalGradient(
                colors = listOf(
                    color2.copy(alpha = (0.48f * intensity * visibilityBoost).coerceAtMost(0.85f)),
                    color3.copy(alpha = (0.32f * intensity * visibilityBoost).coerceAtMost(0.65f)),
                    Color.Transparent
                ),
                startY = wave3BaseY - wave3Amp,
                endY = wave3BaseY + wave3Amp * 3.2f
            )
        )

        if (config.showWaveContours) {
            drawPath(
                path = wave3CrestPath,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        color2.copy(alpha = 0.25f),
                        color3.copy(alpha = (0.78f * intensity).coerceAtMost(0.95f)),
                        highlightColor.copy(alpha = (0.80f * intensity).coerceAtMost(0.95f)),
                        color2.copy(alpha = 0.25f)
                    )
                ),
                style = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // 6. FLUID SINUSOIDAL AURORA WAVE RIBBON 2 (Lower Dynamic Wave Ribbon)
        val wave2Path = Path()
        val wave2CrestPath = Path()
        val wave2BaseY = height * 0.64f
        val wave2Amp = height * 0.115f * (2f - pulse)

        val startY2 = wave2BaseY + cos(wavePhase2) * wave2Amp
        wave2Path.moveTo(0f, height)
        wave2Path.lineTo(0f, startY2)
        wave2CrestPath.moveTo(0f, startY2)

        for (i in 1..steps) {
            val currentX = i * stepX
            val prevX = (i - 1) * stepX
            val currentY = wave2BaseY +
                    sin(wavePhase2 + (i.toFloat() / steps) * 2.5f * PI.toFloat()) * wave2Amp +
                    cos(wavePhase1 + (i.toFloat() / steps) * 3f * PI.toFloat()) * (wave2Amp * 0.35f)
            val prevY = wave2BaseY +
                    sin(wavePhase2 + ((i - 1).toFloat() / steps) * 2.5f * PI.toFloat()) * wave2Amp +
                    cos(wavePhase1 + ((i - 1).toFloat() / steps) * 3f * PI.toFloat()) * (wave2Amp * 0.35f)

            val midX = (prevX + currentX) / 2f
            val midY = (prevY + currentY) / 2f
            wave2Path.quadraticTo(prevX, prevY, midX, midY)
            wave2CrestPath.quadraticTo(prevX, prevY, midX, midY)
        }
        wave2Path.lineTo(width, height)
        wave2Path.close()

        drawPath(
            path = wave2Path,
            brush = Brush.verticalGradient(
                colors = listOf(
                    color3.copy(alpha = (0.55f * intensity * pulse * visibilityBoost).coerceAtMost(0.88f)),
                    color4.copy(alpha = (0.35f * intensity * visibilityBoost).coerceAtMost(0.7f)),
                    Color.Transparent
                ),
                startY = wave2BaseY - wave2Amp,
                endY = wave2BaseY + wave2Amp * 2.8f
            )
        )

        // Glowing Wave Crest Contour 2
        if (config.showWaveContours) {
            drawPath(
                path = wave2CrestPath,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        color3.copy(alpha = 0.2f),
                        highlightColor.copy(alpha = (0.85f * intensity).coerceAtMost(1f)),
                        color4.copy(alpha = (0.75f * intensity).coerceAtMost(1f)),
                        highlightColor.copy(alpha = (0.85f * intensity).coerceAtMost(1f)),
                        color3.copy(alpha = 0.2f)
                    )
                ),
                style = Stroke(width = 2.8.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // 7. DIAGONAL NORTHERN LIGHTS CURTAIN (Curved luminous aurora ribbon)
        val curtainPath = Path()
        val curtainStartX = width * (0.1f + 0.15f * sin(wavePhase1 * 0.6f))
        val curtainControl1X = width * (0.85f + 0.12f * cos(wavePhase2 * 0.7f))
        val curtainControl1Y = height * 0.38f
        val curtainControl2X = width * (0.15f + 0.14f * cos(wavePhase1 * 0.8f))
        val curtainControl2Y = height * 0.68f
        val curtainEndX = width * (0.92f + 0.08f * sin(wavePhase2 * 0.9f))

        curtainPath.moveTo(curtainStartX, 0f)
        curtainPath.cubicTo(
            curtainControl1X, curtainControl1Y,
            curtainControl2X, curtainControl2Y,
            curtainEndX, height
        )
        curtainPath.lineTo(curtainEndX + width * 0.35f, height)
        curtainPath.cubicTo(
            curtainControl2X + width * 0.3f, curtainControl2Y,
            curtainControl1X + width * 0.3f, curtainControl1Y,
            curtainStartX + width * 0.35f, 0f
        )
        curtainPath.close()

        drawPath(
            path = curtainPath,
            brush = Brush.linearGradient(
                colors = listOf(
                    highlightColor.copy(alpha = (0.42f * intensity * pulse * visibilityBoost).coerceAtMost(0.8f)),
                    color1.copy(alpha = (0.32f * intensity * visibilityBoost).coerceAtMost(0.7f)),
                    color2.copy(alpha = (0.22f * intensity * visibilityBoost).coerceAtMost(0.5f)),
                    Color.Transparent
                ),
                start = Offset(curtainStartX, 0f),
                end = Offset(curtainEndX, height)
            )
        )

        // 8. LUMINOUS FLOATING PARTICLES & ISLAMIC CELESTIAL STARS (নূরানি আলোককণা ও তারকা)
        if (config.showParticles) {
            drawCelestialParticlesAndStars(
                width = width,
                height = height,
                progress = particleRise,
                rotation = starRotation,
                intensity = intensity,
                highlight = highlightColor,
                accent = color1,
                secondaryAccent = color2
            )
        }
    }
}

/**
 * Draws floating spiritual light motes and gentle rotating celestial Islamic stars (৪-কোণী নূরানি তারকা).
 */
private fun DrawScope.drawCelestialParticlesAndStars(
    width: Float,
    height: Float,
    progress: Float,
    rotation: Float,
    intensity: Float,
    highlight: Color,
    accent: Color,
    secondaryAccent: Color
) {
    val particleCount = 22
    for (i in 0 until particleCount) {
        val seedX = ((i * 37 + 19) % 100) / 100f
        val seedY = ((i * 53 + 29) % 100) / 100f
        val speedFactor = 0.55f + ((i * 17) % 10) / 12f
        val swayAmp = width * (0.025f + ((i * 7) % 5) * 0.008f)
        val phaseOffset = i * (PI.toFloat() / 4f)

        // Particles rise slowly and loop seamlessly
        val rawY = (seedY - (progress * speedFactor)) % 1f
        val normY = if (rawY < 0f) rawY + 1f else rawY
        val currentY = normY * height

        val sway = sin((progress * 2f * PI.toFloat() * speedFactor) + phaseOffset) * swayAmp
        val currentX = (seedX * width + sway).coerceIn(0f, width)

        // Twinkle pulse
        val twinkle = (0.45f + 0.55f * sin(progress * 6f * PI.toFloat() + phaseOffset)).coerceIn(0.2f, 1f)
        val baseAlpha = (0.55f * intensity * twinkle).coerceIn(0f, 0.95f)

        val isStar = (i % 4 == 0) // Every 4th item is a shimmering 4-point Islamic celestial star
        val particleColor = when (i % 3) {
            0 -> highlight
            1 -> accent
            else -> secondaryAccent
        }

        if (isStar) {
            // Draw rotating 4-point Islamic starlight sparkle
            val starSize = 5.5f + (i % 3) * 2.0f
            val radAngle = (rotation + i * 45f) * (PI.toFloat() / 180f)
            val cosA = cos(radAngle)
            val sinA = sin(radAngle)

            // Outer glow halo
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        particleColor.copy(alpha = baseAlpha * 0.9f),
                        particleColor.copy(alpha = baseAlpha * 0.25f),
                        Color.Transparent
                    ),
                    center = Offset(currentX, currentY),
                    radius = starSize * 3.2f
                ),
                center = Offset(currentX, currentY),
                radius = starSize * 3.2f
            )

            // Star diamond rays
            val starPath = Path().apply {
                val top = Offset(currentX - sinA * starSize, currentY + cosA * starSize)
                val bottom = Offset(currentX + sinA * starSize, currentY - cosA * starSize)
                val right = Offset(currentX + cosA * starSize, currentY + sinA * starSize)
                val left = Offset(currentX - cosA * starSize, currentY - sinA * starSize)

                moveTo(top.x, top.y)
                quadraticTo(currentX, currentY, right.x, right.y)
                quadraticTo(currentX, currentY, bottom.x, bottom.y)
                quadraticTo(currentX, currentY, left.x, left.y)
                quadraticTo(currentX, currentY, top.x, top.y)
                close()
            }

            drawPath(
                path = starPath,
                color = Color.White.copy(alpha = baseAlpha)
            )
        } else {
            // Normal radiant light mote
            val particleRadius = 2.5f + ((i % 4) * 1.2f)

            // Outer ethereal glow halo
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        particleColor.copy(alpha = baseAlpha * 0.85f),
                        particleColor.copy(alpha = baseAlpha * 0.30f),
                        Color.Transparent
                    ),
                    center = Offset(currentX, currentY),
                    radius = particleRadius * 3.6f
                ),
                center = Offset(currentX, currentY),
                radius = particleRadius * 3.6f
            )

            // Inner bright spark core
            drawCircle(
                color = Color.White.copy(alpha = baseAlpha),
                radius = particleRadius * 0.85f,
                center = Offset(currentX, currentY)
            )
        }
    }
}
