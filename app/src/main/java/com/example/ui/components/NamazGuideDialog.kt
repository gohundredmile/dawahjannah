package com.example.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.screens.salah.SalahGuideMainScreen

data class NamazRakatGuide(
    val nameBn: String,
    val totalRakatBn: String,
    val breakdownBn: String,
    val timeInfoBn: String
)

/**
 * Complete Salah Guide Dialog.
 * Delegates to SalahGuideMainScreen covering all 10 modules:
 * Lessons, Step-by-Step, Rakats, Audio Recitations, Taharah/Wudu,
 * Conditions/Fard, Mistakes/Sajdah Sahw, Special Prayers, Rulings, and FAQ.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NamazGuideDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            SalahGuideMainScreen(
                onNavigateBack = onDismiss
            )
        }
    }
}

