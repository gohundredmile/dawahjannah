package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily

data class QuranSurahItem(
    val id: String,
    val numberBn: String,
    val nameBn: String,
    val nameAr: String,
    val meaningBn: String,
    val versesCountBn: String,
    val originBn: String,
    val fojilotBn: String,
    val keyAyatAr: String,
    val keyAyatPronunciationBn: String,
    val keyAyatMeaningBn: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HolyQuranDialog(
    onDismiss: () -> Unit,
    onOpenSurahBaqarahSpecial: () -> Unit = {}
) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()

    val surahs = remember {
        listOf(
            QuranSurahItem(
                id = "fatiha",
                numberBn = "১",
                nameBn = "সূরা আল-ফাতিহা",
                nameAr = "الفَاتِحَة",
                meaningBn = "উদ্বোধনকারী বা ভূমিকা",
                versesCountBn = "৭ আয়াত",
                originBn = "মাক্কী",
                fojilotBn = "কুরআনের উম্মুল কুরআন ও শেফা। প্রতিদিনের প্রতি রাকাতে পাঠ ওয়াজিব। সর্বরোগের মহৌষধ।",
                keyAyatAr = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ • الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ • الرَّحْمَٰنِ الرَّحِيمِ • مَالِكِ يَوْمِ الدِّينِ • إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ • اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
                keyAyatPronunciationBn = "বিসমিল্লাহির রাহমানির রাহীম। আলহামদু লিল্লাহি রাব্বিল আলামীন। আর-রাহমানির রাহীম। মালিকি ইয়াওমিদ্দীন। ইয়্যাকা না'বুদু ওয়া ইয়্যাকা নাসতা'ঈন। ইহদিনাস সিরাতাল মুসতাক্বীম।",
                keyAyatMeaningBn = "পরম করুণাময় অসীম দয়ালু আল্লাহর নামে শুরু করছি। সমস্ত প্রশংসা নিখিল বিশ্বের প্রতিপালক আল্লাহর জন্য। যিনি পরম দয়ালু ও অতিশয় করুণাময়। যিনি বিচার দিবসের মালিক। আমরা কেবল তোমারই ইবাদত করি এবং কেবল তোমারই কাছে সাহায্য চাই। আমাদেরকে সরল সঠিক পথ প্রদর্শন করো।"
            ),
            QuranSurahItem(
                id = "baqarah_last_two",
                numberBn = "২",
                nameBn = "সূরা আল-বাক্বারাহ (শেষ ২ আয়াত ও আয়াতুল কুরসী)",
                nameAr = "البَقَرَة",
                meaningBn = "গাভী",
                versesCountBn = "২৮৬ আয়াত",
                originBn = "মাদানীর শ্রেষ্ঠ সূরা",
                fojilotBn = "নবীজী (সা.) বলেছেন: 'যে ব্যক্তি রাতে সূরা বাক্বারাহ শেষ দুই আয়াত পাঠ করবে, তা তার জন্য যথেষ্ট হবে।' শয়তান এই ঘরের কাছে আসতে পারে না।",
                keyAyatAr = "آمَنَ الرَّسُولُ بِمَا أُنزِلَ إِلَيْهِ مِن رَّبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ لَا نُفَرِّقُ بَيْنَ أَحَدٍ مِّن رُّسُلِهِ",
                keyAyatPronunciationBn = "আ-মানার রাসুলু বিমা-উনঝিলা ইলাইহি মির রাব্বিহী ওয়াল মু’মিনুন...",
                keyAyatMeaningBn = "রসূল তাঁর প্রতিপালকের পক্ষ থেকে যা অবতীর্ণ হয়েছে তাতে বিশ্বাস স্থাপন করেছেন এবং মুমিনগণও। সবাই আল্লাহ্, তাঁর ফেরেশতাগণ, তাঁর কিতাবসমূহ এবং তাঁর রসূলগণের উপর বিশ্বাস স্থাপন করেছেন।"
            ),
            QuranSurahItem(
                id = "yasin",
                numberBn = "৩৬",
                nameBn = "সূরা ইয়াসীন",
                nameAr = "يس",
                meaningBn = "ইয়াসীন (কুরআনের হৃৎপিণ্ড)",
                versesCountBn = "৮৩ আয়াত",
                originBn = "মাক্কী",
                fojilotBn = "রাসূলুল্লাহ (সা.) বলেছেন: 'নিশ্চয়ই প্রতিটি জিনিসের একটি হৃৎপিণ্ড রয়েছে, আর কুরআনের হৃৎপিণ্ড হলো সূরা ইয়াসীন।' রাতে পাঠ করলে সকাল বেলা ক্ষমা লাভ হয়।",
                keyAyatAr = "يس ۚ وَالْقُرْآنِ الْحَكِيمِ ۚ إِنَّكَ لَمِنَ الْمُرْسَلِينَ ۚ عَلَىٰ صِرَاطٍ مُّسْتَقِيمٍ",
                keyAyatPronunciationBn = "ইয়া-সীন। ওয়াল কুরআনিল হাকীম। ইন্নাকা লামিনাল মুরসালীন। ‘আলা-সিরা-তিম মুসতাক্বীম।",
                keyAyatMeaningBn = "ইয়াসীন। প্রজ্ঞাময় কুরআনের শপথ! নিশ্চয়ই আপনি প্রেরিত রাসূলদের অন্যতম। সরল সঠিক পথের উপর প্রতিষ্ঠিত।"
            ),
            QuranSurahItem(
                id = "mulk",
                numberBn = "৬৭",
                nameBn = "সূরা আল-মুলক (তাবারাকাল্লাযী)",
                nameAr = "المُلْك",
                meaningBn = "সার্বভৌম কর্তৃত্ব",
                versesCountBn = "৩০ আয়াত",
                originBn = "মাক্কী",
                fojilotBn = "কবরের আযাব থেকে মুক্তিদাতা ও সুপারিশকারী সূরা। রাসূল (সা.) এটি পাঠ না করে কখনো রাতে ঘুমাতেন না।",
                keyAyatAr = "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ ۚ الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا",
                keyAyatPronunciationBn = "তাবারাকাল্লাযী বিয়াদিহিল মুলকু ওয়াহুওয়া ‘আলা-কুল্লি শাইয়িন ক্বাদীর। আল্লাযী খালাকাল মাওতা ওয়াল হায়া-তা লিইয়াবলুওয়াকুম আইয়্যুকুম আহসানু ‘আমালা-।",
                keyAyatMeaningBn = "বরকতময় তিনি যাঁর হাতে সমস্ত কর্তৃত্ব এবং তিনি সব কিছুর উপর সর্বশক্তিমান। যিনি সৃষ্টি করেছেন মৃত্যু ও জীবন, তোমাদেরকে পরীক্ষা করার জন্য যে কে কর্মে শ্রেষ্ঠ।"
            ),
            QuranSurahItem(
                id = "kahf",
                numberBn = "১৮",
                nameBn = "সূরা আল-কাহাফ",
                nameAr = "الكَهْف",
                meaningBn = "গুহা",
                versesCountBn = "১১০ আয়াত",
                originBn = "মাক্কী",
                fojilotBn = "জুমার দিনে সূরা কাহাফ তিলাওয়াতকারীর জন্য এক জুমা থেকে অপর জুমা পর্যন্ত নূর প্রজ্বলিত রাখা হয় এবং দাজ্জালের ফিতনা থেকে সুরক্ষা মেলে।",
                keyAyatAr = "الْحَمْدُ لِلَّهِ الَّذِي أَنزَلَ عَلَىٰ عَبْدِهِ الْكِتَابَ وَلَمْ يَجْعَل لَّهُ عِوَجًا ۜ",
                keyAyatPronunciationBn = "আলহামদু লিল্লাহিল্লাযী আনঝালা ‘আলা-আবদিহিল কিতা-বা ওয়ালাম ইয়াজ‘আল লাহু ‘ইওয়াজা-।",
                keyAyatMeaningBn = "সমস্ত প্রশংসা আল্লাহর যিনি তাঁর বান্দার উপর এই কিতাব অবতীর্ণ করেছেন এবং এতে কোনো বক্রতা রাখেননি।"
            ),
            QuranSurahItem(
                id = "rahman",
                numberBn = "৫৫",
                nameBn = "সূরা আর-রহমান",
                nameAr = "الرَّحْمَٰن",
                meaningBn = "পরম করুণাময়",
                versesCountBn = "৭৮ আয়াত",
                originBn = "মাদানীর সৌন্দর্য",
                fojilotBn = "কুরআনের রূপ ও সৌন্দর্য (আরূসুল কুরআন)। এতে বারবার নিয়ামতের কথা স্মরণ করিয়ে দেওয়া হয়েছে: 'অতএব তোমরা তোমাদের প্রতিপালকের কোন্ কোন্ অনুগ্রহকে অস্বীকার করবে?'",
                keyAyatAr = "فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ",
                keyAyatPronunciationBn = "ফাবিআইয়্যি আ-লা-ই রাব্বিকুমা তুক্বায্যিবান।",
                keyAyatMeaningBn = "অতএব তোমরা উভয়ে (মানব ও দানব) তোমাদের প্রতিপালকের কোন্ কোন্ নিয়ামতকে অস্বীকার করবে?"
            ),
            QuranSurahItem(
                id = "waqiah",
                numberBn = "৫৬",
                nameBn = "সূরা আল-ওয়াকি‘আহ",
                nameAr = "الوَاقِعَة",
                meaningBn = "মহা ঘটনা / কেয়ামত",
                versesCountBn = "৯৬ আয়াত",
                originBn = "মাক্কী",
                fojilotBn = "দারিদ্র্য ও অভাব দূরীকরণকারী সূরা। যে ব্যক্তি প্রতিদিন রাতে সূরা ওয়াকি‘আহ তিলাওয়াত করবে, তাকে কখনো অভাব-অনটন স্পর্শ করবে না।",
                keyAyatAr = "إِذَا وَقَعَتِ الْوَاقِعَةُ ۚ لَيْسَ لِوَقْعَتِهَا كَاذِبَةٌ ۚ خَافِضَةٌ رَّافِعَةٌ",
                keyAyatPronunciationBn = "ইযা-ওয়াক্বা‘আতিল ওয়া-ক্বি‘আহ। লাইসা লিওয়াক্ব‘আতিহা কা-যিবাহ। খা-ফিদাতুর রা-ফি‘আহ।",
                keyAyatMeaningBn = "যখন সংঘটিত হবে সেই অবশ্যম্ভাবী ঘটনা (কেয়ামত), যার সংঘটন অস্বীকার করার মতো কেউ নেই। যা কাউকে অবনমিত করবে এবং কাউকে উন্নীত করবে।"
            ),
            QuranSurahItem(
                id = "ikhlas",
                numberBn = "১১২",
                nameBn = "সূরা আল-ইখলাস (কুল হুআল্লাহু আহাদ)",
                nameAr = "الإِخْلَاص",
                meaningBn = "একনিষ্ঠতা ও তাওহীদ",
                versesCountBn = "৪ আয়াত",
                originBn = "মাক্কী",
                fojilotBn = "কুরআনের এক-তৃতীয়াংশের (১/৩) সমতুল্য। তিনবার পাঠ করলে এক খতম কুরআনের সওয়াব পাওয়া যায়। জান্নাত ওয়াজিবকারী সূরা।",
                keyAyatAr = "قُلْ هُوَ اللَّهُ أَحَدٌ • اللَّهُ الصَّمَدُ • لَمْ يَلِدْ وَلَمْ يُولَدْ • وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
                keyAyatPronunciationBn = "কুল হুওয়াল্লাহু আহাদ। আল্লাহুস সামাদ। লাম ইয়ালিদ ওয়া লাম ইউলাদ। ওয়া লাম ইয়াকুল্লাহু কুফুওয়ান আহাদ।",
                keyAyatMeaningBn = "বলুন: তিনিই আল্লাহ্, এক-অদ্বিতীয়। আল্লাহ্ অমুখাপেক্ষী (সকলের ভরসা)। তিনি কাউকে জন্ম দেননি এবং তাঁকেও কেউ জন্ম দেয়নি। আর তাঁর সমতুল্য কেউই নেই।"
            )
        )
    }

    var selectedSurahId by remember { mutableStateOf("fatiha") }
    val activeSurah = surahs.find { it.id == selectedSurahId } ?: surahs.first()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            color = if (isDark) Color(0xFF041910) else MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isDark) Color(0x33FFFFFF) else MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f)),
                            modifier = Modifier.size(38.dp)
                        ) {
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "ফিরে যান",
                                    tint = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = IslamicGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "পবিত্র আল-কুরআন",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = "দৈনন্দিন শ্রেষ্ঠ সূরাসমূহ, তিলাওয়াত ও ফজিলত",
                                fontSize = 11.5.sp,
                                color = if (isDark) Color(0xFFCBD5E1) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = IslamicGold.copy(alpha = if (isDark) 0.22f else 0.15f),
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = "${surahs.size}টি সূরা",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) IslamicGold else Color(0xFF92400E),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }

                // Surah Selection Carousel
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(surahs) { surah ->
                        val isSelected = surah.id == selectedSurahId
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) {
                                if (isDark) IslamicGold else MaterialTheme.colorScheme.primary
                            } else {
                                if (isDark) Color(0x2EFFFFFF) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                            },
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) IslamicGold else Color(0x33CBD5E1)
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { selectedSurahId = surah.id }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = surah.nameBn,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) {
                                        if (isDark) Color(0xFF0F172A) else Color.White
                                    } else {
                                        if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
                                    }
                                )
                            }
                        }
                    }
                }

                // Content View for Active Surah
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Title Card
                    item {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isDark) Color(0xFF0B2E21) else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.5f)),
                            shadowElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = activeSurah.nameBn,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDark) IslamicGold else MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "অর্থ: ${activeSurah.meaningBn} • ${activeSurah.versesCountBn} • ${activeSurah.originBn}",
                                            fontSize = 12.sp,
                                            color = if (isDark) Color(0xFFCBD5E1) else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Text(
                                        text = activeSurah.nameAr,
                                        fontSize = 22.sp,
                                        fontFamily = LocalArabicFontFamily.current,
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGold
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Fojilot Highlight Box
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = IslamicGold.copy(alpha = if (isDark) 0.15f else 0.1f),
                                    border = BorderStroke(0.8.dp, IslamicGold.copy(alpha = 0.4f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = IslamicGold,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = activeSurah.fojilotBn,
                                            fontSize = 12.sp,
                                            color = if (isDark) Color(0xFFE2E8F0) else Color(0xFF1E293B),
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Key Ayats & Tilawat Card
                    item {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isDark) Color(0xFF092419) else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, if (isDark) Color(0x334ADE80) else Color(0xFFCBD5E1)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "মূল তিলাওয়াত ও অনুবাদ",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) Color(0xFF5EEAD4) else MaterialTheme.colorScheme.primary
                                    )

                                    IconButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val text = "${activeSurah.nameBn}\n\n${activeSurah.keyAyatAr}\n\nউচ্চারণ: ${activeSurah.keyAyatPronunciationBn}\n\nঅর্থ: ${activeSurah.keyAyatMeaningBn}"
                                            clipboard.setPrimaryClip(ClipData.newPlainText("Quran", text))
                                            Toast.makeText(context, "কুরআনের আয়াত ও অর্থ কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "কপি করুন",
                                            tint = IslamicGold,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Arabic Box
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isDark) Color(0x33000000) else Color(0xFFF1F5F9),
                                    border = BorderStroke(0.8.dp, IslamicGold.copy(alpha = 0.35f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = activeSurah.keyAyatAr,
                                        fontSize = 20.sp,
                                        fontFamily = LocalArabicFontFamily.current,
                                        color = if (isDark) Color(0xFFFEF3C7) else Color(0xFF064E3B),
                                        textAlign = TextAlign.End,
                                        lineHeight = 36.sp,
                                        modifier = Modifier.padding(14.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Pronunciation
                                Text(
                                    text = "উচ্চারণ:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGold
                                )
                                Text(
                                    text = activeSurah.keyAyatPronunciationBn,
                                    fontSize = 13.5.sp,
                                    color = if (isDark) Color(0xFFE2E8F0) else Color(0xFF334155),
                                    lineHeight = 20.sp,
                                    modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                                )

                                // Meaning
                                Text(
                                    text = "বাংলা অনুবাদ:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGold
                                )
                                Text(
                                    text = activeSurah.keyAyatMeaningBn,
                                    fontSize = 13.5.sp,
                                    color = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A),
                                    lineHeight = 20.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
