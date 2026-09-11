package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FindReplace
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.IslamicLifeData
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.MoreSubScreen

@Composable
fun SurahBaqarahLastTwoScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current

    val dynamicSections by viewModel.islamicLifeSections.collectAsState()
    val baqarahItems = dynamicSections.firstOrNull { it.id == "surah_baqarah_last_2" }?.items
        ?: IslamicLifeData.sections.firstOrNull { it.id == "surah_baqarah_last_2" }?.items
    val ayat285 = baqarahItems?.getOrNull(0)
    val ayat286 = baqarahItems?.getOrNull(1)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                ),
                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = IslamicGold.copy(alpha = 0.18f),
                        modifier = Modifier.size(52.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "সুরা বাকারাহ'র শেষ ২ আয়াত",
                        fontFamily = banglaFont,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "সূরা আল বাকারাহ • আয়াত ২৮৫ ও ২৮৬",
                        fontFamily = banglaFont,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = IslamicGold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    ) {
                        Text(
                            text = "আরশের নিচের রত্নভাণ্ডার • রাতে তিলাওয়াতকারীর জন্য যথেষ্ট",
                            fontFamily = banglaFont,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Ayat 285 Card
        item {
            val a285Title = ayat285?.titleBn ?: "সূরা আল বাকারাহ - ২:২৮৫"
            val a285Arabic = ayat285?.arabicText ?: "آمَنَ الرَّسُولُ بِمَا أُنزِلَ إِلَيْهِ مِن رَّبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ لَا نُفَرِّقُ بَيْنَ أَحَدٍ مِّن رُّسُلِهِ ۚ وَقَالُوا سَمِعْنَا وَأَطَعْنَا ۖ غُفْرَانَكَ رَبَّنَا وَإِلَيْكَ الْمَصِيرُ ﴿٢٨٥﴾"
            val a285Pronun = ayat285?.pronunciationBn ?: "আ-মানাররাছূলু বিমাউনঝিলা ইলাইহি মির রাব্বিহী ওয়াল মু’মিনূনা কুল্লুন আ-মানা বিল্লাহি ওয়া মালাইকাতিহী ওয়া কুতুবিহী ওয়া রুছুলিহী লা-নুফাররিকুবাইনা আহাদিম মির রুছুলিহী ওয়া কা-লূ ছামি‘না ওয়াআতা‘না গুফরা-নাকা রাব্বানা-ওয়া ইলাইকাল মাসীর।"
            val a285Meaning = ayat285?.meaningBn ?: "রাসূল তার নিকট তার রবের পক্ষ থেকে নাযিলকৃত বিষয়ের প্রতি ঈমান এনেছে, আর মুমিনগণও। প্রত্যেকে ঈমান এনেছে আল্লাহর উপর, তাঁর ফেরেশতাকুল, কিতাবসমূহ ও তাঁর রাসূলগণের উপর, আমরা তাঁর রাসূলগণের কারও মধ্যে তারতম্য করি না। আর তারা বলে, আমরা শুনলাম এবং মানলাম। হে আমাদের রব! আমরা আপনারই ক্ষমা প্রার্থনা করি, আর আপনার দিকেই প্রত্যাবর্তনস্থল।"

            BaqarahAyatCard(
                ayatNumber = a285Title,
                arabicText = a285Arabic,
                pronunciation = a285Pronun,
                meaning = a285Meaning,
                onCopy = {
                    val textToCopy = "$a285Title\n\nমূল আরবীঃ\n$a285Arabic\n\nউচ্চারণঃ\n$a285Pronun\n\nঅনুবাদঃ\n$a285Meaning"
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Ayat 285", textToCopy))
                    Toast.makeText(context, "আয়াত ২৮৫ কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Ayat 286 Card
        item {
            val a286Title = ayat286?.titleBn ?: "সূরা আল বাকারাহ - ২:২৮৬"
            val a286Arabic = ayat286?.arabicText ?: "لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ ۗ رَبَّنَا لَا تُؤَاخِذْنَا إِن نَّسِينَا أَوْ أَخْطَأْنَا ۚ رَبَّنَا وَلَا تَحْمِلْ عَلَيْنَا إِصْرًا كَمَا حَمَلْتَهُ عَلَى الَّذِينَ مِن قَبْلِنَا ۚ رَبَّنَا وَلَا تُحَمِّلْنَا مَا لَا طَاقَةَ لَنَا بِهِ ۖ وَاعْفُ عَنَّا وَاغْفِرْ لَنَا وَارْحَمْنَا ۚ أَنتَ مَوْلَانَا فَانصُرْنَا عَلَى الْقَوْمِ الْكَافِرِينَ ﴿٢٨٦﴾"
            val a286Pronun = ayat286?.pronunciationBn ?: "লা-ইউকালিলফুল্লা-হু নাফছান ইল্লা-উছ‘আহা-লাহা-মা কাছাবাত ওয়া ‘আলাইহা-মাকতাছাবাত রাব্বানা-লা-তুআ-খিযনা ইন নাছীনা-আও আখতা’না-রাব্বানা ওয়ালা-তাহমিল ‘আলাইনা-ইসরান কামা-হামালতাহূ আলাল্লাযীনা মিন কাবলিনা-রাব্বানা-ওয়ালা তুহাম্মিলনা-মা-লা-তা-কাতা লানা-বিহী ওয়া‘ফু‘আন্না-ওয়াগফিরলানা-ওয়ারহামনা-আনতা মাওলা-না-ফানসুরনা-‘আলাল কাওমিল কা-ফিরীন।"
            val a286Meaning = ayat286?.meaningBn ?: "\"আল্লাহ কোন ব্যক্তির উপর তার সাধ্যের অতিরিক্ত কিছু আরোপ করেন না, সে ভাল যা করেছে সে তার সওয়াব পাবে এবং স্বীয় মন্দ কৃতকর্মের জন্য সে নিজেই নিগ্রহ ভোগ করবে। হে আমাদের প্রতিপালক! আমরা যদি ভুলে যাই কিংবা ভুল করি, তাহলে আমাদেরকে পাকড়াও করো না, হে আমাদের প্রতিপালক! আমাদের আগের লোকেদের উপর যেমন গুরু-দায়িত্ব অর্পণ করেছিলে, আমাদের উপর তেমন দায়িত্ব অর্পণ করো না; হে আমাদের প্রতিপালক! যে ভার বহনের ক্ষমতা আমাদের নেই, এমন ভার আমাদের উপর চাপিয়ে দিও না, (ভুল-ত্রুটি উপেক্ষা করে) আমাদেরকে রেহাই দাও, আমাদেরকে ক্ষমা কর এবং আমাদের প্রতি দয়া কর; তুমিই আমাদের প্রতিপালক, কাজেই আমাদেরকে কাফিরদের উপর জয়যুক্ত কর।\""

            BaqarahAyatCard(
                ayatNumber = a286Title,
                arabicText = a286Arabic,
                pronunciation = a286Pronun,
                meaning = a286Meaning,
                onCopy = {
                    val textToCopy = "$a286Title\n\nমূল আরবীঃ\n$a286Arabic\n\nউচ্চারণঃ\n$a286Pronun\n\nঅনুবাদঃ\n$a286Meaning"
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Ayat 286", textToCopy))
                    Toast.makeText(context, "আয়াত ২৮৬ কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Section Title: সূরা বাকারার শেষ দুই আয়াতের ফজিলতঃ
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = IslamicGold.copy(alpha = 0.2f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "সূরা বাকারার শেষ দুই আয়াতের ফজিলতঃ",
                    fontFamily = banglaFont,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Hadith 1: Muslim 806 (Two Lights)
        item {
            BaqarahHadithCard(
                title = "দুটো নূরের সুসংবাদ ও অবারিত দু'আ কবুল",
                source = "সহিহ মুসলিম, হাদীস নং- ৮০৬",
                icon = Icons.Default.AutoAwesome,
                narrator = "হযরত ইবনু আব্বাস (রাঃ) / জিবরাঈল (আঃ)",
                hadithText = "কোনো একদিন হযরত জিবরাঈল (আঃ) নবী করীম (ﷺ)-এর কাছে বসে ছিলেন। হঠাৎ প্রচন্ড একটি শব্দ শোনা গেলো । হযরত জিবরাঈল (আঃ) নিজের মাথা উঁচু করে বললেন, এটা আকাশের সেই দরজা খোলার শব্দ যা আজকের পূর্বে আর কখনো খোলা হয়নি। উক্ত দরজা দিয়ে একজন ফিরিশতা পৃথিবীতে অবতরণ করেছেন, যিনি ইতোপূর্বে আর কখনো পৃথিবীতে আগমন করেননি । সে ফিরিশতা নবী করীম (ﷺ)-এর কাছে এসে বললেন, আপনার জন্যে দুটো নূরের সুসংবাদ রয়েছে। সূরাতুল ফাতিহা এবং সূরাতুল বাকারার শেষ দুটো আয়াত উক্ত দুটো নূর । যা আপনার পূর্বে অন্য কোনো নবীকে প্রদান করা হয়নি। সূরা ফাতিহা এবং সূরাতুল বাকারার শেষ দুটো আয়াত থেকে একটি অক্ষরও পড়ে আল্লাহ তা'য়ালার কাছে আপনি যা কিছু প্রার্থনা করবেন আপনাকে তা প্রদান করা হবে।"
            )
        }

        // Hadith 2: Tirmidhi 2882
        item {
            BaqarahHadithCard(
                title = "শয়তানের উপস্থিতি থেকে তিন রাত ঘর সুরক্ষিত রাখা",
                source = "সুনান আত-তিরমিজি— ২৮৮২ (সহীহ)",
                icon = Icons.Default.Shield,
                narrator = "নু‘মান ইবনু বাশীর (রাঃ)",
                hadithText = "নবী (ﷺ) বলেছেনঃ আল্লাহ তা‘আলা আসমান-যামীন সৃষ্টির দুই হাজার বছর পূর্বে একটি কিতাব লিখেছেন। সেই কিতাব হতে তিনি দু‘টি আয়াত নাযিল করছেন। সেই দু‘টি আয়াতের মাধ্যমেই সূরা আল-বাক্বারা সমাপ্ত করেছেন। যে ঘরে তিন রাত এ দু‘টি আয়াত তিলাওয়াত করা হয় শয়তান সেই ঘরের নিকট আসতে পারে না।"
            )
        }

        // Hadith 3: Mishkat 2169
        item {
            BaqarahHadithCard(
                title = "আল্লাহর আরশের নীচের ভাণ্ডার ও উভয় জাহানের কল্যাণ",
                source = "মিশকাতুল মাসাবীহ— ২১৬৯",
                icon = Icons.Default.FormatQuote,
                narrator = "আয়ফা' ইবনু ‘আবদিল কালা‘ঈ (রাঃ)",
                hadithText = "তিনি বলেন, এক ব্যক্তি আরয করলেন, হে আল্লাহর নবী(ﷺ)! কুরআনের কোন্ আয়াত এমন, যার বরকত আপনার ও আপনার উম্মাতের কাছে পৌঁছতে আপনি ভালবাসেন? তিনি (ﷺ) বললেন, সূরা আল বাকারাহ্’র শেষাংশ। কেননা আল্লাহ তা‘আলা তাঁর ‘আরশের নীচের ভাণ্ডার হতে তা এ উম্মতকে দান করেছেন। দুনিয়া ও আখিরাতের এমন কোন কল্যাণ নেই যা এতে নেই।"
            )
        }

        // Hadith 4: Mishkat 2173
        item {
            BaqarahHadithCard(
                title = "বিশেষ রহমত, নৈকট্য লাভ ও নারী-পরিবারকে শিক্ষা দান",
                source = "মিশকাতুল মাসাবীহ— ২১৭৩",
                icon = Icons.Default.FormatQuote,
                narrator = "জুবায়র ইবনু নুফায়র (রাঃ)",
                hadithText = "তিনি বলেন, রসূলুল্লাহ (ﷺ) বলেছেনঃ সূরা আল বাকারাকে আল্লাহ তা‘আলা এমন দু’টি আয়াত দ্বারা শেষ করেছেন, যা আমাকে আল্লাহর ‘আরশের নীচের ভাণ্ডার হতে দান করা হয়েছে। তাই তোমরা এ আয়াতগুলোকে শিখবে। তোমাদের রমণীকুলকেও শিখাবে। কারণ এ আয়াতগুলো হচ্ছে রহমত, (আল্লাহর) নৈকট্য লাভের উপায়। (দীন দুনিয়ার সকল) কল্যাণ লাভের দু‘আ।"
            )
        }

        // Hadith 5: Tafsir Ibn Kathir
        item {
            BaqarahHadithCard(
                title = "বিবেকসম্পন্ন মুমিনের আবশ্যকীয় রাত্রিকালীন আমল",
                source = "তাফসীরে ইবনে কাসীর, ১ম খন্ড, পৃষ্ঠা-৭৩৫",
                icon = Icons.Default.Star,
                narrator = "হযরত আলী ইবনে আবী তালিব (রাঃ)",
                hadithText = "হযরত আলী (রাঃ) বলেছেন, এটা আমার জানা নেই, উপযুক্ত বয়সের এবং জ্ঞান-বিবেক বুদ্ধি সম্পন্ন কোনো মুসলমানদের মধ্যে এমন কেউ রয়েছে যে, রাতে ঘুমানোর পূর্বে আয়াতুল কুরসী এবং সূরাতুল বাকারার শেষ দুটো আয়াত তিলাওয়াত করে না।"
            )
        }

        // Hadith 6: Bukhari 4008
        item {
            BaqarahHadithCard(
                title = "রাতের বেলা তিলাওয়াতকারীর জন্য যথেষ্ট",
                source = "সহিহ বুখারী— ৪০০৮",
                icon = Icons.Default.Shield,
                narrator = "বাদ্রী সাহাবী আবূ মাস‘উদ (রাঃ)",
                hadithText = "তিনি বলেন, রসূলুল্লাহ্ (ﷺ) বলেছেন, সূরা বাকারার শেষে এমন দু’টি আয়াত রয়েছে যে ব্যক্তি রাতের বেলা আয়াত দু’টি তিলাওয়াত করবে তার জন্য এ আয়াত দু’টোই যথেষ্ট। অর্থাৎ রাত্রে কুরআন মাজীদ তেলাওয়াত করার যে হক রয়েছে, কমপক্ষে সূরাহ বাকারার শেষ দু’টি আয়াত তেলাওয়াত করলে তার জন্য তা যথেষ্ট।"
            )
        }

        // Ayat Detector & Solver Navigation Promo Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                ),
                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FindReplace,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "আয়াত ও হাদিস শুদ্ধিকরণ ল্যাব (Detector & Solver)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "আরবী আয়াত বা হাদিসের টেক্সটে কোনো ভুল হরকত, ভাঙ্গা শব্দ বা অনাকাঙ্ক্ষিত বর্ণ থাকলে আমাদের স্মার্ট ডিটেক্টর ও সলভার টুল দিয়ে তা পরীক্ষা ও এক ক্লিকে সমাধান করুন।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FilledTonalButton(
                        onClick = { viewModel.navigateToMoreSubScreen(MoreSubScreen.AYAT_DETECTOR_SOLVER) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.FindReplace,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ডিটেক্টর ও সলভার টুল খুলুন",
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun BaqarahAyatCard(
    ayatNumber: String,
    arabicText: String,
    pronunciation: String,
    meaning: String,
    onCopy: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Ayat Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = ayatNumber,
                        fontFamily = banglaFont,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                IconButton(
                    onClick = onCopy,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "কপি করুন",
                        tint = IslamicGold,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Arabic Text
            Text(
                text = "মূল আরবীঃ",
                fontFamily = banglaFont,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = arabicText,
                    fontFamily = LocalArabicFontFamily.current,
                    fontSize = 24.sp,
                    lineHeight = 42.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bengali Pronunciation
            Text(
                text = "উচ্চারণঃ",
                fontFamily = banglaFont,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = IslamicGold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = pronunciation,
                fontFamily = banglaFont,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 24.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            Spacer(modifier = Modifier.height(12.dp))

            // Bengali Translation
            Text(
                text = "অনুবাদঃ",
                fontFamily = banglaFont,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = meaning,
                fontFamily = banglaFont,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun BaqarahHadithCard(
    title: String,
    source: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    narrator: String,
    hadithText: String
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.14f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = IslamicGold.copy(alpha = 0.14f),
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontFamily = banglaFont,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "বর্ণনাকারী: $narrator",
                        fontFamily = banglaFont,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = hadithText,
                    fontFamily = banglaFont,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 23.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = source,
                        fontFamily = banglaFont,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}
