package com.example.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Controller interface provided to all screens via CompositionLocal to control
 * font size decrease (A-) and increase (A+) globally with persistent storage.
 */
data class FontScaleController(
    val scale: Float = 1.0f,
    val canDecrease: Boolean = true,
    val canIncrease: Boolean = true,
    val onDecrease: () -> Unit = {},
    val onIncrease: () -> Unit = {},
    val onReset: () -> Unit = {}
) {
    val currentScale: Float get() = scale
}

val LocalFontScaleController = staticCompositionLocalOf<FontScaleController?> { null }

/**
 * Standard font scaling action buttons (A- and A+) styled matching the app top bar.
 */
@Composable
fun FontSizeActionButtons(
    modifier: Modifier = Modifier,
    controller: FontScaleController? = LocalFontScaleController.current
) {
    if (controller == null) return

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        // Decrease Font (A-)
        IconButton(
            onClick = controller.onDecrease,
            enabled = controller.canDecrease,
            modifier = Modifier
                .size(36.dp)
                .testTag("action_decrease_font")
        ) {
            Text(
                text = "A-",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = if (controller.canDecrease) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
                }
            )
        }

        // Increase Font (A+)
        IconButton(
            onClick = controller.onIncrease,
            enabled = controller.canIncrease,
            modifier = Modifier
                .size(36.dp)
                .testTag("action_increase_font")
        ) {
            Text(
                text = "A+",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = if (controller.canIncrease) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
                }
            )
        }
    }
}

/**
 * Global Font Scale Action Buttons (A- and A+) matching the user's design across every window.
 */
@Composable
fun FontScaleActionButtons(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    controller: FontScaleController? = LocalFontScaleController.current,
    onDecrease: () -> Unit = controller?.onDecrease ?: {},
    onIncrease: () -> Unit = controller?.onIncrease ?: {},
    currentScale: Float = controller?.scale ?: 1.0f
) {
    val canDec = controller?.canDecrease ?: (currentScale > 0.82f)
    val canInc = controller?.canIncrease ?: (currentScale < 1.43f)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Decrease Font Size (A-)
        IconButton(
            onClick = onDecrease,
            enabled = canDec,
            modifier = Modifier
                .size(36.dp)
                .testTag("action_decrease_font")
        ) {
            Text(
                text = "A-",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = if (canDec) color else color.copy(alpha = 0.38f)
            )
        }

        // Increase Font Size (A+)
        IconButton(
            onClick = onIncrease,
            enabled = canInc,
            modifier = Modifier
                .size(36.dp)
                .testTag("action_increase_font")
        ) {
            Text(
                text = "A+",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = if (canInc) color else color.copy(alpha = 0.38f)
            )
        }
    }
}
