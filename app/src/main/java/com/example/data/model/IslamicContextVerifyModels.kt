package com.example.data.model

import androidx.compose.ui.graphics.Color

/**
 * Islamic Context & Verify: Core data models for analyzing, verifying,
 * and contextualizing Islamic claims encountered in WhatsApp messages,
 * social media posts, videos, articles, and everyday discussions.
 */

enum class ClaimVerificationVerdict(
    val iconEmoji: String,
    val titleBn: String,
    val titleEn: String,
    val descriptionBn: String,
    val primaryColor: Color,
    val containerColor: Color
) {
    ESTABLISHED(
        iconEmoji = "✅",
        titleBn = "সুপ্রতিষ্ঠিত ও প্রমাণিত",
        titleEn = "Established",
        descriptionBn = "দাবিটি কুরআন ও নির্ভরযোগ্য সহীহ হাদিস দ্বারা সুদৃঢ়ভাবে প্রমাণিত।",
        primaryColor = Color(0xFF059669),
        containerColor = Color(0xFFE8F5E9)
    ),
    CONTEXT_NEEDED(
        iconEmoji = "⚠️",
        titleBn = "প্রেক্ষাপট জানা আবশ্যক",
        titleEn = "Context needed",
        descriptionBn = "উদ্ধৃতি বা দলিলের সত্যতা থাকলেও প্রেক্ষাপট বিচ্ছিন্ন বা নির্দিষ্ট অবস্থাকে সার্বজনীন হিসেবে উপস্থাপন করা হয়েছে।",
        primaryColor = Color(0xFFD97706),
        containerColor = Color(0xFFFFFBEB)
    ),
    SCHOLARLY_DISAGREEMENT(
        iconEmoji = "⚖️",
        titleBn = "আইনজ্ঞ ও আলেমগণের মতভেদ",
        titleEn = "Scholarly disagreement",
        descriptionBn = "বিষয়টিতে বিশ্বস্ত মুজতাহিদ ফকীহগণের মধ্যে বৈধ ইজতিহাদী মতপার্থক্য রয়েছে; একক মতকে একমাত্র সত্য বলা যায় না।",
        primaryColor = Color(0xFF2563EB),
        containerColor = Color(0xFFEFF6FF)
    ),
    INSUFFICIENT_EVIDENCE(
        iconEmoji = "❓",
        titleBn = "পর্যাপ্ত প্রমাণের ঘাটতি",
        titleEn = "Insufficient evidence",
        descriptionBn = "দাবিটির পক্ষে কোনো স্পষ্ট প্রামাণ্য আয়াত বা নির্ভরযোগ্য সহীহ দলীল পাওয়া যায়নি।",
        primaryColor = Color(0xFF7C3AED),
        containerColor = Color(0xFFF5F3FF)
    ),
    MISQUOTED_OR_UNSUPPORTED(
        iconEmoji = "❌",
        titleBn = "বানোয়াট / বিকৃত / সূত্রহীন",
        titleEn = "Misquoted / unsupported",
        descriptionBn = "দাবিটি সম্পূর্ণ ভিত্তিহীন, রাসূলুল্লাহ ﷺ-এর নামে মিথ্যা বা কোনো মনগড়া বর্ণনার ওপর প্রতিষ্ঠিত।",
        primaryColor = Color(0xFFDC2626),
        containerColor = Color(0xFFFEF2F2)
    ),
    MORE_EVIDENCE_NEEDED(
        iconEmoji = "🔎",
        titleBn = "অধিকতর তথ্য প্রয়োজন",
        titleEn = "More evidence needed",
        descriptionBn = "প্রদত্ত বার্তাটি অসম্পূর্ণ বা অস্পষ্ট হওয়ায় নিশ্চিত শরঈ মূল্যায়নের জন্য মূল সোর্স বা আরও তথ্যের প্রয়োজন।",
        primaryColor = Color(0xFF0891B2),
        containerColor = Color(0xFFF0FDF4)
    );

    companion object {
        fun fromString(value: String): ClaimVerificationVerdict {
            val normalized = value.trim().uppercase()
            return when {
                normalized.contains("ESTABLISHED") -> ESTABLISHED
                normalized.contains("CONTEXT") -> CONTEXT_NEEDED
                normalized.contains("DISAGREEMENT") || normalized.contains("SCHOLAR") -> SCHOLARLY_DISAGREEMENT
                normalized.contains("INSUFFICIENT") -> INSUFFICIENT_EVIDENCE
                normalized.contains("MISQUOTED") || normalized.contains("UNSUPPORTED") || normalized.contains("FABRICATED") -> MISQUOTED_OR_UNSUPPORTED
                else -> MORE_EVIDENCE_NEEDED
            }
        }
    }
}

enum class ConfidenceLevel(val labelBn: String, val labelEn: String, val color: Color) {
    HIGH("উচ্চ প্রামাণিকতা", "High", Color(0xFF059669)),
    MODERATE("মাঝারি প্রামাণিকতা", "Moderate", Color(0xFFD97706)),
    LIMITED("সীমিত প্রামাণিকতা", "Limited", Color(0xFF64748B));

    companion object {
        fun fromString(value: String): ConfidenceLevel {
            val normalized = value.trim().uppercase()
            return when {
                normalized.contains("HIGH") -> HIGH
                normalized.contains("MODERATE") -> MODERATE
                else -> LIMITED
            }
        }
    }
}

data class QuranReferenceItem(
    val surahName: String = "",
    val surahNumber: Int = 0,
    val ayahNumber: Int = 0,
    val arabicText: String = "",
    val translationBn: String = "",
    val whatVerseActuallyAddresses: String = "",
    val isDirectEvidence: Boolean = true // true: direct textual evidence, false: interpretive
)

data class HadithReferenceItem(
    val collection: String = "", // e.g. সহীহ বুখারী, সহীহ মুসলিম, সুনান আবু দাউদ, জামে তিরমিজি
    val hadithNumber: String = "",
    val arabicText: String = "",
    val translationBn: String = "",
    val authenticityGrade: String = "", // e.g. সহীহ (Sahih), হাসান (Hasan), যয়ীফ (Da'if), মাওযূ / জাল (Fabricated)
    val textVsInterpretation: String = "" // Distinguishing actual wording from subsequent inferences
)

data class ClaimScholarlyOpinion(
    val schoolOrScholar: String = "", // e.g. হানাফী মাযহাব, শাফেয়ী মাযহাব, মালেকী মাযহাব, হাম্বলী মাযহাব, জমহুর
    val positionSummary: String = "",
    val textualBasis: String = ""
)

data class MisinformationWarning(
    val warningType: String = "", // e.g. Fabricated Hadith, Context Removed, Selective Quoting, Wrong Verse Number
    val warningDetail: String = ""
)

data class WhatIsActuallyEstablished(
    val explicitTextualFact: String = "",    // What the primary text literally and explicitly commands/prohibits
    val scholarlyInference: String = "",     // What scholars have derived through Ijtihad/interpretation
    val uncertainOrUnverified: String = ""   // What is speculative, disputed, or unsupported
)

data class IslamicContextVerifyReport(
    val id: String = java.util.UUID.randomUUID().toString(),
    val originalClaim: String = "",
    val sourcePlatformTag: String = "WhatsApp বার্তা",
    val individualClaims: List<String> = emptyList(),
    val verdict: ClaimVerificationVerdict = ClaimVerificationVerdict.CONTEXT_NEEDED,
    val confidence: ConfidenceLevel = ConfidenceLevel.HIGH,
    val summaryHeadlineBn: String = "",
    val evidenceSummaryBn: String = "",
    val quranReferences: List<QuranReferenceItem> = emptyList(),
    val hadithReferences: List<HadithReferenceItem> = emptyList(),
    val historicalContextBn: String = "",
    val linguisticContextBn: String = "",
    val audienceAndScopeBn: String = "", // Who was addressed, general vs situation-specific
    val omittedSurroundingsBn: String = "", // Surrounding verses or missing context
    val scholarlyInterpretations: List<ClaimScholarlyOpinion> = emptyList(),
    val potentialMisinformation: List<MisinformationWarning> = emptyList(),
    val whatIsActuallyEstablished: WhatIsActuallyEstablished = WhatIsActuallyEstablished(),
    val actionableConclusionBn: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isSaved: Boolean = false
)
