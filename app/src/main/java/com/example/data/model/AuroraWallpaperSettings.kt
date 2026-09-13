package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class AuroraWavePreset(
    val id: String,
    val titleBn: String,
    val descriptionBn: String,
    val colors: List<Color>,
    val highlightColor: Color
) {
    THEME_DYNAMIC(
        id = "theme_dynamic",
        titleBn = "থিমের সাথে স্বয়ংক্রিয়",
        descriptionBn = "অ্যাক্টিভ থিমের কালার প্যালেটের সাথে মিলিয়ে সুমধুর জীবন্ত তরঙ্গ",
        colors = emptyList(),
        highlightColor = Color(0xFFFBBF24)
    ),
    JANNATI_EMERALD(
        id = "jannati_emerald",
        titleBn = "জান্নাতী নূর ও জমরুদ",
        descriptionBn = "পবিত্র স্বর্গীয় সবুজ, স্নিগ্ধ ফিরোজা ও সোনালী আভা",
        colors = listOf(
            Color(0xFF059669),
            Color(0xFF10B981),
            Color(0xFF34D399),
            Color(0xFF6EE7B7)
        ),
        highlightColor = Color(0xFFFBBF24)
    ),
    SOLAR_DAWN(
        id = "solar_dawn",
        titleBn = "সোনালী উষা ও ফজর",
        descriptionBn = "সকালের উষা, সোনালী সূর্যোদয় ও প্রশান্ত নূরানি আলো",
        colors = listOf(
            Color(0xFFD97706),
            Color(0xFFF59E0B),
            Color(0xFFFBBF24),
            Color(0xFFFB923C)
        ),
        highlightColor = Color(0xFFFEF3C7)
    ),
    COSMIC_SAPPHIRE(
        id = "cosmic_sapphire",
        titleBn = "কসমিক গ্যালাক্সি ও তারা",
        descriptionBn = "গভীর মহাজাগতিক নীলকান্তমণি, আকাশী ও নীলিমা তরঙ্গ",
        colors = listOf(
            Color(0xFF0284C7),
            Color(0xFF06B6D4),
            Color(0xFF38BDF8),
            Color(0xFF818CF8)
        ),
        highlightColor = Color(0xFFE0F2FE)
    ),
    SPIRITUAL_LAVENDER(
        id = "spiritual_lavender",
        titleBn = "প্রশান্ত ল্যাভেন্ডার কুয়াশা",
        descriptionBn = "স্নিগ্ধ সুফি বেগুনি, ল্যাভেন্ডার ও প্রশান্তিময় আভা",
        colors = listOf(
            Color(0xFF7C3AED),
            Color(0xFF8B5CF6),
            Color(0xFFA78BFA),
            Color(0xFFC084FC)
        ),
        highlightColor = Color(0xFFF3E8FF)
    ),
    OCEAN_BREEZE(
        id = "ocean_breeze",
        titleBn = "সাগরীয় সুবাতাস",
        descriptionBn = "সামুদ্রিক ফিরোজা, শীতল বাতাস ও শান্ত নীল ঢেউ",
        colors = listOf(
            Color(0xFF0D9488),
            Color(0xFF14B8A6),
            Color(0xFF2DD4BF),
            Color(0xFF38BDF8)
        ),
        highlightColor = Color(0xFFCCFBF1)
    ),
    ROSE_AURORA(
        id = "rose_aurora",
        titleBn = "রোজ গোল্ড ও গুলশান",
        descriptionBn = "স্বর্গীয় গোলাপী নূর, সোনালী আভা ও স্নিগ্ধ রক্তিমা তরঙ্গ",
        colors = listOf(
            Color(0xFFE11D48),
            Color(0xFFFB7185),
            Color(0xFFF43F5E),
            Color(0xFFFBBF24)
        ),
        highlightColor = Color(0xFFFFF1F2)
    ),
    TURQUOISE_NEBULA(
        id = "turquoise_nebula",
        titleBn = "ফিরোজা নূর ও নীলাকাশ",
        descriptionBn = "উজ্জ্বল ফিরোজা, আসমানী আভা ও স্নিগ্ধ সবুজ সমুদ্র তরঙ্গ",
        colors = listOf(
            Color(0xFF059669),
            Color(0xFF06B6D4),
            Color(0xFF0EA5E9),
            Color(0xFF34D399)
        ),
        highlightColor = Color(0xFFE0F2FE)
    );

    companion object {
        fun fromId(id: String): AuroraWavePreset {
            return entries.find { it.id == id } ?: THEME_DYNAMIC
        }
    }
}

data class AuroraWallpaperConfig(
    val isEnabled: Boolean = true,
    val preset: AuroraWavePreset = AuroraWavePreset.THEME_DYNAMIC,
    val intensity: Float = 0.72f, // 0.40f (মৃদু), 0.72f (উজ্জ্বল ও দৃশ্যমান), 0.95f (দীপ্তিময়)
    val speed: Float = 1.0f,      // 0.6f (ধীর), 1.0f (স্বাভাবিক), 1.5f (গতিময়)
    val showParticles: Boolean = true,
    val showWaveContours: Boolean = true,
    val showAuroraRays: Boolean = true
) {
    fun getEffectiveColors(themeStyle: ThemeStyle): List<Color> {
        if (preset != AuroraWavePreset.THEME_DYNAMIC) {
            return preset.colors
        }
        return when (themeStyle) {
            ThemeStyle.COSMIC_AURORA -> listOf(
                Color(0xFF0E7490), Color(0xFF06B6D4), Color(0xFF10B981), Color(0xFF6366F1)
            )
            ThemeStyle.AURORA_AUSTRALIS -> listOf(
                Color(0xFFFB7185), Color(0xFFF43F5E), Color(0xFFFDA4AF), Color(0xFFFECDD3)
            )
            ThemeStyle.GLACIAL_AURORA -> listOf(
                Color(0xFF0284C7), Color(0xFF38BDF8), Color(0xFF7DD3FC), Color(0xFFBAE6FD)
            )
            ThemeStyle.SOLAR_DAWN -> listOf(
                Color(0xFFD97706), Color(0xFFF59E0B), Color(0xFFFBBF24), Color(0xFFFDE68A)
            )
            ThemeStyle.ORCHID_DREAM -> listOf(
                Color(0xFF9333EA), Color(0xFFA855F7), Color(0xFFC084FC), Color(0xFFE9D5FF)
            )
            ThemeStyle.SAGE_WHISPER -> listOf(
                Color(0xFF059669), Color(0xFF10B981), Color(0xFF34D399), Color(0xFFA7F3D0)
            )
            ThemeStyle.LAVENDER_MIST -> listOf(
                Color(0xFF7C3AED), Color(0xFF8B5CF6), Color(0xFFA78BFA), Color(0xFFDDD6FE)
            )
            ThemeStyle.PEACH_BLOSSOM -> listOf(
                Color(0xFFEA580C), Color(0xFFF97316), Color(0xFFFB923C), Color(0xFFFED7AA)
            )
            ThemeStyle.OCEAN_BREEZE -> listOf(
                Color(0xFF0284C7), Color(0xFF0D9488), Color(0xFF38BDF8), Color(0xFF99F6E4)
            )
            ThemeStyle.CHAMOMILE_TEA -> listOf(
                Color(0xFFCA8A04), Color(0xFFEAB308), Color(0xFFFACC15), Color(0xFFFEF08A)
            )
            ThemeStyle.FOREST_BATHING, ThemeStyle.EMERALD_JANNAH -> listOf(
                Color(0xFF047857), Color(0xFF10B981), Color(0xFF34D399), Color(0xFF6EE7B7)
            )
            ThemeStyle.ETHEREAL_SAND -> listOf(
                Color(0xFFB45309), Color(0xFFD97706), Color(0xFFFBBF24), Color(0xFFFDE68A)
            )
            ThemeStyle.VELVET_PLUM -> listOf(
                Color(0xFF831843), Color(0xFFBE185D), Color(0xFFDB2777), Color(0xFFF472B6)
            )
            ThemeStyle.SILVER_BIRCH -> listOf(
                Color(0xFF475569), Color(0xFF64748B), Color(0xFF94A3B8), Color(0xFFCBD5E1)
            )
            ThemeStyle.MINT_MATCHA -> listOf(
                Color(0xFF059669), Color(0xFF10B981), Color(0xFF6EE7B7), Color(0xFFA7F3D0)
            )
        }
    }

    fun getEffectiveHighlight(themeStyle: ThemeStyle): Color {
        if (preset != AuroraWavePreset.THEME_DYNAMIC) {
            return preset.highlightColor
        }
        return when (themeStyle) {
            ThemeStyle.COSMIC_AURORA -> Color(0xFF38BDF8)
            ThemeStyle.AURORA_AUSTRALIS -> Color(0xFFFDA4AF)
            ThemeStyle.GLACIAL_AURORA -> Color(0xFFBAE6FD)
            ThemeStyle.SOLAR_DAWN -> Color(0xFFFEF3C7)
            ThemeStyle.ORCHID_DREAM -> Color(0xFFF3E8FF)
            ThemeStyle.SAGE_WHISPER -> Color(0xFFD1FAE5)
            ThemeStyle.LAVENDER_MIST -> Color(0xFFEDE9FE)
            ThemeStyle.PEACH_BLOSSOM -> Color(0xFFFFEDD5)
            ThemeStyle.OCEAN_BREEZE -> Color(0xFFE0F2FE)
            ThemeStyle.CHAMOMILE_TEA -> Color(0xFFFEF9C3)
            ThemeStyle.FOREST_BATHING, ThemeStyle.EMERALD_JANNAH -> Color(0xFFFDE68A)
            ThemeStyle.ETHEREAL_SAND -> Color(0xFFFEF3C7)
            ThemeStyle.VELVET_PLUM -> Color(0xFFFCE7F3)
            ThemeStyle.SILVER_BIRCH -> Color(0xFFF1F5F9)
            ThemeStyle.MINT_MATCHA -> Color(0xFFD1FAE5)
        }
    }
}
