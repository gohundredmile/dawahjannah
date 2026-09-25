package com.example.data.repository

import android.content.Context
import com.example.data.datasource.QuranActionEngineCatalog
import com.example.data.model.ActionHistoryRecord
import com.example.data.model.ActionMode
import com.example.data.model.ActionPlanDuration
import com.example.data.model.AyahActionInsight
import com.example.data.model.EveningReviewRecord
import com.example.data.model.LifeSphere
import com.example.data.model.ReviewOutcome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class QuranActionEngineRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("quran_action_engine_prefs", Context.MODE_PRIVATE)
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _history = MutableStateFlow<List<ActionHistoryRecord>>(emptyList())
    val history: StateFlow<List<ActionHistoryRecord>> = _history.asStateFlow()

    private val _activeCommitment = MutableStateFlow<ActionHistoryRecord?>(null)
    val activeCommitment: StateFlow<ActionHistoryRecord?> = _activeCommitment.asStateFlow()

    private val _bookmarkedAyahIds = MutableStateFlow<Set<String>>(emptySet())
    val bookmarkedAyahIds: StateFlow<Set<String>> = _bookmarkedAyahIds.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        try {
            val jsonStr = prefs.getString("action_records", "[]") ?: "[]"
            val array = JSONArray(jsonStr)
            val list = mutableListOf<ActionHistoryRecord>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(parseRecord(obj))
            }
            _history.value = list.sortedByDescending { it.createdAt }

            // Find current active commitment (not completed or today)
            _activeCommitment.value = list.firstOrNull { !it.isCompleted }

            val bmJson = prefs.getString("bookmarked_ayah_ids", "[]") ?: "[]"
            val bmArray = JSONArray(bmJson)
            val bmSet = mutableSetOf<String>()
            for (i in 0 until bmArray.length()) {
                bmSet.add(bmArray.getString(i))
            }
            _bookmarkedAyahIds.value = bmSet
        } catch (_: Exception) {
            _history.value = emptyList()
            _activeCommitment.value = null
        }
    }

    private fun saveData() {
        try {
            val array = JSONArray()
            _history.value.forEach { record ->
                array.put(recordToJson(record))
            }
            prefs.edit().putString("action_records", array.toString()).apply()

            val bmArray = JSONArray()
            _bookmarkedAyahIds.value.forEach { bmArray.put(it) }
            prefs.edit().putString("bookmarked_ayah_ids", bmArray.toString()).apply()
        } catch (_: Exception) {
        }
    }

    fun commitAction(
        insight: AyahActionInsight,
        selectedSphere: LifeSphere,
        selectedMode: ActionMode,
        planDuration: ActionPlanDuration,
        actionText: String,
        userPersonalNote: String = "",
        reflectionAnswers: Map<String, String> = emptyMap()
    ): ActionHistoryRecord {
        val record = ActionHistoryRecord(
            id = "act_" + UUID.randomUUID().toString().take(8),
            createdAt = System.currentTimeMillis(),
            ayahId = insight.ayahId,
            surahNumber = insight.surahNumber,
            ayahNumber = insight.ayahNumber,
            surahNameBangla = insight.surahNameBangla,
            arabicShort = insight.arabicText.take(45) + if (insight.arabicText.length > 45) "..." else "",
            translationShort = insight.banglaTranslation.take(65) + if (insight.banglaTranslation.length > 65) "..." else "",
            selectedSphere = selectedSphere,
            selectedMode = selectedMode,
            planDuration = planDuration,
            actionText = actionText,
            userPersonalNote = userPersonalNote,
            userReflectionAnswers = reflectionAnswers,
            isCompleted = false,
            themes = insight.primaryThemes
        )

        val updated = listOf(record) + _history.value
        _history.value = updated
        _activeCommitment.value = record
        saveData()
        return record
    }

    fun completeAction(recordId: String, isDone: Boolean = true) {
        val current = _history.value
        val updated = current.map { rec ->
            if (rec.id == recordId) {
                rec.copy(
                    isCompleted = isDone,
                    completedAt = if (isDone) System.currentTimeMillis() else null
                )
            } else rec
        }
        _history.value = updated
        _activeCommitment.value = updated.firstOrNull { !it.isCompleted }
        saveData()
    }

    fun submitEveningReview(
        recordId: String,
        outcome: ReviewOutcome,
        whatDidYouNotice: String,
        didItChangeHandling: String,
        growthTakeaway: String
    ) {
        val review = EveningReviewRecord(
            reviewedAt = System.currentTimeMillis(),
            outcome = outcome,
            whatDidYouNotice = whatDidYouNotice,
            didItChangeHandling = didItChangeHandling,
            growthTakeaway = growthTakeaway
        )

        val current = _history.value
        val updated = current.map { rec ->
            if (rec.id == recordId) {
                rec.copy(
                    eveningReview = review,
                    isCompleted = outcome == ReviewOutcome.DID_IT || outcome == ReviewOutcome.PARTIALLY,
                    completedAt = System.currentTimeMillis()
                )
            } else rec
        }
        _history.value = updated
        _activeCommitment.value = updated.firstOrNull { !it.isCompleted }
        saveData()
    }

    fun toggleBookmark(ayahId: String) {
        val current = _bookmarkedAyahIds.value.toMutableSet()
        if (current.contains(ayahId)) {
            current.remove(ayahId)
        } else {
            current.add(ayahId)
        }
        _bookmarkedAyahIds.value = current
        saveData()
    }

    /**
     * Continuity helper: checks the most recent reflection or action to suggest a bridging thought.
     */
    fun getRecentContinuityInsight(): Pair<String, String>? {
        val recent = _history.value.firstOrNull() ?: return null
        val recentTheme = recent.themes.firstOrNull() ?: recent.selectedSphere.titleBn
        val headline = "পূর্ববর্তী আমল: '${recent.surahNameBangla}' থেকে $recentTheme"
        val message = if (recent.eveningReview != null) {
            "গত সন্ধ্যায় আপনি পর্যালোচনা করেছেন: '${recent.eveningReview.growthTakeaway.ifBlank { "তাদাব্বুর ও আমলের অনুভূতি" }}'। আজকের আয়াতেও সেই ধারাবাহিকতা রক্ষা করুন।"
        } else {
            "আপনি '${recent.actionText.take(35)}...' আমলটি বেছে নিয়েছিলেন। আজকের পাঠ্য আপনাকে সেই উপলব্ধির গভীরে নিয়ে যাবে।"
        }
        return Pair(headline, message)
    }

    /**
     * Aggregates spiritual themes encountered into a qualitative summary (NO competitive numerical scores).
     */
    fun getEncounteredThemesMap(): Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        _history.value.forEach { rec ->
            rec.themes.forEach { theme ->
                map[theme] = (map[theme] ?: 0) + 1
            }
        }
        return map
    }

    private fun parseRecord(obj: JSONObject): ActionHistoryRecord {
        val answersMap = mutableMapOf<String, String>()
        val answersObj = obj.optJSONObject("answers")
        answersObj?.keys()?.forEach { k ->
            answersMap[k] = answersObj.optString(k)
        }

        val themesList = mutableListOf<String>()
        val themesArr = obj.optJSONArray("themes")
        if (themesArr != null) {
            for (i in 0 until themesArr.length()) {
                themesList.add(themesArr.getString(i))
            }
        }

        var eveningReview: EveningReviewRecord? = null
        val revObj = obj.optJSONObject("eveningReview")
        if (revObj != null) {
            eveningReview = EveningReviewRecord(
                reviewedAt = revObj.optLong("reviewedAt", System.currentTimeMillis()),
                outcome = ReviewOutcome.valueOf(revObj.optString("outcome", ReviewOutcome.DID_IT.name)),
                whatDidYouNotice = revObj.optString("whatDidYouNotice", ""),
                didItChangeHandling = revObj.optString("didItChangeHandling", ""),
                growthTakeaway = revObj.optString("growthTakeaway", "")
            )
        }

        return ActionHistoryRecord(
            id = obj.optString("id", UUID.randomUUID().toString()),
            createdAt = obj.optLong("createdAt", System.currentTimeMillis()),
            ayahId = obj.optString("ayahId", ""),
            surahNumber = obj.optInt("surahNumber", 1),
            ayahNumber = obj.optInt("ayahNumber", 1),
            surahNameBangla = obj.optString("surahNameBangla", ""),
            arabicShort = obj.optString("arabicShort", ""),
            translationShort = obj.optString("translationShort", ""),
            selectedSphere = LifeSphere.fromId(obj.optString("selectedSphere", "mindset")),
            selectedMode = ActionMode.fromId(obj.optString("selectedMode", "practice")),
            planDuration = ActionPlanDuration.fromId(obj.optString("planDuration", "today")),
            actionText = obj.optString("actionText", ""),
            userPersonalNote = obj.optString("userPersonalNote", ""),
            userReflectionAnswers = answersMap,
            isCompleted = obj.optBoolean("isCompleted", false),
            completedAt = if (obj.has("completedAt")) obj.optLong("completedAt") else null,
            eveningReview = eveningReview,
            themes = themesList
        )
    }

    private fun recordToJson(record: ActionHistoryRecord): JSONObject {
        val obj = JSONObject()
        obj.put("id", record.id)
        obj.put("createdAt", record.createdAt)
        obj.put("ayahId", record.ayahId)
        obj.put("surahNumber", record.surahNumber)
        obj.put("ayahNumber", record.ayahNumber)
        obj.put("surahNameBangla", record.surahNameBangla)
        obj.put("arabicShort", record.arabicShort)
        obj.put("translationShort", record.translationShort)
        obj.put("selectedSphere", record.selectedSphere.id)
        obj.put("selectedMode", record.selectedMode.id)
        obj.put("planDuration", record.planDuration.id)
        obj.put("actionText", record.actionText)
        obj.put("userPersonalNote", record.userPersonalNote)
        obj.put("isCompleted", record.isCompleted)
        if (record.completedAt != null) {
            obj.put("completedAt", record.completedAt)
        }

        val answersObj = JSONObject()
        record.userReflectionAnswers.forEach { (k, v) -> answersObj.put(k, v) }
        obj.put("answers", answersObj)

        val themesArr = JSONArray()
        record.themes.forEach { themesArr.put(it) }
        obj.put("themes", themesArr)

        if (record.eveningReview != null) {
            val revObj = JSONObject()
            revObj.put("reviewedAt", record.eveningReview.reviewedAt)
            revObj.put("outcome", record.eveningReview.outcome.name)
            revObj.put("whatDidYouNotice", record.eveningReview.whatDidYouNotice)
            revObj.put("didItChangeHandling", record.eveningReview.didItChangeHandling)
            revObj.put("growthTakeaway", record.eveningReview.growthTakeaway)
            obj.put("eveningReview", revObj)
        }

        return obj
    }
}
