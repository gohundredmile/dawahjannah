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
    SOLAIMAN_LIPI(
        id = "solaiman_lipi",
        displayNameEn = "SOLAIMAN LIPI",
        displayNameBn = "সোলায়মান লিপি (জনপ্রিয় ও নিখুঁত)",
        googleFontName = "Solaiman Lipi",
        fontResId = R.font.font_solaiman_lipi
    ),
    KALPURUSH(
        id = "kalpurush",
        displayNameEn = "KALPURUSH",
        displayNameBn = "কালপুরুষ (মার্জিত ও চিরচেনা)",
        googleFontName = "Kalpurush",
        fontResId = R.font.font_kalpurush
    ),
    SIYAM_RUPALI(
        id = "siyam_rupali",
        displayNameEn = "SIYAM RUPALI",
        displayNameBn = "সিয়াম রূপালী (ক্লাসিক ও সাহিত্যিক)",
        googleFontName = "Siyam Rupali",
        fontResId = R.font.font_siyam_rupali
    ),
    NIKOSH(
        id = "nikosh",
        displayNameEn = "NIKOSH BAN",
        displayNameBn = "নিকোশ (অফিসিয়াল ও স্পষ্ট)",
        googleFontName = "Nikosh",
        fontResId = R.font.font_nikosh
    ),
    ADORSHO_LIPI(
        id = "adorsho_lipi",
        displayNameEn = "ADORSHO LIPI",
        displayNameBn = "আদর্শ লিপি (ঐতিহ্যবাহী পাঠযোগ্য)",
        googleFontName = "Adorsho Lipi",
        fontResId = R.font.font_adorsho_lipi
    ),
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

enum class FlipClockFont(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val googleFontName: String,
    @FontRes val fontResId: Int?,
    val fontWeight: FontWeight = FontWeight.Normal
) {
    RETRO_7SEGMENT(
        id = "retro_7segment",
        displayName = "HTC Sense 7-Segment (Original 1.4.8)",
        subtitle = "খাঁটি মেকানিক্যাল ৭-সেগমেন্ট স্প্লিট ফ্ল্যাপ (ডিফল্ট ও পারফেক্ট)",
        googleFontName = "HTC Sense Mechanical",
        fontResId = null,
        fontWeight = FontWeight.Normal
    ),
    MONTSERRAT_THIN(
        id = "montserrat_thin",
        displayName = "Montserrat Ultra Thin",
        subtitle = "আল্ট্রা থিন ও জ্যামিতিক মিনিমালিস্ট",
        googleFontName = "Montserrat",
        fontResId = R.font.montserrat,
        fontWeight = FontWeight.W200
    ),
    RALEWAY_THIN(
        id = "raleway_thin",
        displayName = "Raleway Elegant Thin",
        subtitle = "আর্টিস্টিক ও লাক্সারি কার্ভড ডিজিট",
        googleFontName = "Raleway",
        fontResId = R.font.raleway,
        fontWeight = FontWeight.W200
    ),
    JOST_LITE(
        id = "jost_lite",
        displayName = "Jost Bauhaus Lite",
        subtitle = "ফিউচুরা ইন্সপায়ার্ড সুনির্দিষ্ট রেজার ক্লিন",
        googleFontName = "Jost",
        fontResId = R.font.jost,
        fontWeight = FontWeight.W200
    ),
    POPPINS_LIGHT(
        id = "poppins_light",
        displayName = "Poppins Geometric Lite",
        subtitle = "সফট মিনিমালিস্ট ও পারফেক্ট ব্যালান্সড",
        googleFontName = "Poppins",
        fontResId = R.font.poppins,
        fontWeight = FontWeight.W300
    ),
    QUICKSAND_LITE(
        id = "quicksand_lite",
        displayName = "Quicksand Gentle Lite",
        subtitle = "রাউন্ডেড কর্নার ও দৃষ্টিনন্দন সফট টার্মিনাল",
        googleFontName = "Quicksand",
        fontResId = R.font.quicksand,
        fontWeight = FontWeight.W300
    ),
    RAJDHANI_THIN(
        id = "rajdhani_thin",
        displayName = "Rajdhani Cyber Thin",
        subtitle = "মডার্ন কনডেন্সড স্কয়ার্ড সাইবার ডিজিট",
        googleFontName = "Rajdhani",
        fontResId = R.font.rajdhani,
        fontWeight = FontWeight.W300
    ),
    SPACE_GROTESK(
        id = "space_grotesk",
        displayName = "Space Grotesk Lite",
        subtitle = "হাই-টেক মনোস্পেস ও মেকানিক্যাল নান্দনিকতা",
        googleFontName = "Space Grotesk",
        fontResId = R.font.space_grotesk,
        fontWeight = FontWeight.W300
    ),
    EXO_2_LITE(
        id = "exo_2_lite",
        displayName = "Exo 2 Futuristic Lite",
        subtitle = "ফিউচারিস্টিক ও এরোডাইনামিক ডিজিটাল লুক",
        googleFontName = "Exo 2",
        fontResId = R.font.exo_2,
        fontWeight = FontWeight.W200
    ),
    BARLOW_THIN(
        id = "barlow_thin",
        displayName = "Barlow Architectural Lite",
        subtitle = "লো-কনট্রাস্ট প্রিসিশন আধুনিক ফন্ট",
        googleFontName = "Barlow",
        fontResId = R.font.barlow,
        fontWeight = FontWeight.W200
    ),
    SYNE_LITE(
        id = "syne_lite",
        displayName = "Syne Avant-Garde",
        subtitle = "আই-ক্যাচিং ডিজাইনার ডিসপ্লে টাইপোগ্রাফি",
        googleFontName = "Syne",
        fontResId = R.font.syne,
        fontWeight = FontWeight.W400
    ),
    COMFORTAA_LITE(
        id = "comfortaa_lite",
        displayName = "Comfortaa Ultra Soft",
        subtitle = "আল্ট্রা স্মুথ সার্কুলার জ্যামিতি",
        googleFontName = "Comfortaa",
        fontResId = R.font.comfortaa,
        fontWeight = FontWeight.W300
    ),
    OSWALD_FLIP(
        id = "oswald_flip",
        displayName = "Oswald Station Flip",
        subtitle = "আইকনিক এয়ারপোর্ট ও স্টেশন স্প্লিট-ফ্ল্যাপ বোর্ড",
        googleFontName = "Oswald",
        fontResId = R.font.oswald,
        fontWeight = FontWeight.W300
    ),
    BEBAS_NEUE(
        id = "bebas_neue",
        displayName = "Bebas Neue Display",
        subtitle = "বোল্ড ও মার্জিত ক্ল্যাসিক স্প্লিট ক্লক",
        googleFontName = "Bebas Neue",
        fontResId = R.font.bebas_neue,
        fontWeight = FontWeight.Normal
    ),
    UBUNTU_LIGHT(
        id = "ubuntu_light",
        displayName = "Ubuntu Sense Lite",
        subtitle = "এইচটিসি সেন্স ফোন ক্লকের ক্লাসিক সৌন্দর্য",
        googleFontName = "Ubuntu Light",
        fontResId = R.font.font_ubuntu_light,
        fontWeight = FontWeight.W300
    ),
    OUTFIT_LITE(
        id = "outfit_lite",
        displayName = "Outfit Neo-Grotesque",
        subtitle = "প্রিমিয়াম ক্রিস্প স্টুডিও ফিনিশ",
        googleFontName = "Outfit",
        fontResId = R.font.font_outfit,
        fontWeight = FontWeight.W200
    )
}

