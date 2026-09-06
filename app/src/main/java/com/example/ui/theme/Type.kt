package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.data.model.BanglaFont
import com.example.data.model.BanglaFontWeight
import com.example.data.model.EnglishFont
import com.example.data.model.PrimaryFontPreference

private val englishFontCache = mutableMapOf<EnglishFont, FontFamily>()
private val banglaFontCache = mutableMapOf<BanglaFont, FontFamily>()

val LocalAppFontFamily = compositionLocalOf<FontFamily> { FontFamily.SansSerif }
val LocalEnglishFontFamily = compositionLocalOf<FontFamily> { FontFamily.SansSerif }
val LocalBanglaFontFamily = compositionLocalOf<FontFamily> { FontFamily.Default }

fun getEnglishFontFamily(font: EnglishFont): FontFamily {
    return englishFontCache.getOrPut(font) {
        val resId = font.fontResId ?: return@getOrPut FontFamily.SansSerif
        FontFamily(Font(resId = resId))
    }
}

fun getBanglaFontFamily(font: BanglaFont): FontFamily {
    return banglaFontCache.getOrPut(font) {
        val resId = font.fontResId ?: return@getOrPut FontFamily.Default
        FontFamily(Font(resId = resId))
    }
}

private val dualFontCache = mutableMapOf<Pair<EnglishFont, BanglaFont>, FontFamily>()

fun getDualActiveFontFamily(
    englishFont: EnglishFont = EnglishFont.ROBOTO,
    banglaFont: BanglaFont = BanglaFont.NOTO_SANS_BENGALI
): FontFamily {
    return dualFontCache.getOrPut(englishFont to banglaFont) {
        // When a custom Bangla font is chosen (e.g. Noto Sans, Hind Siliguri, Anek Bangla, Tiro Bangla),
        // it must be the PRIMARY font in the font family so that the text engine renders Bengali characters
        // with that specific Bangla font's glyphs, matras, and ligatures.
        val fonts = mutableListOf<Font>()

        // 1) Bangla font first: Ensures selected Bangla font typography applies to all Bengali text
        if (banglaFont.fontResId != null) {
            fonts.add(Font(resId = banglaFont.fontResId))
        }

        // 2) English font: Renders Latin characters, numbers, and symbols
        if (englishFont != EnglishFont.SYSTEM_SANS && englishFont.fontResId != null) {
            fonts.add(Font(resId = englishFont.fontResId))
        }

        if (fonts.isEmpty()) {
            FontFamily.SansSerif
        } else {
            FontFamily(fonts)
        }
    }
}

fun getActiveAppFontFamily(
    englishFont: EnglishFont = EnglishFont.ROBOTO,
    banglaFont: BanglaFont = BanglaFont.NOTO_SANS_BENGALI,
    primaryPreference: PrimaryFontPreference = PrimaryFontPreference.BANGLA_PRIMARY
): FontFamily {
    if (primaryPreference == PrimaryFontPreference.ENGLISH_PRIMARY && englishFont.fontResId != null) {
        val fonts = mutableListOf<Font>()
        fonts.add(Font(resId = englishFont.fontResId))
        if (banglaFont.fontResId != null) {
            fonts.add(Font(resId = banglaFont.fontResId))
        }
        return FontFamily(fonts)
    }
    return getDualActiveFontFamily(englishFont, banglaFont)
}

fun getAppTypography(
    englishFont: EnglishFont = EnglishFont.ROBOTO,
    banglaFont: BanglaFont = BanglaFont.NOTO_SANS_BENGALI,
    weight: BanglaFontWeight = BanglaFontWeight.NORMAL,
    primaryPreference: PrimaryFontPreference = PrimaryFontPreference.BANGLA_PRIMARY
): Typography {
    val family = getActiveAppFontFamily(englishFont, banglaFont, primaryPreference)

    val baseWeight = when (weight) {
        BanglaFontWeight.THIN -> FontWeight.W200
        BanglaFontWeight.EXTRA_LIGHT -> FontWeight.W200
        BanglaFontWeight.LIGHT -> FontWeight.W300
        BanglaFontWeight.NORMAL -> FontWeight.W400
        BanglaFontWeight.MEDIUM -> FontWeight.W500
        BanglaFontWeight.SEMI_BOLD -> FontWeight.W600
        BanglaFontWeight.BOLD -> FontWeight.W700
        BanglaFontWeight.EXTRA_BOLD -> FontWeight.W800
        BanglaFontWeight.BLACK -> FontWeight.W900
    }

    val mediumWeight = when (weight) {
        BanglaFontWeight.THIN -> FontWeight.W300
        BanglaFontWeight.EXTRA_LIGHT -> FontWeight.W300
        BanglaFontWeight.LIGHT -> FontWeight.W400
        BanglaFontWeight.NORMAL -> FontWeight.W500
        BanglaFontWeight.MEDIUM -> FontWeight.W600
        BanglaFontWeight.SEMI_BOLD -> FontWeight.W600
        BanglaFontWeight.BOLD, BanglaFontWeight.EXTRA_BOLD, BanglaFontWeight.BLACK -> FontWeight.W700
    }

    val boldWeight = when (weight) {
        BanglaFontWeight.THIN -> FontWeight.W400
        BanglaFontWeight.EXTRA_LIGHT -> FontWeight.W400
        BanglaFontWeight.LIGHT -> FontWeight.W500
        BanglaFontWeight.NORMAL -> FontWeight.W600
        BanglaFontWeight.MEDIUM -> FontWeight.W600
        BanglaFontWeight.SEMI_BOLD -> FontWeight.W700
        BanglaFontWeight.BOLD -> FontWeight.W800
        BanglaFontWeight.EXTRA_BOLD, BanglaFontWeight.BLACK -> FontWeight.W900
    }

    return Typography(
        displayLarge = TextStyle(
            fontFamily = family,
            fontWeight = boldWeight,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = (-0.5).sp
        ),
        displayMedium = TextStyle(
            fontFamily = family,
            fontWeight = boldWeight,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = (-0.3).sp
        ),
        displaySmall = TextStyle(
            fontFamily = family,
            fontWeight = boldWeight,
            fontSize = 24.sp,
            lineHeight = 32.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = family,
            fontWeight = boldWeight,
            fontSize = 24.sp,
            lineHeight = 32.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = family,
            fontWeight = boldWeight,
            fontSize = 20.sp,
            lineHeight = 28.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = family,
            fontWeight = boldWeight,
            fontSize = 18.sp,
            lineHeight = 26.sp
        ),
        titleLarge = TextStyle(
            fontFamily = family,
            fontWeight = boldWeight,
            fontSize = 18.sp,
            lineHeight = 26.sp
        ),
        titleMedium = TextStyle(
            fontFamily = family,
            fontWeight = mediumWeight,
            fontSize = 16.sp,
            lineHeight = 23.sp
        ),
        titleSmall = TextStyle(
            fontFamily = family,
            fontWeight = mediumWeight,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = family,
            fontWeight = baseWeight,
            fontSize = 15.sp,
            lineHeight = 23.sp,
            letterSpacing = 0.15.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = family,
            fontWeight = baseWeight,
            fontSize = 13.5.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.15.sp
        ),
        bodySmall = TextStyle(
            fontFamily = family,
            fontWeight = baseWeight,
            fontSize = 12.sp,
            lineHeight = 17.sp,
            letterSpacing = 0.15.sp
        ),
        labelLarge = TextStyle(
            fontFamily = family,
            fontWeight = mediumWeight,
            fontSize = 13.sp,
            lineHeight = 18.sp
        ),
        labelMedium = TextStyle(
            fontFamily = family,
            fontWeight = mediumWeight,
            fontSize = 11.5.sp,
            lineHeight = 16.sp
        ),
        labelSmall = TextStyle(
            fontFamily = family,
            fontWeight = mediumWeight,
            fontSize = 10.5.sp,
            lineHeight = 14.sp
        )
    )
}

fun getAppTypography(
    font: BanglaFont = BanglaFont.NOTO_SANS_BENGALI,
    weight: BanglaFontWeight = BanglaFontWeight.NORMAL
): Typography = getAppTypography(
    englishFont = EnglishFont.ROBOTO,
    banglaFont = font,
    weight = weight,
    primaryPreference = PrimaryFontPreference.BANGLA_PRIMARY
)

val Typography = getAppTypography(EnglishFont.ROBOTO, BanglaFont.NOTO_SANS_BENGALI, BanglaFontWeight.NORMAL, PrimaryFontPreference.BANGLA_PRIMARY)

