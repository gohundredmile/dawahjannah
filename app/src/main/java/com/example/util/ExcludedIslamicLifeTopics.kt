package com.example.util

/**
 * Filter helper to exclude unwanted topics/headings from the 'ইসলামী জীবন' tab.
 * Specifically removes:
 * 1. কুরআনী আমল ও রিযিক
 * 2. ঋণমুক্তি ও বরকত
 * 3. কঠিন বিপদের ইসমে আজম
 * 4. বুধবার যোহর ও আসরের মধ্যবর্তী দোয়া
 * 5. মনের আশা পুরণের উপায়
 * 6. শারীরিক সুস্থ্যতার দোয়া
 */
object ExcludedIslamicLifeTopics {

    private val exactTitles = setOf(
        "কুরআনী আমল ও রিযিক",
        "কুরআনি আমল ও রিযিক",
        "কুরআনি আমল ও রিজিক",
        "ঋণমুক্তি ও বরকত",
        "কঠিন বিপদের ইসমে আজম",
        "কঠিন বিপদের ইসমে আযম",
        "বুধবার যোহর ও আসরের মধ্যবর্তী দোয়া",
        "বুধবার যোহর ও আসরের মধ্যবর্তী দোয়া",
        "মনের আশা পূরণের উপায়",
        "মনের আশা পুরণের উপায়",
        "মনের আশা পূরণের উপায়",
        "মনের আশা পুরণের উপায়",
        "শারীরিক সুস্থতার দোয়া",
        "শারীরিক সুস্থ্যতার দোয়া",
        "শারীরিক সুস্থতার দোয়া",
        "শারীরিক সুস্থ্যতার দোয়া",
        "friday_special_duas",
        "শুক্রবারের বিশেষ দোয়া ও আমল",
        "শুক্রবারের বিশেষ দোয়া ও আমল",
        "★★★শুক্রবারের বিশেষ দোয়া ও আমল★★★",
        "★★★শুক্রবারের বিশেষ দোয়া ও আমল★★★"
    )

    fun isExcluded(titleOrCategory: String?): Boolean {
        if (titleOrCategory.isNullOrBlank()) return false
        val trimmed = titleOrCategory.trim()
        if (exactTitles.contains(trimmed)) return true

        // Normalized comparison without spaces, virama (্), and unified vowel signs
        val clean = trimmed
            .replace("\\s+".toRegex(), "")
            .replace("্", "")
            .replace("ূ", "ু")
            .replace("ী", "ি")
            .replace("য়", "য")
            .replace("জ", "য")

        // Check 1: কুরআনী আমল ও রিযিক
        if (clean.contains("কুরআন") && (clean.contains("রিযিক") || clean.contains("রিজিক"))) {
            return true
        }

        // Check 2: ঋণমুক্তি ও বরকত
        if (clean.contains("ঋণমুক্তি") && clean.contains("বরকত")) {
            return true
        }

        // Check 3: কঠিন বিপদের ইসমে আজম
        if (clean.contains("কঠিনবিপদ") && (clean.contains("আজম") || clean.contains("আযম"))) {
            return true
        }

        // Check 4: বুধবার যোহর ও আসরের মধ্যবর্তী দোয়া
        if (clean.contains("বুধবার") && (clean.contains("যোহর") || clean.contains("জোহর")) && clean.contains("আসর")) {
            return true
        }

        // Check 5: মনের আশা পুরণের উপায়
        if (clean.contains("মনেরআশা") && clean.contains("উপায")) {
            return true
        }

        // Check 6: শারীরিক সুস্থ্যতার দোয়া
        if (clean.contains("শারীরিক") && clean.contains("সুস্থ")) {
            return true
        }

        // Check 7: শুক্রবারের বিশেষ দোয়া ও আমল
        if (clean.contains("শুক্রবার") && (clean.contains("দোয") || clean.contains("আমল"))) {
            return true
        }

        return false
    }
}
