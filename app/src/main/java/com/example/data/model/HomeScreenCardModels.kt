package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Representation of configurable cards displayed on the HomeScreen.
 */
enum class HomeScreenCardId(
    val id: String,
    val titleBn: String,
    val subtitleBn: String,
    val icon: ImageVector,
    val iconColor: Color,
    val defaultIndex: Int
) {
    HEADER_COVER(
        id = "header_cover",
        titleBn = "সালাত টাইমিং ও হেডার কভার",
        subtitleBn = "ওয়াক্ত কাউন্টডাউন, ক্যালেন্ডার, বর্তমান সালাত ও নিষিদ্ধ সময়",
        icon = Icons.Default.AccessTime,
        iconColor = Color(0xFF0284C7),
        defaultIndex = 0
    ),
    AMOL_TICKER(
        id = "amol_ticker",
        titleBn = "লাইভ আমল টিকার বার",
        subtitleBn = "সময় ও ওয়াক্তভিত্তিক তাৎক্ষণিক আমল ও জিকির নোটিফিকেশন",
        icon = Icons.Default.Schedule,
        iconColor = Color(0xFF059669),
        defaultIndex = 1
    ),
    FRIDAY_BANNER(
        id = "friday_banner",
        titleBn = "জুমার বিশেষ আমল ব্যানার",
        subtitleBn = "বৃহস্পতিবার মাগরিব থেকে শুক্রবার মাগরিব পর্যন্ত সক্রিয় বিশেষ ব্যানার",
        icon = Icons.Default.Star,
        iconColor = Color(0xFFD97706),
        defaultIndex = 2
    ),
    QUICK_ACTION(
        id = "quick_action",
        titleBn = "কুইক অ্যাকশন ও স্ট্রিক",
        subtitleBn = "ধারাবাহিকতা দিন স্ট্রিক, আজকের আমল ট্র্যাকার ও তাসবীহ শর্টকাট",
        icon = Icons.Default.LocalFireDepartment,
        iconColor = Color(0xFFEA580C),
        defaultIndex = 3
    ),
    CAMERA_AI(
        id = "camera_ai",
        titleBn = "Explain This Ayah ক্যামেরা",
        subtitleBn = "স্মার্ট ক্যামেরা দিয়ে কুরআনের আয়াত স্ক্যান, তফসির ও অডিও",
        icon = Icons.Default.PhotoCamera,
        iconColor = Color(0xFF10B981),
        defaultIndex = 4
    ),
    TOP_FEATURES(
        id = "top_features",
        titleBn = "টপ ফিচার সেকশন",
        subtitleBn = "সালাত, কুরআন, হাদিস, হিসনুল মুসলিমসহ শীর্ষ ৮টি ফিচারের গ্রিড ও 'আরও' বাটন",
        icon = Icons.Default.Dashboard,
        iconColor = Color(0xFF8B5CF6),
        defaultIndex = 5
    ),
    SEHRI_IFTAR(
        id = "sehri_iftar",
        titleBn = "সেহরি এবং ইফতারের সময়সূচী",
        subtitleBn = "দৈনিক সেহরি-ইফতার কাউন্টডাউন ও পূর্ণাঙ্গ রমজান সময়সূচী",
        icon = Icons.Default.NightsStay,
        iconColor = Color(0xFFE11D48),
        defaultIndex = 6
    ),
    SALAT_TIMINGS(
        id = "salat_timings",
        titleBn = "৫ ওয়াক্ত নামাজের সময়সূচী",
        subtitleBn = "সালাতের সঠিক ওয়াক্ত, নিষিদ্ধ সময়, জিপিএস ও স্থান সমন্বয়",
        icon = Icons.Default.Mosque,
        iconColor = Color(0xFF0284C7),
        defaultIndex = 7
    ),
    NOFOL_SALAT(
        id = "nofol_salat",
        titleBn = "নফল সালাতের সময়সূচী",
        subtitleBn = "তাহাজ্জুদ, ইশরাক, চাশত, আওয়াবীনসহ ৮টি নফল সালাতের সময় ও ফযিলত",
        icon = Icons.Default.SelfImprovement,
        iconColor = Color(0xFF6366F1),
        defaultIndex = 8
    ),
    DAILY_WISDOM(
        id = "daily_wisdom",
        titleBn = "আজকের হেদায়েত ও অনুপ্রেরণা",
        subtitleBn = "প্রতিদিনের পবিত্র কুরআন আয়াত, সহীহ হাদিস ও ইসলামিক বাণী",
        icon = Icons.Default.AutoAwesome,
        iconColor = Color(0xFFF59E0B),
        defaultIndex = 9
    );

    companion object {
        val defaultList: List<HomeScreenCardId> = entries.sortedBy { it.defaultIndex }

        fun parseOrder(csv: String?): List<HomeScreenCardId> {
            if (csv.isNullOrBlank()) return defaultList
            val ids = csv.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            val idMap = entries.associateBy { it.id }
            val ordered = ids.mapNotNull { idMap[it] }.distinct().toMutableList()
            // Add any missing cards that weren't in the saved CSV (e.g. newly added cards)
            defaultList.forEach { card ->
                if (!ordered.contains(card)) {
                    ordered.add(card)
                }
            }
            return ordered
        }

        fun toCsv(list: List<HomeScreenCardId>): String {
            return list.joinToString(",") { it.id }
        }
    }
}
