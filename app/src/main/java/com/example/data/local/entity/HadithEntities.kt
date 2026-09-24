package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "hadith_books")
data class HadithBookEntity(
    @PrimaryKey
    val slug: String,                   // e.g. "bukhari", "muslim", "tirmidhi"
    val nameBn: String,                 // e.g. "সহীহ বুখারী"
    val nameAr: String,                 // e.g. "صحيح البخاري"
    val nameEn: String,                 // e.g. "Sahih al-Bukhari"
    val authorBn: String,               // e.g. "ইমাম মুহাম্মদ বিন ইসমাইল আল-বুখারী (রহ.)"
    val totalHadiths: Int,              // e.g. 7563
    val totalChapters: Int,             // e.g. 97
    val descriptionBn: String,          // e.g. "ইসলামের বিশুদ্ধতম হাদিস গ্রন্থ..."
    val isSihahSitta: Boolean,          // true if part of Kutub al-Sittah
    val orderIndex: Int,                // Display order
    val colorHex: String = "#0A5C36"    // Theme accent color
)

@Entity(
    tableName = "hadith_chapters",
    indices = [Index(value = ["bookSlug", "chapterNumber"])]
)
data class HadithChapterEntity(
    @PrimaryKey
    val id: String,                     // e.g. "bukhari_1"
    val bookSlug: String,
    val chapterNumber: Int,
    val titleBn: String,
    val titleAr: String,
    val hadithRange: String
)

@Entity(
    tableName = "hadith_items",
    indices = [
        Index(value = ["bookSlug", "hadithNumber"]),
        Index(value = ["bookSlug", "chapterNumber"])
    ]
)
data class HadithEntity(
    @PrimaryKey
    val id: String,                     // e.g. "bukhari_1"
    val bookSlug: String,
    val hadithNumber: Int,
    val chapterNumber: Int,
    val chapterTitleBn: String,
    val narratorBn: String,             // e.g. "হযরত উমর ইবনুল খাত্তাব (রা.)"
    val arabicText: String,             // Full Arabic text with Harakat
    val banglaText: String,             // Authentic HadithBD / IRD Bengali translation
    val englishText: String = "",
    val gradeBn: String,                // e.g. "সহীহ বুখারী", "সহীহ", "হাসান"
    val gradeColor: String = "SAHIH",   // "SAHIH", "HASAN", "DAIF"
    val sourceBn: String = "হাদিসবিডি (HadithBD) / IRD ফাউন্ডেশন",
    val explanationBn: String = "",     // Hadith takeaways, context & Fiqh note
    val hadithTypeBn: String = "কওলী (বাণী)", // কওলী, ফে'লী, তাকরীরী, কুদসী
    val isBookmarked: Boolean = false
)

@Entity(tableName = "hadith_bookmarks")
data class HadithBookmarkEntity(
    @PrimaryKey
    val hadithId: String,
    val bookSlug: String,
    val hadithNumber: Int,
    val timestamp: Long = System.currentTimeMillis()
)
