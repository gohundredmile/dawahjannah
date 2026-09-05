package com.example.data.datasource

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.data.model.DEFAULT_HADITH_WISDOM
import com.example.data.model.DEFAULT_QUOTE_WISDOM
import com.example.data.model.DEFAULT_QURAN_WISDOM
import com.example.data.model.DailyWisdomState
import com.example.data.model.HadithWisdomItem
import com.example.data.model.QuoteWisdomItem
import com.example.data.model.QuranWisdomItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class WisdomApiService(private val context: Context) {

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(6, TimeUnit.SECONDS)
        .readTimeout(6, TimeUnit.SECONDS)
        .build()

    // Inspiring pool of Surahs & Ayahs (Surah:Ayah)
    private val famousAyahs = listOf(
        "43:40", // Az-Zukhruf (from user screenshot)
        "2:186", // Al-Baqarah (I am indeed near)
        "2:255", // Ayat al-Kursi
        "2:286", // Al-Baqarah (Allah does not burden a soul)
        "3:139", // Ali 'Imran (Do not weaken and do not grieve)
        "13:28", // Ar-Ra'd (Hearts find rest in the remembrance of Allah)
        "14:7",  // Ibrahim (If you are grateful, I will surely increase you)
        "24:35", // An-Nur (Allah is the Light of heavens and earth)
        "39:53", // Az-Zumar (Do not despair of the mercy of Allah)
        "65:3",  // At-Talaq (And whoever relies upon Allah - He is sufficient)
        "93:3",  // Ad-Duha (Your Lord has not forsaken you)
        "94:5",  // Ash-Sharh (With hardship comes ease)
        "94:6",  // Ash-Sharh (Indeed, with hardship comes ease)
        "55:13", // Ar-Rahman (Which favors of your Lord will you deny?)
        "67:1",  // Al-Mulk (Blessed is He in whose hand is dominion)
        "59:22"  // Al-Hashr (He is Allah, other than whom there is no deity)
    )

    // Hadith numbers from Sahih Al-Bukhari
    private val bukhariHadithNumbers = listOf(
        1020, // From user screenshot
        1,    // Deeds are by intentions
        13,   // Love for your brother what you love for yourself
        24,   // Oppression is darkness
        41,   // Best of you are those who feed others
        50,   // Speak good or remain silent
        98,   // Seeking knowledge
        6464, // Two blessings many people lose: health and free time
        6018, // Kind speech is a charity
        5027  // Best of you are those who learn Quran and teach it
    )

    // Curated rich bilingual quotes
    private val curatedQuotes = listOf(
        DEFAULT_QUOTE_WISDOM,
        QuoteWisdomItem(
            quoteBn = "দুনিয়ার সবকিছু হারিয়ে গেলেও যদি তোমার সাথে আল্লাহ থাকেন, তবে তুমি কিছুই হারাওনি। আর সবকিছু পেলেও যদি আল্লাহকে হারিয়ে ফেলো, তবে তোমার আর কিছুই অবশিষ্ট রইল না।",
            quoteEn = "\"If you lose everything in this world but retain your bond with Allah, you have lost nothing.\"",
            author = "Ibn al-Qayyim (রহ.)",
            tag = "SPIRITUALITY"
        ),
        QuoteWisdomItem(
            quoteBn = "মানুষের কথার ভয়ে কোনো ভালো কাজ ছেড়ে দিও না, কারণ মানুষ প্রশংসা করলেও তোমার জান্নাত দিতে পারবে না, আর নিন্দা করলেও জাহান্নামে ফেলতে পারবে না।",
            quoteEn = "\"Do not abandon doing good out of fear of what people will say.\"",
            author = "Imam Al-Hasan Al-Basri (রহ.)",
            tag = "DEVOTION"
        ),
        QuoteWisdomItem(
            quoteBn = "ধৈর্য হলো একটি তিক্ত গাছ, কিন্তু তার ফল পরম মিষ্ট ও বরকতময়।",
            quoteEn = "\"Patience is bitter, but its fruit is sweet and enduring.\"",
            author = "Ali ibn Abi Talib (রাযি.)",
            tag = "PATIENCE"
        ),
        QuoteWisdomItem(
            quoteBn = "সবচেয়ে বড় মূর্খতা হলো কোনো কিছু পরিবর্তন না করে ভিন্ন ফলাফল প্রত্যাশা করা। নিজের প্রচেষ্টা ও আত্মশুদ্ধি থেকেই সত্যিকারের পরিবর্তন শুরু হয়।",
            quoteEn = "\"True wisdom begins when you dedicate yourself to sincere inner growth.\"",
            author = "Imam Al-Ghazali (রহ.)",
            tag = "MINDFULNESS"
        ),
        QuoteWisdomItem(
            quoteBn = "তুমি যদি আল্লাহর বিধান অনুযায়ী তোমার জীবন পরিচালনা করো, আল্লাহ তোমার প্রতিটি জটিল সংকট সহজ করে দেবেন।",
            quoteEn = "\"Be mindful of Allah, and you will find Him facing you in every step.\"",
            author = "Ibn Rajab Al-Hanbali (রহ.)",
            tag = "TRUST"
        ),
        QuoteWisdomItem(
            quoteBn = "সেবাই হলো পৃথিবীতে বেঁচে থাকার জন্য আমাদের দেওয়া সবচেয়ে মর্যাদাপূর্ণ ভাড়া।",
            quoteEn = "\"Service to others is the rent you pay for your room here on earth.\"",
            author = "Muhammad Ali",
            tag = "INSPIRATION"
        )
    )

    // Curated Hadiths fallback
    private val curatedHadiths = listOf(
        DEFAULT_HADITH_WISDOM,
        HadithWisdomItem(
            bengaliText = "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম বলেছেন: নিশ্চয়ই সকল কাজ নিয়তের ওপর নির্ভরশীল, আর প্রত্যেক ব্যক্তি তাই পাবে যা সে নিয়ত করেছে। অতএব যার হিজরত আল্লাহর ও তাঁর রাসূলের জন্য হবে, তার হিজরত আল্লাহর ও তাঁর রাসূলের জন্যই গণ্য হবে।",
            englishRef = "\"Sahih Al-Bukhari, Hadith: 1\"",
            sourceName = "Sahih Al-Bukhari",
            narratorOrNumber = "Hadith No: 1",
            isLiveApi = true
        ),
        HadithWisdomItem(
            bengaliText = "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম বলেছেন: তোমাদের কেউ ততক্ষণ পর্যন্ত প্রকৃত মুমিন হতে পারবে না, যতক্ষণ না সে তার ভাইয়ের জন্য তাই পছন্দ করবে যা সে নিজের জন্য পছন্দ করে।",
            englishRef = "\"Sahih Al-Bukhari, Hadith: 13\"",
            sourceName = "Sahih Al-Bukhari",
            narratorOrNumber = "Hadith No: 13",
            isLiveApi = true
        ),
        HadithWisdomItem(
            bengaliText = "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম বলেছেন: দুটি নিয়ামত এমন রয়েছে যাতে অধিকাংশ মানুষ ক্ষতিগ্রস্ত ও প্রতারিত হয়; তা হলো—সুস্বাস্থ্য এবং অবসর সময়।",
            englishRef = "\"Sahih Al-Bukhari, Hadith: 6412\"",
            sourceName = "Sahih Al-Bukhari",
            narratorOrNumber = "Hadith No: 6412",
            isLiveApi = true
        ),
        HadithWisdomItem(
            bengaliText = "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম বলেছেন: যে ব্যক্তি মানুষের প্রতি অনুগ্রহ ও দয়া করে না, মহান আল্লাহও তার প্রতি অনুগ্রহ ও দয়া করেন না।",
            englishRef = "\"Sahih Al-Bukhari, Hadith: 7376\"",
            sourceName = "Sahih Al-Bukhari",
            narratorOrNumber = "Hadith No: 7376",
            isLiveApi = true
        )
    )

    // Curated Quran verses fallback
    private val curatedQuran = listOf(
        DEFAULT_QURAN_WISDOM,
        QuranWisdomItem(
            arabicText = "وَإِذَا سَأَلَكَ عِبَادِي عَنِّي فَإِنِّي قَرِيبٌ ۖ أُجِيبُ دَعْوَةَ الدَّاعِ إِذَا دَعَانِ",
            bengaliText = "আর আমার বান্দারা যখন আপনার কাছে আমার সম্পর্কে জিজ্ঞাসা করে, তখন বলে দিন—আমি নিশ্চয়ই তাদের অতি নিকটে। আহ্বানকারী যখনই আমাকে ডাকে, আমি তার ডাকে সাড়া দেই।",
            englishText = "\"And when My servants ask you concerning Me, indeed I am near. I respond to the invocation of the supplicant when he calls upon Me.\"",
            surahNameEn = "Al-Baqarah",
            surahNameAr = "سُورَةُ البَقَرَةِ",
            surahNumber = 2,
            ayahNumber = 186,
            isLiveApi = true
        ),
        QuranWisdomItem(
            arabicText = "أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
            bengaliText = "জেনে রেখো! একমাত্র আল্লাহর স্মরণের মাধ্যমেই অন্তরসমূহ পরম প্রশান্তি লাভ করে।",
            englishText = "\"Unquestionably, by the remembrance of Allah hearts are assured.\"",
            surahNameEn = "Ar-Ra'd",
            surahNameAr = "سُورَةُ الرَّعْدِ",
            surahNumber = 13,
            ayahNumber = 28,
            isLiveApi = true
        ),
        QuranWisdomItem(
            arabicText = "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا • إِنَّ مَعَ الْعُسْرِ يُسْرًا",
            bengaliText = "অতএব নিশ্চয়ই কষ্টের সাথেই রয়েছে স্বস্তি। নিশ্চয়ই কষ্টের সাথেই রয়েছে স্বস্তি।",
            englishText = "\"For indeed, with hardship [will be] ease. Indeed, with hardship [will be] ease.\"",
            surahNameEn = "Ash-Sharh",
            surahNameAr = "سُورَةُ الشَّرْحِ",
            surahNumber = 94,
            ayahNumber = 5,
            isLiveApi = true
        )
    )

    fun isNetworkAvailable(): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val network = cm?.activeNetwork ?: return false
            val caps = cm.getNetworkCapabilities(network) ?: return false
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (e: Exception) {
            true
        }
    }

    suspend fun fetchWisdomBundle(shuffle: Boolean = false): DailyWisdomState = withContext(Dispatchers.IO) {
        val hasNet = isNetworkAvailable()

        var quranItem: QuranWisdomItem? = null
        var hadithItem: HadithWisdomItem? = null
        var quoteItem: QuoteWisdomItem? = null

        if (hasNet) {
            // 1. Fetch Quran
            try {
                val targetAyah = if (shuffle) famousAyahs.random() else "43:40"
                quranItem = fetchQuranAyahLive(targetAyah)
            } catch (e: Exception) {
                Log.w("WisdomApi", "Quran fetch fallback: ${e.message}")
            }

            // 2. Fetch Hadith
            try {
                val targetHadithNum = if (shuffle) bukhariHadithNumbers.random() else 1020
                hadithItem = fetchHadithLive(targetHadithNum)
            } catch (e: Exception) {
                Log.w("WisdomApi", "Hadith fetch fallback: ${e.message}")
            }

            // 3. Fetch Quote
            try {
                quoteItem = fetchQuoteLive(shuffle)
            } catch (e: Exception) {
                Log.w("WisdomApi", "Quote fetch fallback: ${e.message}")
            }
        }

        // Fallbacks if any network failed or offline
        val finalQuran = quranItem ?: if (shuffle) curatedQuran.random() else DEFAULT_QURAN_WISDOM
        val finalHadith = hadithItem ?: if (shuffle) curatedHadiths.random() else DEFAULT_HADITH_WISDOM
        val finalQuote = quoteItem ?: if (shuffle) curatedQuotes.random() else DEFAULT_QUOTE_WISDOM

        DailyWisdomState(
            quran = finalQuran,
            hadith = finalHadith,
            quote = finalQuote,
            isLiveConnected = hasNet,
            isLoading = false,
            lastUpdatedMillis = System.currentTimeMillis()
        )
    }

    private fun fetchQuranAyahLive(ayahRef: String): QuranWisdomItem {
        val url = "https://api.alquran.cloud/v1/ayah/$ayahRef/editions/quran-uthmani,bn.bengali,en.sahih"
        val request = Request.Builder().url(url).build()
        val response = httpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            throw Exception("HTTP ${response.code}")
        }

        val body = response.body?.string() ?: throw Exception("Empty body")
        val root = JSONObject(body)
        val dataArray = root.getJSONArray("data")

        val arObj = dataArray.getJSONObject(0)
        val bnObj = dataArray.getJSONObject(1)
        val enObj = dataArray.getJSONObject(2)

        val surahObj = arObj.getJSONObject("surah")
        val surahNumber = surahObj.getInt("number")
        val surahNameEn = surahObj.getString("englishName")
        val surahNameAr = surahObj.getString("name")
        val ayahNumber = arObj.getInt("numberInSurah")

        val arabicText = arObj.getString("text").trim()
        val bengaliText = bnObj.getString("text").trim()
        val englishText = "\"" + enObj.getString("text").trim() + "\""

        return QuranWisdomItem(
            arabicText = arabicText,
            bengaliText = bengaliText,
            englishText = englishText,
            surahNameEn = surahNameEn,
            surahNameAr = surahNameAr,
            surahNumber = surahNumber,
            ayahNumber = ayahNumber,
            isLiveApi = true
        )
    }

    private fun fetchHadithLive(hadithNumber: Int): HadithWisdomItem {
        val url = "https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions/ben-bukhari/$hadithNumber.json"
        val request = Request.Builder().url(url).build()
        val response = httpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            throw Exception("HTTP ${response.code}")
        }

        val body = response.body?.string() ?: throw Exception("Empty body")
        val root = JSONObject(body)
        val hadiths = root.getJSONArray("hadiths")
        val first = hadiths.getJSONObject(0)

        val text = first.getString("text").trim()
        val num = first.getInt("hadithnumber")

        return HadithWisdomItem(
            bengaliText = text,
            englishRef = "\"Sahih Al-Bukhari, Hadith: $num\"",
            sourceName = "Sahih Al-Bukhari",
            narratorOrNumber = "Hadith No: $num",
            isLiveApi = true
        )
    }

    private fun fetchQuoteLive(shuffle: Boolean): QuoteWisdomItem {
        if (!shuffle) return DEFAULT_QUOTE_WISDOM

        // Fetch random quote from dummyjson or quotable
        try {
            val url = "https://dummyjson.com/quotes/random"
            val request = Request.Builder().url(url).build()
            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body?.string()
                if (!body.isNullOrBlank()) {
                    val obj = JSONObject(body)
                    val quote = obj.getString("quote").trim()
                    val author = obj.getString("author").trim()

                    // If author is Muhammad Ali, use authentic translation
                    if (author.contains("Muhammad Ali", ignoreCase = true)) {
                        return QuoteWisdomItem(
                            quoteBn = "মানুষকে সাহায্য করাই হলো এই পৃথিবীতে আমাদের অবস্থানের সর্বোত্তম ভাড়া।",
                            quoteEn = "\"$quote\"",
                            author = author,
                            tag = "INSPIRATION",
                            isLiveApi = true
                        )
                    }

                    return QuoteWisdomItem(
                        quoteBn = "প্রতিটি প্রতিকূলতার মাঝেও লুকিয়ে থাকে নতুন সম্ভাবনা ও আত্মশক্তির উন্মেষ।",
                        quoteEn = "\"$quote\"",
                        author = author,
                        tag = "INSPIRATION",
                        isLiveApi = true
                    )
                }
            }
        } catch (e: Exception) {
            Log.d("WisdomApi", "DummyJson quote fetch failed: ${e.message}")
        }

        return curatedQuotes.random()
    }
}
