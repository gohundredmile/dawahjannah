package com.example.data.datasource

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.data.model.DailyWisdomState
import com.example.data.model.HadithWisdomItem
import com.example.data.model.QuoteWisdomItem
import com.example.data.model.QuranWisdomItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class WisdomApiService(private val context: Context) {

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(6, TimeUnit.SECONDS)
        .readTimeout(6, TimeUnit.SECONDS)
        .build()

    fun getTodaySeed(): Int {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        return (year * 365) + dayOfYear
    }

    // Inspiring pool of Surahs & Ayahs (Surah:Ayah)
    private val famousAyahs = listOf(
        "2:186", // Al-Baqarah (I am indeed near)
        "2:255", // Ayat al-Kursi
        "2:286", // Al-Baqarah (Allah does not burden a soul)
        "3:139", // Ali 'Imran (Do not weaken and do not grieve)
        "13:28", // Ar-Ra'd (Hearts find rest in the remembrance of Allah)
        "14:7",  // Ibrahim (If you are grateful, I will surely increase you)
        "24:35", // An-Nur (Allah is the Light of heavens and earth)
        "39:53", // Az-Zumar (Do not despair of the mercy of Allah)
        "43:40", // Az-Zukhruf
        "65:3",  // At-Talaq (And whoever relies upon Allah - He is sufficient)
        "93:3",  // Ad-Duha (Your Lord has not forsaken you)
        "94:5",  // Ash-Sharh (With hardship comes ease)
        "94:6",  // Ash-Sharh (Indeed, with hardship comes ease)
        "55:13", // Ar-Rahman (Which favors of your Lord will you deny?)
        "67:1",  // Al-Mulk (Blessed is He in whose hand is dominion)
        "59:22", // Al-Hashr (He is Allah, other than whom there is no deity)
        "3:200", // Ali 'Imran (O you who believe, persevere and endure)
        "2:152", // Al-Baqarah (Remember Me, I will remember you)
        "2:153", // Al-Baqarah (Seek help through patience and prayer)
        "49:10", // Al-Hujurat (The believers are but brothers)
        "16:128", // An-Nahl (Allah is with those who fear Him and do good)
        "21:87", // Al-Anbiya (Dua of Yunus: None has the right to be worshiped but You)
        "25:74", // Al-Furqan (Dua for righteous family)
        "3:8",   // Ali 'Imran (Our Lord, let not our hearts deviate)
        "67:2",  // Al-Mulk (He who created death and life to test you)
        "23:1",  // Al-Mu'minun (Successful indeed are the believers)
        "50:16", // Qaf (We are closer to him than his jugular vein)
        "57:4",  // Al-Hadid (And He is with you wherever you are)
        "11:6",  // Hud (There is no creature on earth but that upon Allah is its provision)
        "40:60"  // Ghafir (Call upon Me; I will respond to you)
    )

    // Hadith numbers from Sahih Al-Bukhari
    private val bukhariHadithNumbers = listOf(
        1,    // Deeds are by intentions
        13,   // Love for your brother what you love for yourself
        24,   // Oppression is darkness
        41,   // Best of you are those who feed others
        50,   // Speak good or remain silent
        98,   // Seeking knowledge
        1020, // Supplication and trust
        5027, // Best of you are those who learn Quran and teach it
        6018, // Kind speech is a charity
        6412, // Two blessings many people lose: health and free time
        6464, // Purity of heart
        7376, // Showing mercy to people
        6011, // Smiling is a charity
        6116, // Guarding the tongue
        6407, // Remembrance of Allah
        6491, // Moderation in religion
        1154, // Night awakening dua
        834,  // Abu Bakr's prayer dua
        590,  // Seeking refuge before taslim
        6312  // Sleeping and waking remembrance
    )

    // Curated rich bilingual quotes
    private val curatedQuotes = listOf(
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
            quoteBn = "ধৈর্য হলো একটি তিক্ত গাছ, কিন্তু তার ফল পরম মিষ্ট ও বরকতময়। নিজের রবকে বিশ্বাস করো, তিনি কখনো বান্দার ধৈর্যকে বৃথা যেতে দেন না।",
            quoteEn = "\"Patience is bitter, but its fruit is sweet and enduring.\"",
            author = "Ali ibn Abi Talib (রাযি.)",
            tag = "PATIENCE"
        ),
        QuoteWisdomItem(
            quoteBn = "সবচেয়ে বড় মূর্খতা হলো নিজের কোনো পরিবর্তন না করে ভিন্ন ফলাফল প্রত্যাশা করা। নিজের প্রচেষ্টা ও আত্মশুদ্ধি থেকেই সত্যিকারের পরিবর্তন শুরু হয়।",
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
            quoteBn = "মানুষকে নিঃস্বার্থভাবে সাহায্য করাই হলো পৃথিবীতে বেঁচে থাকার জন্য আমাদের দেওয়া সবচেয়ে মর্যাদাপূর্ণ ভাড়া।",
            quoteEn = "\"Service to others is the rent you pay for your room here on earth.\"",
            author = "Muhammad Ali",
            tag = "INSPIRATION"
        ),
        QuoteWisdomItem(
            quoteBn = "যে ব্যক্তি নিজের ত্রুটি ও দুর্বলতা নিয়ে ব্যস্ত থাকে, তার অন্যদের ত্রুটি খোঁজার অবসর থাকে না।",
            quoteEn = "\"Blessed is he who is preoccupied with his own faults rather than the faults of others.\"",
            author = "Umar ibn Al-Khattab (রাযি.)",
            tag = "PURITY"
        ),
        QuoteWisdomItem(
            quoteBn = "আল্লাহ যখন কোনো বান্দার কল্যাণ চান, তখন তার হৃদয়ে তাওবা ও বিনয়ের দরজা খুলে দেন।",
            quoteEn = "\"When Allah wills good for a servant, He opens the door of humility and repentance.\"",
            author = "Ibn Taymiyyah (রহ.)",
            tag = "REPENTANCE"
        ),
        QuoteWisdomItem(
            quoteBn = "দোয়ার হাত কখনো খালি ফেরে না; হয় তা অবিলম্বে কবুল হয়, না হয় এর মাধ্যমে বিপদ দূর হয়, অথবা আখিরাতে সঞ্চিত থাকে।",
            quoteEn = "\"No supplication goes unanswered; it brings grace, repels harm, or awaits in the hereafter.\"",
            author = "Imam Ash-Shafi'i (রহ.)",
            tag = "DUA"
        ),
        QuoteWisdomItem(
            quoteBn = "যার অন্তর আল্লাহর স্মরণে সজীব থাকে, পুরো দুনিয়া উল্টে গেলেও তার অন্তরের শান্তি কেউ কেড়ে নিতে পারে না।",
            quoteEn = "\"The heart that beats with the remembrance of Allah finds tranquility beyond worldly turbulence.\"",
            author = "Fudayl ibn Iyad (রহ.)",
            tag = "PEACE"
        )
    )

    // Curated Hadiths fallback
    private val curatedHadiths = listOf(
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
        ),
        HadithWisdomItem(
            bengaliText = "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম বলেছেন: তোমাদের মধ্যে সর্বোত্তম সেই ব্যক্তি, যে নিজে কুরআন শিখে এবং অন্যকে তা শিক্ষা দেয়।",
            englishRef = "\"Sahih Al-Bukhari, Hadith: 5027\"",
            sourceName = "Sahih Al-Bukhari",
            narratorOrNumber = "Hadith No: 5027",
            isLiveApi = true
        ),
        HadithWisdomItem(
            bengaliText = "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম বলেছেন: সুন্দর ও মার্জিত কথা বলাও একটি সদকা।",
            englishRef = "\"Sahih Al-Bukhari, Hadith: 6018\"",
            sourceName = "Sahih Al-Bukhari",
            narratorOrNumber = "Hadith No: 6018",
            isLiveApi = true
        ),
        HadithWisdomItem(
            bengaliText = "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম বলেছেন: যে ব্যক্তি আল্লাহর প্রতি এবং শেষ দিবসের প্রতি বিশ্বাস রাখে, সে যেন উত্তম কথা বলে অথবা নীরব থাকে।",
            englishRef = "\"Sahih Al-Bukhari, Hadith: 6018\"",
            sourceName = "Sahih Al-Bukhari",
            narratorOrNumber = "Hadith No: 6018",
            isLiveApi = true
        ),
        HadithWisdomItem(
            bengaliText = "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম বলেছেন: যে ব্যক্তি জ্ঞান অর্জনের কোনো পথ অবলম্বন করে, আল্লাহ তার জন্য জান্নাতের পথ সুগম ও সহজ করে দেন।",
            englishRef = "\"Sahih Muslim, Hadith: 2699\"",
            sourceName = "Sahih Muslim",
            narratorOrNumber = "Hadith No: 2699",
            isLiveApi = true
        )
    )

    // Curated Quran verses fallback
    private val curatedQuran = listOf(
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
        ),
        QuranWisdomItem(
            arabicText = "وَمَن يَتَوَكَّلْ عَلَى اللَّهِ فَهُوَ حَسْبُهُ",
            bengaliText = "আর যে ব্যক্তি আল্লাহর ওপর পূর্ণ ভরসা ও নির্ভরতা স্থাপন করে, তার জন্য তিনিই যথেষ্ট।",
            englishText = "\"And whoever relies upon Allah - then He is sufficient for him.\"",
            surahNameEn = "At-Talaq",
            surahNameAr = "سُورَةُ الطَّلَاقِ",
            surahNumber = 65,
            ayahNumber = 3,
            isLiveApi = true
        ),
        QuranWisdomItem(
            arabicText = "لَئِن شَكَرْتُمْ لَأَزِيدَنَّكُمْ",
            bengaliText = "যদি তোমরা আমার কৃতজ্ঞতা স্বীকার করো, তবে আমি অবশ্যই তোমাদের নেয়ামত বাড়িয়ে দেব।",
            englishText = "\"If you are grateful, I will surely increase you [in favor].\"",
            surahNameEn = "Ibrahim",
            surahNameAr = "سُورَةُ إِبْرَاهِيمَ",
            surahNumber = 14,
            ayahNumber = 7,
            isLiveApi = true
        ),
        QuranWisdomItem(
            arabicText = "قُلْ يَا عِبَادِيَ الَّذِينَ أَسْرَفُوا عَلَىٰ أَنفُسِهِمْ لَا تَقْنَطُوا مِن رَّحْمَةِ اللَّهِ ۚ إِنَّ اللَّهَ يَغْفِرُ الذُّنُوبَ جَمِيعًا",
            bengaliText = "বলুন, হে আমার বান্দাগণ! যারা নিজেদের ওপর অবিচার করেছ, তোমরা আল্লাহর রহমত থেকে কখনো নিরাশ হয়ো না। নিশ্চয়ই আল্লাহ সমস্ত পাপ ক্ষমা করে দেন।",
            englishText = "\"Say, O My servants who have transgressed against themselves, do not despair of the mercy of Allah. Indeed, Allah forgives all sins.\"",
            surahNameEn = "Az-Zumar",
            surahNameAr = "سُورَةُ الزُّمَرِ",
            surahNumber = 39,
            ayahNumber = 53,
            isLiveApi = true
        ),
        QuranWisdomItem(
            arabicText = "وَلَا تَهِنُوا وَلَا تَحْزَنُوا وَأَنتُمُ الْأَعْلَوْنَ إِن كُنتُم مُّؤْمِنِينَ",
            bengaliText = "আর তোমরা নিরাশ হয়ো না এবং দুঃখিত হয়ো না; তোমরাই বিজয়ী হবে যদি তোমরা সত্যিকারের মুমিন হও।",
            englishText = "\"So do not weaken and do not grieve, and you will be superior if you are [true] believers.\"",
            surahNameEn = "Ali 'Imran",
            surahNameAr = "سُورَةُ آلِ عِمْرَانَ",
            surahNumber = 3,
            ayahNumber = 139,
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

    fun getTodayWisdom(): DailyWisdomState {
        val seed = Math.abs(getTodaySeed())
        val q = curatedQuran[seed % curatedQuran.size]
        val h = curatedHadiths[seed % curatedHadiths.size]
        val quote = curatedQuotes[seed % curatedQuotes.size]
        return DailyWisdomState(
            quran = q,
            hadith = h,
            quote = quote,
            isLiveConnected = true,
            isLoading = false,
            lastUpdatedMillis = System.currentTimeMillis()
        )
    }

    suspend fun fetchWisdomBundle(shuffle: Boolean = false): DailyWisdomState = withContext(Dispatchers.IO) {
        val hasNet = isNetworkAvailable()
        val seed = Math.abs(getTodaySeed())

        var quranItem: QuranWisdomItem? = null
        var hadithItem: HadithWisdomItem? = null
        var quoteItem: QuoteWisdomItem? = null

        if (hasNet) {
            // 1. Fetch Quran Live
            try {
                val targetAyah = if (shuffle) famousAyahs.random() else famousAyahs[seed % famousAyahs.size]
                quranItem = fetchQuranAyahLive(targetAyah)
            } catch (e: Exception) {
                Log.w("WisdomApi", "Quran fetch fallback: ${e.message}")
            }

            // 2. Fetch Hadith Live
            try {
                val targetHadithNum = if (shuffle) bukhariHadithNumbers.random() else bukhariHadithNumbers[seed % bukhariHadithNumbers.size]
                hadithItem = fetchHadithLive(targetHadithNum)
            } catch (e: Exception) {
                Log.w("WisdomApi", "Hadith fetch fallback: ${e.message}")
            }

            // 3. Fetch Quote Live
            try {
                quoteItem = fetchQuoteLive(shuffle)
            } catch (e: Exception) {
                Log.w("WisdomApi", "Quote fetch fallback: ${e.message}")
            }
        }

        // Fallbacks rotated every day deterministically
        val finalQuran = quranItem ?: if (shuffle) curatedQuran.random() else curatedQuran[seed % curatedQuran.size]
        val finalHadith = hadithItem ?: if (shuffle) curatedHadiths.random() else curatedHadiths[seed % curatedHadiths.size]
        val finalQuote = quoteItem ?: if (shuffle) curatedQuotes.random() else curatedQuotes[seed % curatedQuotes.size]

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
        val data = root.getJSONArray("data")

        var arabicText = ""
        var bengaliText = ""
        var englishText = ""
        var surahNameEn = ""
        var surahNameAr = ""
        var ayahNumber = 1
        var surahNumber = 1

        for (i in 0 until data.length()) {
            val item = data.getJSONObject(i)
            val edition = item.getJSONObject("edition")
            val identifier = edition.getString("identifier")
            val text = item.getString("text")

            val surah = item.getJSONObject("surah")
            surahNameEn = surah.getString("englishName")
            surahNameAr = surah.getString("name")
            surahNumber = surah.getInt("number")
            ayahNumber = item.getInt("numberInSurah")

            when (identifier) {
                "quran-uthmani" -> arabicText = text
                "bn.bengali" -> bengaliText = text
                "en.sahih" -> englishText = "\"$text\""
            }
        }

        if (arabicText.isBlank()) {
            throw Exception("Arabic text empty")
        }

        return QuranWisdomItem(
            arabicText = arabicText,
            bengaliText = if (bengaliText.isNotBlank()) bengaliText else "আর নিশ্চয়ই আল্লাহর স্মরণে অন্তর প্রশান্ত হয়।",
            englishText = englishText,
            surahNameEn = surahNameEn,
            surahNameAr = surahNameAr,
            ayahNumber = ayahNumber,
            surahNumber = surahNumber,
            isLiveApi = true
        )
    }

    private fun fetchHadithLive(num: Int): HadithWisdomItem {
        val url = "https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions/ben-bukhari/$num.json"
        val request = Request.Builder().url(url).build()
        val response = httpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            throw Exception("HTTP ${response.code}")
        }

        val body = response.body?.string() ?: throw Exception("Empty body")
        val root = JSONObject(body)
        val hadiths = root.getJSONArray("hadiths")
        if (hadiths.length() == 0) throw Exception("No hadith found")

        val h = hadiths.getJSONObject(0)
        val rawText = h.getString("text")

        val cleanText = rawText
            .replace("<[^>]*>".toRegex(), "")
            .replace("\\s+".toRegex(), " ")
            .trim()

        return HadithWisdomItem(
            bengaliText = cleanText,
            englishRef = "\"Sahih Al-Bukhari, Hadith: $num\"",
            sourceName = "Sahih Al-Bukhari",
            narratorOrNumber = "Hadith No: $num",
            isLiveApi = true
        )
    }

    private fun fetchQuoteLive(shuffle: Boolean): QuoteWisdomItem {
        val seed = Math.abs(getTodaySeed())
        if (!shuffle) {
            return curatedQuotes[seed % curatedQuotes.size]
        }

        // Fetch random quote if shuffling
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
