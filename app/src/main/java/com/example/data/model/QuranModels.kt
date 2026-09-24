package com.example.data.model

data class QuranSurah(
    val number: Int,
    val nameAr: String,
    val nameBn: String,
    val nameEn: String,
    val meaningBn: String,
    val meaningEn: String,
    val revelationType: String, // "মাক্কী" or "মাদানী"
    val totalAyat: Int,
    val isAudioDownloaded: Boolean = false,
    val localAudioPath: String? = null
)

data class QuranAyah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val pronunciationBn: String,
    val translationBn: String,
    val translationZakaria: String? = null,
    val translationTaisirul: String? = null,
    val tafsirText: String? = null,
    val isBookmarked: Boolean = false
)

enum class QuranReciter(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val isFavorite: Boolean = false,
    val surahBaseUrl: String,
    val ayahBaseUrl: String
) {
    MISHARY_ALAFASY(
        id = "mishary_alafasy",
        nameBn = "শেখ মিশারী রাশিদ আল-আফাসী",
        nameEn = "Sheikh Mishary Rashid Alafasy",
        isFavorite = true,
        surahBaseUrl = "https://server8.mp3quran.net/afs/",
        ayahBaseUrl = "https://everydayayah.com/data/Alafasy_128kbps/"
    ),
    ABDUL_BASIT(
        id = "abdul_basit",
        nameBn = "শেখ আব্দুল বাসিত আব্দুস সামাদ (মুরত্তাল)",
        nameEn = "Sheikh Abdul Basit Abdul Samad",
        isFavorite = false,
        surahBaseUrl = "https://server7.mp3quran.net/basit/",
        ayahBaseUrl = "https://everydayayah.com/data/Abdul_Basit_Murattal_192kbps/"
    ),
    MAHER_AL_MUAIQLY(
        id = "maher_muaiqly",
        nameBn = "শেখ মাহের আল-মুয়াইক্বলী",
        nameEn = "Sheikh Maher Al-Muaiqly",
        isFavorite = false,
        surahBaseUrl = "https://server12.mp3quran.net/maher/",
        ayahBaseUrl = "https://everydayayah.com/data/MaherAlMuaiqly128kbps/"
    ),
    SAAD_AL_GHAMDI(
        id = "saad_ghamdi",
        nameBn = "শেখ সা‘দ আল-গামদী",
        nameEn = "Sheikh Saad Al-Ghamdi",
        isFavorite = false,
        surahBaseUrl = "https://server7.mp3quran.net/ghamadi/",
        ayahBaseUrl = "https://everydayayah.com/data/Ghamadi_40kbps/"
    ),
    ABDUR_RAHMAN_SUDAIS(
        id = "abdur_rahman_sudais",
        nameBn = "শেখ আব্দুর রহমান আস-সুদাইস",
        nameEn = "Sheikh Abdur Rahman As-Sudais",
        isFavorite = false,
        surahBaseUrl = "https://server11.mp3quran.net/sds/",
        ayahBaseUrl = "https://everydayayah.com/data/Abdurrahmaan_As-Sudais_192kbps/"
    );

    fun getSurahAudioUrl(surahNumber: Int): String {
        val padded = surahNumber.toString().padStart(3, '0')
        return "$surahBaseUrl$padded.mp3"
    }

    fun getAyahAudioUrl(surahNumber: Int, ayahNumber: Int): String {
        val surahPadded = surahNumber.toString().padStart(3, '0')
        val ayahPadded = ayahNumber.toString().padStart(3, '0')
        return "$ayahBaseUrl$surahPadded$ayahPadded.mp3"
    }
}

enum class QuranTranslator(
    val id: String,
    val titleBn: String,
    val shortNameBn: String
) {
    DR_ZAKARIA("zakaria", "ড. আবু বকর মুহাম্মাদ যাকারিয়া (মদীনা প্রিন্ট)", "ড. আবু বকর যাকারিয়া"),
    TAISIRUL_QURAN("taisirul", "তাওহীদ পাবলিকেশন্স (তাইসীরুল কুরআন)", "তাইসীরুল কুরআন"),
    MUHIBBUR_RAHMAN("mujibur", "মাওলানা মুজিবুর রহমান (সহীহ অনুবাদ)", "মুজিবুর রহমান")
}

data class SurahAudioPlayerState(
    val surahNumber: Int? = null,
    val surahNameBn: String = "",
    val reciter: QuranReciter = QuranReciter.MISHARY_ALAFASY,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentPositionMs: Int = 0,
    val durationMs: Int = 0,
    val isOfflineFile: Boolean = false,
    val activeAyahNumber: Int? = null
)

data class DownloadProgressState(
    val surahNumber: Int,
    val isDownloading: Boolean = false,
    val progressPercent: Int = 0,
    val downloadedBytes: Long = 0,
    val totalBytes: Long = 0,
    val errorMessage: String? = null
)
