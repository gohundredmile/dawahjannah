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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin

/**
 * Magnificent Live Aurora Shimmering Wallpaper:
 * An ethereal, living northern-lights atmosphere on a calm lite palette.
 * Floating glowing waves that slowly drift, breathe, and animate softly across the screen.
 * Applied throughout the app windows including Home Screen and content screens.
 */
@Composable
fun LiveAuroraWallpaperBackground(
    modifier: Modifier = Modifier,
    baseColor: Color = Color(0xFFF7FAF8)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "auroraTransition")

    // Slow organic rotating phases
    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(22000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase1"
    )

    val phase2 by infiniteTransition.animateFloat(
        initialValue = 2f * Math.PI.toFloat(),
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase2"
    )

    // Breathing pulse for ethereal glow
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.28f,
        targetValue = 0.48f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // 1. Serene lite background base (comforting mint-ivory)
        drawRect(
            color = baseColor
        )

        // 2. Aurora Wave 1: Soft Celestial Emerald (Top Left - Center)
        val center1 = Offset(
            x = width * 0.25f + cos(phase1) * (width * 0.2f),
            y = height * 0.2f + sin(phase1) * (height * 0.15f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF6EE7B7).copy(alpha = pulse * 0.82f),
                    Color(0xFFA7F3D0).copy(alpha = pulse * 0.42f),
                    Color.Transparent
                ),
                center = center1,
                radius = width * 0.85f
            ),
            center = center1,
            radius = width * 0.85f
        )

        // 3. Aurora Wave 2: Celestial Cyan / Sky Breeze (Top Right)
        val center2 = Offset(
            x = width * 0.75f + sin(phase2) * (width * 0.22f),
            y = height * 0.4f + cos(phase2) * (height * 0.18f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF7DD3FC).copy(alpha = pulse * 0.72f),
                    Color(0xFFBAE6FD).copy(alpha = pulse * 0.38f),
                    Color.Transparent
                ),
                center = center2,
                radius = width * 0.75f
            ),
            center = center2,
            radius = width * 0.75f
        )

        // 4. Aurora Wave 3: Warm Dawn Gold / Amber Glow (Bottom Center)
        val center3 = Offset(
            x = width * 0.45f + cos(phase2 * 0.8f) * (width * 0.25f),
            y = height * 0.75f + sin(phase1 * 0.8f) * (height * 0.15f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFDE68A).copy(alpha = pulse * 0.68f),
                    Color(0xFFFEF3C7).copy(alpha = pulse * 0.32f),
                    Color.Transparent
                ),
                center = center3,
                radius = width * 0.8f
            ),
            center = center3,
            radius = width * 0.8f
        )

        // 5. Aurora Wave 4: Lavender Spiritual Aura (Bottom Right)
        val center4 = Offset(
            x = width * 0.8f + sin(phase1 * 1.1f) * (width * 0.18f),
            y = height * 0.85f + cos(phase2 * 1.1f) * (height * 0.12f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFE9D5FF).copy(alpha = pulse * 0.58f),
                    Color(0xFFF3E8FF).copy(alpha = pulse * 0.28f),
                    Color.Transparent
                ),
                center = center4,
                radius = width * 0.65f
            ),
            center = center4,
            radius = width * 0.65f
        )
    }
}
