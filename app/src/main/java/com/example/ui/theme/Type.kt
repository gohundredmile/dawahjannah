package com.example.ui.theme

import android.content.Context
import android.os.Build
import androidx.compose.material3.Typography
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BanglaFont
import com.example.data.model.BanglaFontWeight
import com.example.data.model.EnglishFont
import com.example.data.model.PrimaryFontPreference

private val englishFontCache = mutableMapOf<EnglishFont, FontFamily>()
private val banglaFontCache = mutableMapOf<BanglaFont, FontFamily>()

val LocalAppFontFamily = compositionLocalOf<FontFamily> { FontFamily.SansSerif }
val LocalEnglishFontFamily = compositionLocalOf<FontFamily> { FontFamily.SansSerif }
val LocalBanglaFontFamily = compositionLocalOf<FontFamily> { FontFamily.Default }

val ArabicFontFamily = FontFamily(
    Font(resId = R.font.font_arabic_amiri, weight = FontWeight.Normal),
    Font(resId = R.font.font_arabic_amiri, weight = FontWeight.Medium),
    Font(resId = R.font.font_arabic_amiri, weight = FontWeight.Bold)
)
val LocalArabicFontFamily = compositionLocalOf<FontFamily> { ArabicFontFamily }

fun getEnglishFontFamily(font: EnglishFont): FontFamily {
    return englishFontCache.getOrPut(font) {
        val resId = font.fontResId ?: return@getOrPut FontFamily.SansSerif
        FontFamily(
            Font(resId = resId, weight = FontWeight.W100),
            Font(resId = resId, weight = FontWeight.W200),
            Font(resId = resId, weight = FontWeight.W300),
            Font(resId = resId, weight = FontWeight.W400),
            Font(resId = resId, weight = FontWeight.W500),
            Font(resId = resId, weight = FontWeight.W600),
            Font(resId = resId, weight = FontWeight.W700),
            Font(resId = resId, weight = FontWeight.W800),
            Font(resId = resId, weight = FontWeight.W900)
        )
    }
}

fun getBanglaFontFamily(font: BanglaFont): FontFamily {
    return banglaFontCache.getOrPut(font) {
        val resId = font.fontResId ?: return@getOrPut FontFamily.Default
        FontFamily(
            Font(resId = resId, weight = FontWeight.W100),
            Font(resId = resId, weight = FontWeight.W200),
            Font(resId = resId, weight = FontWeight.W300),
            Font(resId = resId, weight = FontWeight.W400),
            Font(resId = resId, weight = FontWeight.W500),
            Font(resId = resId, weight = FontWeight.W600),
            Font(resId = resId, weight = FontWeight.W700),
            Font(resId = resId, weight = FontWeight.W800),
            Font(resId = resId, weight = FontWeight.W900)
        )
    }
}

private val dualFontCache = mutableMapOf<Triple<EnglishFont, BanglaFont, Boolean>, FontFamily>()

/**
 * Dual Active Font Engine:
 * Combines the selected English font and Bangla font into a unified Typeface.
 *
 * On Android Q+ (API 29+):
 * Uses Android's Typeface.CustomFallbackBuilder to create an authentic composite Typeface
 * where English/Latin characters are rendered by the selected English font, and Bengali
 * characters are seamlessly rendered by the selected Bangla font as the primary custom fallback.
 *
 * Result:
 * - Selecting any Bangla font in settings updates all Bengali text and numbers across the app.
 * - Selecting any English font in settings updates all English text and numbers across the app.
 * - Both fonts remain active simultaneously everywhere.
 */
fun getDualActiveFontFamily(
    englishFont: EnglishFont = EnglishFont.ROBOTO,
    banglaFont: BanglaFont = BanglaFont.NOTO_SANS_BENGALI,
    context: Context? = null
): FontFamily {
    val key = Triple(englishFont, banglaFont, context != null)
    return dualFontCache.getOrPut(key) {
        if (context != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                val res = context.resources
                val englishRes = englishFont.fontResId ?: R.font.font_roboto
                val banglaRes = banglaFont.fontResId ?: R.font.font_noto_bengali

                val engFont = android.graphics.fonts.Font.Builder(res, englishRes).build()
                val engFamily = android.graphics.fonts.FontFamily.Builder(engFont).build()

                val bngFont = android.graphics.fonts.Font.Builder(res, banglaRes).build()
                val bngFamily = android.graphics.fonts.FontFamily.Builder(bngFont).build()

                val customTypeface = android.graphics.Typeface.CustomFallbackBuilder(engFamily)
                    .addCustomFallback(bngFamily)
                    .build()

                return@getOrPut FontFamily(customTypeface)
            } catch (e: Throwable) {
                // Fallback to Compose font list below
            }
        }

        // Fallback: If context is null or below API 29, prioritize the selected Bangla font
        // so that the 90% Bengali text throughout the app always renders with the chosen font.
        val banglaRes = banglaFont.fontResId ?: R.font.font_noto_bengali
        FontFamily(Font(resId = banglaRes))
    }
}

fun getActiveAppFontFamily(
    englishFont: EnglishFont = EnglishFont.ROBOTO,
    banglaFont: BanglaFont = BanglaFont.NOTO_SANS_BENGALI,
    primaryPreference: PrimaryFontPreference = PrimaryFontPreference.BANGLA_PRIMARY,
    context: Context? = null
): FontFamily {
    return getDualActiveFontFamily(englishFont, banglaFont, context)
}

fun getAppTypography(
    englishFont: EnglishFont = EnglishFont.ROBOTO,
    banglaFont: BanglaFont = BanglaFont.NOTO_SANS_BENGALI,
    weight: BanglaFontWeight = BanglaFontWeight.NORMAL,
    primaryPreference: PrimaryFontPreference = PrimaryFontPreference.BANGLA_PRIMARY,
    context: Context? = null
): Typography {
    val family = getActiveAppFontFamily(englishFont, banglaFont, primaryPreference, context)

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
            lineHeight = 22.sp,
            letterSpacing = 0.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = family,
            fontWeight = baseWeight,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            letterSpacing = 0.sp
        ),
        bodySmall = TextStyle(
            fontFamily = family,
            fontWeight = baseWeight,
            fontSize = 12.5.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.sp
        ),
        labelLarge = TextStyle(
            fontFamily = family,
            fontWeight = mediumWeight,
            fontSize = 13.5.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.sp
        ),
        labelMedium = TextStyle(
            fontFamily = family,
            fontWeight = mediumWeight,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.sp
        ),
        labelSmall = TextStyle(
            fontFamily = family,
            fontWeight = mediumWeight,
            fontSize = 11.sp,
            lineHeight = 15.sp,
            letterSpacing = 0.sp
        )
    )
}

fun getAppTypography(
    englishFont: EnglishFont = EnglishFont.ROBOTO,
    banglaFont: BanglaFont = BanglaFont.NOTO_SANS_BENGALI,
    weight: BanglaFontWeight = BanglaFontWeight.NORMAL,
    primaryPreference: PrimaryFontPreference = PrimaryFontPreference.BANGLA_PRIMARY
): Typography = getAppTypography(englishFont, banglaFont, weight, primaryPreference, null)

fun getAppTypography(
    font: BanglaFont = BanglaFont.HIND_SILIGURI,
    weight: BanglaFontWeight = BanglaFontWeight.NORMAL
): Typography = getAppTypography(
    englishFont = EnglishFont.ROBOTO,
    banglaFont = font,
    weight = weight,
    primaryPreference = PrimaryFontPreference.BANGLA_PRIMARY
)

val Typography = getAppTypography(EnglishFont.ROBOTO, BanglaFont.HIND_SILIGURI, BanglaFontWeight.NORMAL, PrimaryFontPreference.BANGLA_PRIMARY)

