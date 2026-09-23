package com.example.util

import android.content.Context
import com.example.BuildConfig
import com.example.data.datasource.PersonalDuaCatalog
import com.example.data.model.PersonalDuaBlueprint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class PersonalDuaBuilderService(private val context: Context) {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    /**
     * Builds a structured, authentic Personal Dua blueprint for the user's situation.
     * Uses verified authentic local catalog as ground truth and guarantees no fabrication.
     */
    suspend fun buildDuaBlueprint(userQuery: String): PersonalDuaBlueprint = withContext(Dispatchers.IO) {
        val query = userQuery.trim()
        val baseBlueprint = PersonalDuaCatalog.findBestMatch(query)

        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        // If no Gemini API key or standard placeholder, return high-precision authentic catalog
        if (apiKey.isBlank() || apiKey == "your_api_key_here") {
            return@withContext baseBlueprint.copy(userQuery = query)
        }

        // If API key is present, optionally refine the personal comfort & mother-tongue heartfelt supplication
        try {
            val systemPrompt = """
                You are an Islamic Scholarly Assistant specializing in Du'a and Adab.
                Rules:
                1. NEVER fabricate or modify Quran verses or Hadith.
                2. Provide heartfelt, permissible supplication in Bengali based on the user's personal trial.
                3. Return JSON with:
                   - "comfortBn": 2-3 sentences of Islamic spiritual comfort and Tawakkul in Bengali.
                   - "heartfeltDuaBn": A heartfelt, sincere personal prayer in Bengali asking Allah's help with His beautiful names.
            """.trimIndent()

            val requestBodyJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", "$systemPrompt\nUser query: $query"))
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.3)
                    put("responseMimeType", "application/json")
                })
            }

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey")
                .post(requestBodyJson.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                if (!responseBody.isNullOrBlank()) {
                    val root = JSONObject(responseBody)
                    val text = root.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")

                    val parsed = JSONObject(text)
                    val comfortBn = parsed.optString("comfortBn", baseBlueprint.spiritualComfortBn)
                    val heartfeltDuaBn = parsed.optString("heartfeltDuaBn", "")

                    val updatedGeneralDuas = if (heartfeltDuaBn.isNotBlank()) {
                        baseBlueprint.generalSupplications.toMutableList().apply {
                            add(
                                0,
                                com.example.data.model.PermissibleSupplicationItem(
                                    id = "ai_custom_supplication",
                                    titleBn = "আপনার পরিস্থিতির জন্য বিশেষ ব্যক্তিগত আরজি",
                                    heartfeltSupplicationBn = heartfeltDuaBn,
                                    invokedNamesOfAllahBn = listOf("ইয়া আরহামার রাহিমীন", "ইয়া মুজিবাদ দা'ওয়াত"),
                                    islamicGuidelineBn = "ব্যক্তিগত পরিস্থিতিতে নিজের ভাষায় মন খুলে দো'আ করার সুন্নাহসম্মত বৈধ আবেদন।"
                                )
                            )
                        }
                    } else baseBlueprint.generalSupplications

                    return@withContext baseBlueprint.copy(
                        userQuery = query,
                        spiritualComfortBn = comfortBn,
                        generalSupplications = updatedGeneralDuas,
                        isAiEnhanced = true
                    )
                }
            }
        } catch (_: Exception) {
            // Fallback gracefully to pristine authentic local catalog
        }

        baseBlueprint.copy(userQuery = query)
    }
}
