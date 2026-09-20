package com.example.ui.components

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.IslamicGold

data class NamazRakatGuide(
    val nameBn: String,
    val totalRakatBn: String,
    val breakdownBn: String,
    val timeInfoBn: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NamazGuideDialog(
    onDismiss: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    val rakatList = listOf(
        NamazRakatGuide("ফজর (Fajr)", "৪ রাকাত", "২ রাকাত সুন্নত (মুয়াক্কাদাহ) + ২ রাকাত ফরজ", "সুবহে সাদিক থেকে সূর্যোদয়ের পূর্ব পর্যন্ত"),
        NamazRakatGuide("যোহর (Dhuhr)", "১২ রাকাত", "৪ রাকাত সুন্নত + ৪ রাকাত ফরজ + ২ রাকাত সুন্নত + ২ রাকাত নফল", "দ্বিপ্রহরের পর সূর্য হেলে পড়া থেকে আসরের ওয়াক্ত শুরু পর্যন্ত"),
        NamazRakatGuide("আসর (Asr)", "৮ বা ৪ রাকাত", "৪ রাকাত সুন্নতে গায়রে মুয়াক্কাদাহ + ৪ রাকাত ফরজ", "ছায়া দ্বিগুণ/একগুণ হওয়া থেকে সূর্যাস্তের আগ পর্যন্ত"),
        NamazRakatGuide("মাগরিব (Maghrib)", "৭ রাকাত", "৩ রাকাত ফরজ + ২ রাকাত সুন্নত + ২ রাকাত নফল (আওয়াবীন)", "সূর্যাস্তের পর থেকে পশ্চিম আকাশে লালিমা থাকা পর্যন্ত"),
        NamazRakatGuide("এশা (Isha)", "১৭ রাকাত", "৪ রাকাত সুন্নত + ৪ রাকাত ফরজ + ২ রাকাত সুন্নত + ২ রাকাত নফল + ৩ রাকাত বিতর + ২ রাকাত নফল", "মাগরিবের ওয়াক্ত শেষ থেকে সুবহে সাদিকের পূর্ব পর্যন্ত"),
        NamazRakatGuide("জুমা (Jummah - শুক্রবার)", "১৪ রাকাত", "৪ রাকাত কাবলাল জুমা + ২ রাকাত ফরজ + ৪ রাকাত বা'দাল জুমা + ২ রাকাত সুন্নত + ২ রাকাত নফল", "যোহরের ওয়াক্তে জুমার খুতবার পর")
    )

    val stepGuides = listOf(
        "১. নিয়ত ও তাকবীরে তাহরীমা" to "কিবলামুখী হয়ে সালাতের দৃঢ় সংকল্প করে উভয় হাত কান/কাঁধ বরাবর উঠিয়ে 'আল্লাহু আকবার' বলে হাত বাঁধা।",
        "২. ছানা ও সূরা কিরাত" to "সুবহানাকা আল্লাহুম্মা... পাঠ শেষে আউযুবিল্লাহ-বিসমিল্লাহ সহ সূরা ফাতিহা এবং সাথে অন্য একটি সূরা মিলিয়ে পড়া।",
        "৩. রুকু ও কাওমা" to "'আল্লাহু আকবার' বলে রুকুতে যাওয়া, ৩ বার 'সুবহানা রাব্বিয়াল আযীম' পাঠ করা। অতঃপর 'সামিআল্লাহু লিমান হামিদাহ, রাব্বানা লাকাল হামদ' বলে সোজা হয়ে দাঁড়ানো।",
        "৪. সিজদা ও জলসা" to "'আল্লাহু আকবার' বলে জমিনে সিজদায় যাওয়া এবং ৩ বার 'সুবহানা রাব্বিয়াল আ’লা' পাঠ করা। দুই সিজদার মাঝে বসে 'আল্লাহুম্মাগফিরলী' দোয়া পড়া।",
        "৫. তাশাহহুদ, দরূদ ও সালাম" to "শেষ বৈঠকে বসে তাশাহহুদ (আত্তাহিয়্যাতু), দরূদে ইব্রাহীম, দোআয়ে মাসূরা পাঠ শেষে ডান ও বাম দিকে 'আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ' বলে সালাত সমাপ্ত করা।"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            color = if (isDark) Color(0xFF041910) else MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
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
                                imageVector = Icons.Default.Mosque,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "নামাজ গাইড ও নিয়মাবলী",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "৫ ওয়াক্ত সালাতের সঠিক রাকাত, নিয়ম ও ধারাবাহিক পদ্ধতি",
                            fontSize = 11.5.sp,
                            color = if (isDark) Color(0xFFCBD5E1) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Text(
                            text = "ওয়াক্ত অনুযায়ী রাকাতের হিসাব",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) IslamicGold else MaterialTheme.colorScheme.primary
                        )
                    }

                    items(rakatList) { rakat ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isDark) Color(0xFF0A261B) else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, if (isDark) Color(0x334ADE80) else Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = rakat.nameBn,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = IslamicGold.copy(alpha = 0.15f),
                                        border = BorderStroke(0.8.dp, IslamicGold.copy(alpha = 0.5f))
                                    ) {
                                        Text(
                                            text = rakat.totalRakatBn,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDark) IslamicGold else Color(0xFF92400E),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = rakat.breakdownBn,
                                    fontSize = 13.sp,
                                    color = if (isDark) Color(0xFF93C5FD) else Color(0xFF1D4ED8),
                                    fontWeight = FontWeight.Medium
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "সময়সীমা: ${rakat.timeInfoBn}",
                                    fontSize = 11.5.sp,
                                    color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "সালাতের মূল ৫টি ধাপ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) IslamicGold else MaterialTheme.colorScheme.primary
                        )
                    }

                    items(stepGuides) { (title, desc) ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isDark) Color(0xFF081F16) else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, Color(0x33CBD5E1)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = IslamicGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = desc,
                                        fontSize = 12.5.sp,
                                        color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569),
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
