package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.datasource.salah.SalahGuideCatalog
import com.example.data.model.salah.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SalahSearchResult(
    val id: String,
    val titleBn: String,
    val subtitleBn: String,
    val snippetBn: String,
    val targetTab: SalahSectionTab
)

class SalahGuideRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("salah_guide_prefs", Context.MODE_PRIVATE)

    // Bookmarks
    private val _bookmarkedIds = MutableStateFlow<Set<String>>(emptySet())
    val bookmarkedIds: StateFlow<Set<String>> = _bookmarkedIds.asStateFlow()

    // Completed Lessons
    private val _completedLessonIds = MutableStateFlow<Set<String>>(emptySet())
    val completedLessonIds: StateFlow<Set<String>> = _completedLessonIds.asStateFlow()

    // Memorization Status map: recitationId -> MemorizeStatus
    private val _memorizeStatusMap = MutableStateFlow<Map<String, MemorizeStatus>>(emptyMap())
    val memorizeStatusMap: StateFlow<Map<String, MemorizeStatus>> = _memorizeStatusMap.asStateFlow()

    // Last viewed lesson / position for "Continue Learning"
    private val _lastViewedTitle = MutableStateFlow<String>("")
    val lastViewedTitle: StateFlow<String> = _lastViewedTitle.asStateFlow()

    private val _lastViewedTab = MutableStateFlow<SalahSectionTab>(SalahSectionTab.OVERVIEW)
    val lastViewedTab: StateFlow<SalahSectionTab> = _lastViewedTab.asStateFlow()

    // Fiqh Methodology
    private val _fiqhMethodology = MutableStateFlow<SalahFiqhMethodology>(SalahFiqhMethodology.HANAFI)
    val fiqhMethodology: StateFlow<SalahFiqhMethodology> = _fiqhMethodology.asStateFlow()

    init {
        loadPersistedData()
    }

    private fun loadPersistedData() {
        val bookmarks = prefs.getStringSet("bookmarked_ids", emptySet()) ?: emptySet()
        _bookmarkedIds.value = bookmarks

        val completed = prefs.getStringSet("completed_lessons", emptySet()) ?: emptySet()
        _completedLessonIds.value = completed

        val lastTitle = prefs.getString("last_viewed_title", "নামাজ কেন পড়ব এবং এর গুরুত্ব") ?: "নামাজ কেন পড়ব এবং এর গুরুত্ব"
        _lastViewedTitle.value = lastTitle

        val lastTabName = prefs.getString("last_viewed_tab", SalahSectionTab.OVERVIEW.name) ?: SalahSectionTab.OVERVIEW.name
        _lastViewedTab.value = try {
            SalahSectionTab.valueOf(lastTabName)
        } catch (_: Exception) {
            SalahSectionTab.OVERVIEW
        }

        val fiqhId = prefs.getString("fiqh_methodology", SalahFiqhMethodology.HANAFI.id) ?: SalahFiqhMethodology.HANAFI.id
        _fiqhMethodology.value = if (fiqhId == SalahFiqhMethodology.SHAFI_OR_GENERAL.id) {
            SalahFiqhMethodology.SHAFI_OR_GENERAL
        } else {
            SalahFiqhMethodology.HANAFI
        }

        val statusMap = mutableMapOf<String, MemorizeStatus>()
        SalahGuideCatalog.recitationsLibrary.forEach { item ->
            val statusStr = prefs.getString("mem_status_${item.id}", null)
            if (statusStr != null) {
                try {
                    statusMap[item.id] = MemorizeStatus.valueOf(statusStr)
                } catch (_: Exception) {
                    statusMap[item.id] = MemorizeStatus.NEW
                }
            } else {
                statusMap[item.id] = MemorizeStatus.NEW
            }
        }
        _memorizeStatusMap.value = statusMap
    }

    fun toggleBookmark(id: String) {
        val current = _bookmarkedIds.value.toMutableSet()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        _bookmarkedIds.value = current
        prefs.edit().putStringSet("bookmarked_ids", current).apply()
    }

    fun isBookmarked(id: String): Boolean {
        return _bookmarkedIds.value.contains(id)
    }

    fun toggleLessonCompleted(lessonId: String) {
        val current = _completedLessonIds.value.toMutableSet()
        if (current.contains(lessonId)) {
            current.remove(lessonId)
        } else {
            current.add(lessonId)
        }
        _completedLessonIds.value = current
        prefs.edit().putStringSet("completed_lessons", current).apply()
    }

    fun setMemorizeStatus(recitationId: String, status: MemorizeStatus) {
        val current = _memorizeStatusMap.value.toMutableMap()
        current[recitationId] = status
        _memorizeStatusMap.value = current
        prefs.edit().putString("mem_status_$recitationId", status.name).apply()
    }

    fun recordLastViewed(title: String, tab: SalahSectionTab) {
        _lastViewedTitle.value = title
        _lastViewedTab.value = tab
        prefs.edit()
            .putString("last_viewed_title", title)
            .putString("last_viewed_tab", tab.name)
            .apply()
    }

    fun setFiqhMethodology(methodology: SalahFiqhMethodology) {
        _fiqhMethodology.value = methodology
        prefs.edit().putString("fiqh_methodology", methodology.id).apply()
    }

    fun getOverallProgressFraction(): Float {
        val totalLessons = SalahGuideCatalog.beginnerLessons.size
        if (totalLessons == 0) return 0f
        val completed = _completedLessonIds.value.size
        return (completed.toFloat() / totalLessons.toFloat()).coerceIn(0f, 1f)
    }

    fun search(query: String): List<SalahSearchResult> {
        val q = query.trim().lowercase()
        if (q.isBlank()) return emptyList()

        val results = mutableListOf<SalahSearchResult>()

        // 1. Beginner Lessons
        SalahGuideCatalog.beginnerLessons.forEach { lesson ->
            if (lesson.titleBn.lowercase().contains(q) ||
                lesson.subtitleBn.lowercase().contains(q) ||
                lesson.detailedContentBn.lowercase().contains(q)
            ) {
                results.add(
                    SalahSearchResult(
                        id = lesson.id,
                        titleBn = lesson.titleBn,
                        subtitleBn = "পাঠ ${lesson.serialNumberBn} • শিক্ষণ",
                        snippetBn = lesson.summaryBn,
                        targetTab = lesson.targetTab ?: SalahSectionTab.OVERVIEW
                    )
                )
            }
        }

        // 2. Daily Prayers
        SalahGuideCatalog.dailyPrayers.forEach { prayer ->
            if (prayer.nameBn.lowercase().contains(q) ||
                prayer.nameAr.contains(q) ||
                prayer.breakdownBn.lowercase().contains(q) ||
                prayer.sequenceGuideBn.lowercase().contains(q)
            ) {
                results.add(
                    SalahSearchResult(
                        id = prayer.id,
                        titleBn = "${prayer.nameBn} নামাজ (${prayer.totalRakatsBn})",
                        subtitleBn = prayer.breakdownBn,
                        snippetBn = prayer.timeDescriptionBn,
                        targetTab = SalahSectionTab.DAILY_PRAYERS
                    )
                )
            }
        }

        // 3. Steps
        SalahGuideCatalog.stepByStepGuide.forEach { step ->
            if (step.titleBn.lowercase().contains(q) ||
                step.actionInstructionBn.lowercase().contains(q) ||
                (step.transliterationBn?.lowercase()?.contains(q) == true) ||
                (step.meaningBn?.lowercase()?.contains(q) == true)
            ) {
                results.add(
                    SalahSearchResult(
                        id = "step_${step.stepNumber}",
                        titleBn = step.titleBn,
                        subtitleBn = "ধাপে ধাপে নামাজ • ${step.posture.titleBn}",
                        snippetBn = step.actionInstructionBn,
                        targetTab = SalahSectionTab.STEP_BY_STEP
                    )
                )
            }
        }

        // 4. Recitations
        SalahGuideCatalog.recitationsLibrary.forEach { recitation ->
            if (recitation.titleBn.lowercase().contains(q) ||
                recitation.transliterationBn.lowercase().contains(q) ||
                recitation.translationBn.lowercase().contains(q) ||
                recitation.arabicText.contains(q)
            ) {
                results.add(
                    SalahSearchResult(
                        id = recitation.id,
                        titleBn = recitation.titleBn,
                        subtitleBn = recitation.placeInSalahBn,
                        snippetBn = recitation.translationBn,
                        targetTab = SalahSectionTab.RECITATIONS
                    )
                )
            }
        }

        // 5. Preparation
        SalahGuideCatalog.preparationGuides.forEach { prep ->
            if (prep.titleBn.lowercase().contains(q) ||
                prep.category.lowercase().contains(q) ||
                prep.stepsOrRules.any { it.lowercase().contains(q) }
            ) {
                results.add(
                    SalahSearchResult(
                        id = prep.id,
                        titleBn = prep.titleBn,
                        subtitleBn = "পবিত্রতা ও প্রস্তুতি • ${prep.category}",
                        snippetBn = prep.subtitleBn,
                        targetTab = SalahSectionTab.PREPARATION
                    )
                )
            }
        }

        // 6. Conditions & Rules
        SalahGuideCatalog.conditionsAndRules.forEach { cond ->
            if (cond.titleBn.lowercase().contains(q) ||
                cond.ruleDescriptionBn.lowercase().contains(q) ||
                cond.categoryBn.lowercase().contains(q)
            ) {
                results.add(
                    SalahSearchResult(
                        id = cond.id,
                        titleBn = cond.titleBn,
                        subtitleBn = cond.categoryBn,
                        snippetBn = cond.ruleDescriptionBn,
                        targetTab = SalahSectionTab.CONDITIONS
                    )
                )
            }
        }

        // 7. Mistakes & Sahw
        SalahGuideCatalog.commonMistakes.forEach { mist ->
            if (mist.mistakeTitleBn.lowercase().contains(q) ||
                mist.whyProblematicBn.lowercase().contains(q) ||
                mist.howToCorrectBn.lowercase().contains(q)
            ) {
                results.add(
                    SalahSearchResult(
                        id = mist.id,
                        titleBn = mist.mistakeTitleBn,
                        subtitleBn = "ভুল সংশোধন • ${mist.categoryBn}",
                        snippetBn = mist.howToCorrectBn,
                        targetTab = SalahSectionTab.MISTAKES
                    )
                )
            }
        }

        // 8. Special Prayers
        SalahGuideCatalog.specialPrayers.forEach { special ->
            if (special.nameBn.lowercase().contains(q) ||
                special.nameAr.contains(q) ||
                special.importanceAndVirtueBn.lowercase().contains(q) ||
                special.completeMethodBn.lowercase().contains(q)
            ) {
                results.add(
                    SalahSearchResult(
                        id = special.id,
                        titleBn = special.nameBn,
                        subtitleBn = "বিশেষ নামাজ • ${special.rakatsSummaryBn}",
                        snippetBn = special.timingBn,
                        targetTab = SalahSectionTab.SPECIAL_PRAYERS
                    )
                )
            }
        }

        // 9. Rulings & Masail
        SalahGuideCatalog.salahRulings.forEach { rule ->
            if (rule.questionOrTopicBn.lowercase().contains(q) ||
                rule.directRulingBn.lowercase().contains(q) ||
                rule.detailedExplanationBn.lowercase().contains(q)
            ) {
                results.add(
                    SalahSearchResult(
                        id = rule.id,
                        titleBn = rule.questionOrTopicBn,
                        subtitleBn = "মাসআলা • ${rule.topicCategoryBn}",
                        snippetBn = rule.directRulingBn,
                        targetTab = SalahSectionTab.RULINGS
                    )
                )
            }
        }

        // 10. FAQs
        SalahGuideCatalog.salahFaqs.forEach { faq ->
            if (faq.questionBn.lowercase().contains(q) ||
                faq.answerBn.lowercase().contains(q)
            ) {
                results.add(
                    SalahSearchResult(
                        id = faq.id,
                        titleBn = faq.questionBn,
                        subtitleBn = "সাধারণ প্রশ্নোত্তর (FAQ)",
                        snippetBn = faq.answerBn,
                        targetTab = faq.relatedTopicTab
                    )
                )
            }
        }

        return results
    }
}
