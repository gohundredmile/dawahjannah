package com.example.data.model

import androidx.annotation.FontRes
import androidx.compose.ui.text.font.FontWeight
import com.example.R

enum class EnglishFont(
    val id: String,
    val displayName: String,
    val subtitle: String,
    @FontRes val fontResId: Int?
) {
    ROBOTO(
        id = "roboto",
        displayName = "Roboto",
        subtitle = "Clean, Thin & Pristine",
        fontResId = R.font.font_roboto
    ),
    UBUNTU_SANS(
        id = "ubuntu_sans",
        displayName = "Ubuntu Sans",
        subtitle = "Modern, Humanist & Thin",
        fontResId = R.font.font_ubuntu_sans
    ),
    UBUNTU_LIGHT(
        id = "ubuntu_light",
        displayName = "Ubuntu Light",
        subtitle = "Ultra Sleek & Delicate",
        fontResId = R.font.font_ubuntu_light
    ),
    OUTFIT(
        id = "outfit",
        displayName = "Outfit",
        subtitle = "Geometric & Aesthetic",
        fontResId = R.font.font_outfit
    ),
    INTER(
        id = "inter",
        displayName = "Inter",
        subtitle = "Swiss Precision & Crisp",
        fontResId = R.font.font_inter
    ),
    SYSTEM_SANS(
        id = "system_sans",
        displayName = "System Sans",
        subtitle = "Native Platform Font",
        fontResId = null
    )
}

enum class BanglaFont(
    val id: String,
    val displayNameEn: String,
    val displayNameBn: String,
    val googleFontName: String,
    @FontRes val fontResId: Int?
) {
    NOTO_SANS_BENGALI(
        id = "noto_sans_bengali",
        displayNameEn = "NOTO SANS BENGALI",
        displayNameBn = "নোটো সান্স বাংলা (স্পষ্ট ও সাবলীল)",
        googleFontName = "Noto Sans Bengali",
        fontResId = R.font.font_noto_bengali
    ),
    HIND_SILIGURI(
        id = "hind_siliguri",
        displayNameEn = "HIND SILIGURI",
        displayNameBn = "হিন্দ শিলিগুড়ি (সুন্দর ও মার্জিত)",
        googleFontName = "Hind Siliguri",
        fontResId = R.font.font_hind_siliguri
    ),
    ANEK_BANGLA(
        id = "anek_bangla",
        displayNameEn = "ANEK BANGLA",
        displayNameBn = "আনেক বাংলা (আধুনিক ও জ্যামিতিক)",
        googleFontName = "Anek Bangla",
        fontResId = R.font.font_anek_bangla
    ),
    TIRO_BANGLA(
        id = "tiro_bangla",
        displayNameEn = "TIRO BANGLA",
        displayNameBn = "তিরো বাংলা (ঐতিহ্যবাহী সাহিত্যিক)",
        googleFontName = "Tiro Bangla",
        fontResId = R.font.font_tiro_bangla
    ),
    SYSTEM_DEFAULT(
        id = "system_default",
        displayNameEn = "SYSTEM DEFAULT",
        displayNameBn = "ডিফল্ট সিস্টেম ফন্ট",
        googleFontName = "Roboto",
        fontResId = null
    )
}

enum class PrimaryFontPreference(
    val label: String,
    val description: String
) {
    ENGLISH_PRIMARY("ইংরেজি প্রাধান্য (English Primary)", "ইংরেজি ও সংখ্যা নির্বাচিত ফন্টে, বাংলা স্বয়ংক্রিয় নিখুঁত ফন্টে"),
    BANGLA_PRIMARY("বাংলা প্রাধান্য (Bangla Primary)", "বাংলা নির্বাচিত ফন্টে, ইংরেজি সুন্দর আধুনিক ফন্টে")
}

enum class BanglaFontWeight(
    val label: String,
    val weightValue: Int,
    val fontWeight: FontWeight
) {
    THIN("Thin (পাতলা)", 100, FontWeight.W100),
    EXTRA_LIGHT("Extra Light", 200, FontWeight.W200),
    LIGHT("Light (হালকা)", 300, FontWeight.W300),
    NORMAL("Normal (স্বাভাবিক)", 400, FontWeight.W400),
    MEDIUM("Medium (মাঝারি)", 500, FontWeight.W500),
    SEMI_BOLD("Semi Bold", 600, FontWeight.W600),
    BOLD("Bold (গাঢ়)", 700, FontWeight.W700),
    EXTRA_BOLD("Extra Bold", 800, FontWeight.W800),
    BLACK("Black (আল্ট্রা বোল্ড)", 900, FontWeight.W900);

    companion object {
        fun fromValue(value: Int): BanglaFontWeight {
            return entries.minByOrNull { Math.abs(it.weightValue - value) } ?: NORMAL
        }
    }
}
