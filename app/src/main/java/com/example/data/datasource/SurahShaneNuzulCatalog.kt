package com.example.data.datasource

import android.content.Context
import com.example.data.model.SurahShaneNuzul
import org.json.JSONArray
import java.io.InputStreamReader

/**
 * Catalog providing authentic Shan-e-Nuzul (শানে নযুল), naming origins,
 * revelation background, core themes, and authentic virtues for all 114 Surahs
 * based on HadithBD, Tafsir Ibn Kathir, Dr. Abu Bakr Muhammad Zakaria, and Ahsanul Bayan.
 */
object SurahShaneNuzulCatalog {

    @Volatile
    private var cachedMap: Map<Int, SurahShaneNuzul>? = null

    fun getShaneNuzul(context: Context, surahNumber: Int): SurahShaneNuzul? {
        if (cachedMap == null) {
            loadFromAssets(context)
        }
        return cachedMap?.get(surahNumber)
    }

    private synchronized fun loadFromAssets(context: Context) {
        if (cachedMap != null) return
        try {
            val jsonString = context.assets.open("surah_shane_nuzul.json").use { inputStream ->
                InputStreamReader(inputStream, Charsets.UTF_8).readText()
            }
            val jsonArray = JSONArray(jsonString)
            val map = HashMap<Int, SurahShaneNuzul>(120)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val surahNumber = obj.getInt("surahNumber")
                val item = SurahShaneNuzul(
                    surahNumber = surahNumber,
                    surahNameBn = obj.optString("surahNameBn"),
                    naming = obj.optString("naming"),
                    period = obj.optString("period"),
                    shaneNuzul = obj.optString("shaneNuzul"),
                    themes = obj.optString("themes"),
                    virtues = obj.optString("virtues"),
                    tafsirPerspectives = obj.optString("tafsirPerspectives")
                )
                map[surahNumber] = item
            }
            cachedMap = map
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
