package com.example.data.model

data class MosqueInfo(
    val id: String = "masjid_default",
    val nameBn: String = "বায়তুল মুকাররম জাতীয় মসজিদ",
    val areaBn: String = "পল্টন, ঢাকা",
    val khatibBn: String = "খতীব ও পেশ ইমাম মহোদয়",
    val muazzinBn: String = "মুয়াজ্জিন সাহেব",
    val jumuahKhutbahTime: String = "১২:৪৫ PM",
    val jumuahJamatTime: String = "০১:১৫ PM",
    val capacity: String = "৪০,০০০+ মুসল্লি",
    val facilities: List<String> = listOf("এসি ব্যবস্থা", "বিশুদ্ধ অজুর স্থান", "মহিলাদের পৃথক নামাযের স্থান", "দ্বীনি লাইব্রেরি", "জেনারেটর ব্যাকআপ")
)

data class MosqueAnnouncement(
    val id: String,
    val titleBn: String,
    val categoryBn: String, // নোটিশ, মাহফিল, সংস্কার, সমাজসেবা, রমাদান
    val dateBn: String,
    val descriptionBn: String,
    val isUrgent: Boolean = false,
    val locationBn: String = "মসজিদ কমপ্লেক্স",
    val organizerBn: String = "মসজিদ পরিচালনা কমিটি"
)

data class MosqueClassItem(
    val id: String,
    val titleBn: String,
    val instructorBn: String,
    val daysOfWeekBn: String,
    val timeSlotBn: String,
    val targetAudienceBn: String, // সর্বস্তরের মুসল্লি, তরুণ ও শিক্ষার্থী, শিশু-কিশোর
    val descriptionBn: String,
    val keyTopicsBn: List<String>
)

data class MosqueCharityFund(
    val id: String,
    val titleBn: String,
    val categoryBn: String, // মসজিদ সংস্কার, এতিম ও দুস্থ তহবিল, ইফতার ফান্ড, ইমাম-মুয়াজ্জিন কল্যাণ
    val descriptionBn: String,
    val goalAmountTaka: Long,
    val raisedAmountTaka: Long,
    val donorCount: Int,
    val accountInfoBn: String = "বিকাশ/নগদ (মার্চেন্ট): ০১৭১১-XXXXXX"
)

enum class MosqueSalahStatus(val titleBn: String, val points: Int) {
    NOT_RECORDED("আদায় হয়নি", 0),
    INDIVIDUAL("একাকী আদায়", 1),
    JAMAT("জামা'আতে আদায়", 27),
    JAMAT_WITH_TAKBIR("তাকবীরে তাহরীমা সহ জামা'আত", 35)
}

data class MosqueSalahTrackItem(
    val waqtKey: String,
    val nameBn: String,
    val arabicName: String,
    val standardTimeBn: String,
    val jamatTimeBn: String,
    val status: MosqueSalahStatus = MosqueSalahStatus.NOT_RECORDED,
    val rakatsSummaryBn: String
)

data class MosqueSurahItem(
    val id: String,
    val nameBn: String,
    val nameAr: String,
    val surahNumber: Int,
    val totalAyahs: Int,
    val readingReasonBn: String, // যেমন: জুমু'আহর বিশেষ সুন্নাত, মাগরিব/ইশার পর, ঘুমানোর পূর্বে
    val virtueHadithBn: String,
    val verses: List<MosqueAyahSnippet>
)

data class MosqueAyahSnippet(
    val ayahNumber: Int,
    val arabicText: String,
    val pronunciationBn: String,
    val meaningBn: String
)
