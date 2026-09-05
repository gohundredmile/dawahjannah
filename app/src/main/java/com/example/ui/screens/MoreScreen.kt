package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DawahTopAppBar
import com.example.ui.screens.sub.AsmaulHusnaScreen
import com.example.ui.screens.sub.DuroodScreen
import com.example.ui.screens.sub.HealthDuaScreen
import com.example.ui.screens.sub.ScratchpadScreen
import com.example.ui.screens.sub.SettingsScreen
import com.example.ui.screens.sub.TasbihScreen
import com.example.ui.theme.IslamicGold
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.MoreSubScreen

@Composable
fun MoreScreen(
    viewModel: MainViewModel,
    contentPadding: PaddingValues
) {
    val currentSubScreen by viewModel.moreSubScreen.collectAsState()

    if (currentSubScreen != MoreSubScreen.MAIN) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            DawahTopAppBar(
                title = currentSubScreen.titleBn,
                canNavigateBack = true,
                onNavigateBack = { viewModel.navigateBackToMore() }
            )

            when (currentSubScreen) {
                MoreSubScreen.NAMES_OF_ALLAH -> AsmaulHusnaScreen(viewModel = viewModel)
                MoreSubScreen.TASBIH -> TasbihScreen(viewModel = viewModel)
                MoreSubScreen.DUROOD_ISTIGHFAR -> DuroodScreen(viewModel = viewModel)
                MoreSubScreen.HEALTH_DUAS -> HealthDuaScreen(viewModel = viewModel)
                MoreSubScreen.SCRATCHPAD -> ScratchpadScreen(viewModel = viewModel)
                MoreSubScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
                MoreSubScreen.MAIN -> {}
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = "ইসলামিক জীবন ও বিশেষ অধ্যায়সমূহ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "আমল বৃদ্ধি ও আধ্যাত্মিক উন্নতির বিশেষ মাধ্যমসমূহ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            item {
                MoreMenuItem(
                    title = "আসমাউল হুসনা (আল্লাহর ৯৯ নাম)",
                    subtitle = "অর্থ, উচ্চারণ ও গভীর আত্মিক প্রতিফলন",
                    icon = Icons.Default.Star,
                    iconTint = IslamicGold,
                    onClick = { viewModel.navigateToMoreSubScreen(MoreSubScreen.NAMES_OF_ALLAH) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                MoreMenuItem(
                    title = "ডিজিটাল তাসবীহ ও জিকির",
                    subtitle = "হ্যাপটিক ভাইব্রেশন, কাউন্ট গোল ও আনলিমিটেড মোড",
                    icon = Icons.Default.TouchApp,
                    iconTint = MaterialTheme.colorScheme.primary,
                    onClick = { viewModel.navigateToMoreSubScreen(MoreSubScreen.TASBIH) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                MoreMenuItem(
                    title = "দরূদ শরীফ ও সাইয়্যিদুল ইস্তিগফার",
                    subtitle = "দরূদে ইবরাহীম, নারিয়া ও তওবার শানে নুযুল ও ফজিলত",
                    icon = Icons.Default.MenuBook,
                    iconTint = MaterialTheme.colorScheme.secondary,
                    onClick = { viewModel.navigateToMoreSubScreen(MoreSubScreen.DUROOD_ISTIGHFAR) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                MoreMenuItem(
                    title = "শারীরিক ও মানসিক রোগের দোয়া (শিফা)",
                    subtitle = "উদ্বেগ, বিষণ্নতা, ঋণমুক্তি, বদনজর ও সুরক্ষার রুকইয়াহ",
                    icon = Icons.Default.Healing,
                    iconTint = Color(0xFF059669),
                    onClick = { viewModel.navigateToMoreSubScreen(MoreSubScreen.HEALTH_DUAS) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                MoreMenuItem(
                    title = "ব্যক্তিগত খাস দোয়া ও মুহাসাবা জার্নাল",
                    subtitle = "১০০% অফলাইনে ব্যক্তিগত দোয়া ও আত্মসমালোচনা সংরক্ষণ",
                    icon = Icons.Default.EditNote,
                    iconTint = Color(0xFF7C3AED),
                    onClick = { viewModel.navigateToMoreSubScreen(MoreSubScreen.SCRATCHPAD) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                MoreMenuItem(
                    title = "সেটিংস ও অরোরা থিম",
                    subtitle = "৫টি কাস্টম থিম, ডার্ক মোড, ফন্ট সাইজ ও পুশ আপডেট",
                    icon = Icons.Default.Palette,
                    iconTint = MaterialTheme.colorScheme.primary,
                    onClick = { viewModel.navigateToMoreSubScreen(MoreSubScreen.SETTINGS) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun MoreMenuItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = iconTint.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
