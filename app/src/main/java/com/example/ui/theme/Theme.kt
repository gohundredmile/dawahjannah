package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.CompositionLocalProvider
import com.example.data.model.BanglaFont
import com.example.data.model.BanglaFontWeight
import com.example.data.model.EnglishFont
import com.example.data.model.PrimaryFontPreference
import com.example.data.model.ThemeMode
import com.example.data.model.ThemeStyle

private val EmeraldLightColors = lightColorScheme(
    primary = NaturalForestGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE7F3ED),
    onPrimaryContainer = NaturalForestGreen,
    secondary = NaturalAmberGold,
    onSecondary = Color.White,
    secondaryContainer = NaturalCreamGold,
    onSecondaryContainer = NaturalForestGreen,
    tertiary = Color(0xFFB45309),
    background = NaturalBackgroundLight,
    surface = NaturalSurfaceLight,
    onBackground = NaturalTextPrimaryLight,
    onSurface = NaturalTextPrimaryLight,
    surfaceVariant = NaturalSurfaceVariantLight,
    onSurfaceVariant = NaturalTextSecondaryLight,
    outline = NaturalBorderLight
)

private val EmeraldDarkColors = darkColorScheme(
    primary = NaturalForestGreenDark,
    onPrimary = Color(0xFF022C22),
    primaryContainer = Color(0xFF1B3B2F),
    onPrimaryContainer = Color(0xFFA7F3D0),
    secondary = Color(0xFFFBBF24),
    onSecondary = Color(0xFF451A03),
    secondaryContainer = NaturalCreamGoldDark,
    onSecondaryContainer = Color(0xFFFDE68A),
    tertiary = Color(0xFFFCD34D),
    background = NaturalBackgroundDark,
    surface = NaturalSurfaceDark,
    onBackground = NaturalTextPrimaryDark,
    onSurface = NaturalTextPrimaryDark,
    surfaceVariant = NaturalSurfaceVariantDark,
    onSurfaceVariant = NaturalTextSecondaryDark,
    outline = NaturalBorderDark
)

private fun createLightPalette(
    primary: Color,
    secondary: Color,
    tertiary: Color = NaturalAmberGold,
    background: Color = NaturalBackgroundLight,
    surface: Color = Color.White,
    outline: Color = NaturalBorderLight
) = lightColorScheme(
    primary = primary,
    onPrimary = Color.White,
    primaryContainer = primary.copy(alpha = 0.12f),
    onPrimaryContainer = primary,
    secondary = secondary,
    onSecondary = Color.White,
    secondaryContainer = secondary.copy(alpha = 0.15f),
    onSecondaryContainer = secondary,
    tertiary = tertiary,
    background = background,
    surface = surface,
    onBackground = NaturalTextPrimaryLight,
    onSurface = NaturalTextPrimaryLight,
    surfaceVariant = surface,
    onSurfaceVariant = NaturalTextSecondaryLight,
    outline = outline
)

private fun createDarkPalette(
    primary: Color,
    secondary: Color,
    background: Color = Color(0xFF0F172A),
    surface: Color = Color(0xFF1E293B),
    outline: Color = Color(0xFF334155)
) = darkColorScheme(
    primary = primary,
    onPrimary = Color(0xFF022C22),
    primaryContainer = primary.copy(alpha = 0.2f),
    onPrimaryContainer = primary,
    secondary = secondary,
    onSecondary = Color(0xFF451A03),
    secondaryContainer = secondary.copy(alpha = 0.2f),
    onSecondaryContainer = secondary,
    background = background,
    surface = surface,
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF26354A),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = outline
)

fun getAppColorScheme(
    style: ThemeStyle,
    isDark: Boolean
): ColorScheme {
    return when (style) {
        ThemeStyle.COSMIC_AURORA -> if (isDark) {
            createDarkPalette(
                primary = Color(0xFF06B6D4),
                secondary = Color(0xFF10B981),
                background = Color(0xFF0B1120),
                surface = Color(0xFF151F32),
                outline = Color(0xFF2E3E5C)
            )
        } else {
            createLightPalette(
                primary = Color(0xFF0284C7),
                secondary = Color(0xFF0D9488),
                background = Color(0xFFF0F9FF)
            )
        }

        ThemeStyle.AURORA_AUSTRALIS -> if (isDark) {
            createDarkPalette(
                primary = Color(0xFFFB7185),
                secondary = Color(0xFFFDA4AF),
                background = Color(0xFF1F1015),
                surface = Color(0xFF2D1620)
            )
        } else {
            createLightPalette(
                primary = Color(0xFFE11D48),
                secondary = Color(0xFFF43F5E),
                background = Color(0xFFFFF1F2),
                outline = Color(0xFFFECDD3)
            )
        }

        ThemeStyle.GLACIAL_AURORA -> if (isDark) {
            createDarkPalette(
                primary = Color(0xFF38BDF8),
                secondary = Color(0xFF67E8F9),
                background = Color(0xFF0A1926),
                surface = Color(0xFF102638)
            )
        } else {
            createLightPalette(
                primary = Color(0xFF0284C7),
                secondary = Color(0xFF06B6D4),
                background = Color(0xFFF0F9FF),
                outline = Color(0xFFBAE6FD)
            )
        }

        ThemeStyle.SOLAR_DAWN -> if (isDark) {
            createDarkPalette(
                primary = Color(0xFFFB923C),
                secondary = Color(0xFFFBBF24),
                background = Color(0xFF1C1108),
                surface = Color(0xFF29190D)
            )
        } else {
            createLightPalette(
                primary = Color(0xFFD97706),
                secondary = Color(0xFFEA580C),
                background = Color(0xFFFFFBEB),
                outline = Color(0xFFFDE68A)
            )
        }

        ThemeStyle.ORCHID_DREAM -> if (isDark) {
            createDarkPalette(
                primary = Color(0xFFC084FC),
                secondary = Color(0xFFA855F7),
                background = Color(0xFF190F26),
                surface = Color(0xFF25173B)
            )
        } else {
            createLightPalette(
                primary = Color(0xFF9333EA),
                secondary = Color(0xFFA855F7),
                background = Color(0xFFFAF5FF),
                outline = Color(0xFFE9D5FF)
            )
        }

        ThemeStyle.SAGE_WHISPER -> if (isDark) {
            createDarkPalette(
                primary = Color(0xFF34D399),
                secondary = Color(0xFF6EE7B7),
                background = Color(0xFF0A1C14),
                surface = Color(0xFF122A1E)
            )
        } else {
            createLightPalette(
                primary = Color(0xFF059669),
                secondary = Color(0xFF10B981),
                background = Color(0xFFF0FDF4),
                outline = Color(0xFFA7F3D0)
            )
        }

        ThemeStyle.LAVENDER_MIST -> if (isDark) {
            createDarkPalette(
                primary = Color(0xFFA78BFA),
                secondary = Color(0xFFC4B5FD),
                background = Color(0xFF140F24),
                surface = Color(0xFF1F1738)
            )
        } else {
            createLightPalette(
                primary = Color(0xFF7C3AED),
                secondary = Color(0xFF8B5CF6),
                background = Color(0xFFF5F3FF),
                outline = Color(0xFFDDD6FE)
            )
        }

        ThemeStyle.PEACH_BLOSSOM -> if (isDark) {
            createDarkPalette(
                primary = Color(0xFFFB923C),
                secondary = Color(0xFFFDBA74),
                background = Color(0xFF1F120A),
                surface = Color(0xFF2E1B10)
            )
        } else {
            createLightPalette(
                primary = Color(0xFFEA580C),
                secondary = Color(0xFFF97316),
                background = Color(0xFFFFF7ED),
                outline = Color(0xFFFED7AA)
            )
        }

        ThemeStyle.OCEAN_BREEZE -> if (isDark) {
            createDarkPalette(
                primary = Color(0xFF38BDF8),
                secondary = Color(0xFF7DD3FC),
                background = Color(0xFF0B1926),
                surface = Color(0xFF12263A)
            )
        } else {
            createLightPalette(
                primary = Color(0xFF0284C7),
                secondary = Color(0xFF38BDF8),
                background = Color(0xFFF0F9FF),
                outline = Color(0xFFBAE6FD)
            )
        }

        ThemeStyle.CHAMOMILE_TEA -> if (isDark) {
            createDarkPalette(
                primary = Color(0xFFFBBF24),
                secondary = Color(0xFFFCD34D),
                background = Color(0xFF1C1708),
                surface = Color(0xFF2B230D)
            )
        } else {
            createLightPalette(
                primary = Color(0xFFB45309),
                secondary = Color(0xFFD97706),
                background = Color(0xFFFEFCE8),
                outline = Color(0xFFFEF08A)
            )
        }

        ThemeStyle.FOREST_BATHING, ThemeStyle.EMERALD_JANNAH -> if (isDark) {
            EmeraldDarkColors
        } else {
            createLightPalette(
                primary = Color(0xFF15803D),
                secondary = Color(0xFF16A34A),
                background = Color(0xFFF0FDF4),
                outline = Color(0xFFBBF7D0)
            )
        }

        ThemeStyle.ETHEREAL_SAND -> if (isDark) {
            createDarkPalette(
                primary = Color(0xFFFBBF24),
                secondary = Color(0xFFFDE68A),
                background = Color(0xFF1C140A),
                surface = Color(0xFF2A1F10)
            )
        } else {
            createLightPalette(
                primary = Color(0xFF92400E),
                secondary = Color(0xFFB45309),
                background = Color(0xFFFFFBEB),
                outline = Color(0xFFFDE68A)
            )
        }

        ThemeStyle.VELVET_PLUM -> if (isDark) {
            createDarkPalette(
                primary = Color(0xFFE879F9),
                secondary = Color(0xFFF0ABFC),
                background = Color(0xFF1E0E24),
                surface = Color(0xFF2C1536)
            )
        } else {
            createLightPalette(
                primary = Color(0xFFA21CAF),
                secondary = Color(0xFFC026D3),
                background = Color(0xFFFDF4FF),
                outline = Color(0xFFF5D0FE)
            )
        }

        ThemeStyle.SILVER_BIRCH -> if (isDark) {
            createDarkPalette(
                primary = Color(0xFF94A3B8),
                secondary = Color(0xFFCBD5E1),
                background = Color(0xFF10141A),
                surface = Color(0xFF1B222C)
            )
        } else {
            createLightPalette(
                primary = Color(0xFF334155),
                secondary = Color(0xFF475569),
                background = Color(0xFFF8FAFC),
                outline = Color(0xFFE2E8F0)
            )
        }

        ThemeStyle.MINT_MATCHA -> if (isDark) {
            createDarkPalette(
                primary = Color(0xFF4ADE80),
                secondary = Color(0xFF86EFAC),
                background = Color(0xFF0B1C12),
                surface = Color(0xFF122C1D)
            )
        } else {
            createLightPalette(
                primary = Color(0xFF16A34A),
                secondary = Color(0xFF22C55E),
                background = Color(0xFFF0FDF4),
                outline = Color(0xFFBBF7D0)
            )
        }
    }
}

@Composable
fun DawahTheme(
    themeStyle: ThemeStyle = ThemeStyle.FOREST_BATHING,
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    englishFont: EnglishFont = EnglishFont.ROBOTO,
    banglaFont: BanglaFont = BanglaFont.NOTO_SANS_BENGALI,
    banglaWeight: BanglaFontWeight = BanglaFontWeight.NORMAL,
    primaryPreference: PrimaryFontPreference = PrimaryFontPreference.BANGLA_PRIMARY,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = when {
        themeStyle.isDark -> true
        themeMode == ThemeMode.SYSTEM -> systemDark
        themeMode == ThemeMode.LIGHT -> false
        themeMode == ThemeMode.DARK -> true
        else -> false
    }

    val colorScheme = getAppColorScheme(themeStyle, isDark)
    val typography = getAppTypography(englishFont, banglaFont, banglaWeight, primaryPreference)
    val activeFamily = getActiveAppFontFamily(englishFont, banglaFont, primaryPreference)
    val activeEnglishFamily = getEnglishFontFamily(englishFont)
    val activeBanglaFamily = getBanglaFontFamily(banglaFont)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography
    ) {
        CompositionLocalProvider(
            LocalAppFontFamily provides activeFamily,
            LocalEnglishFontFamily provides activeEnglishFamily,
            LocalBanglaFontFamily provides activeBanglaFamily,
            LocalTextStyle provides typography.bodyMedium
        ) {
            content()
        }
    }
}
