package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IslamicGold
import com.example.util.CalendarHelper

/**
 * Data representation for an app feature.
 */
data class HomeFeatureItem(
    val id: String,
    val serialNumberBn: String,
    val titleBn: String,
    val shortTitleBn: String,
    val subtitleBn: String,
    val categoryBn: String,
    val icon: ImageVector,
    val iconColor: Color,
    val isTopEight: Boolean = false,
    val onClickAction: () -> Unit
)

/**
 * Extension property to retrieve the clean feature title without numeric prefixes (e.g. "১. তাসবিহ" -> "তাসবিহ").
 */
val HomeFeatureItem.cleanTitleBn: String
    get() {
        val raw = this.titleBn.trim()
        val regex = Regex("^[০-৯0-9]+[.\\-\\s]+\\s*")
        return raw.replace(regex, "").ifBlank { this.shortTitleBn }
    }

/**
 * Renumbers a list of features dynamically according to their current sequence:
 * - serialNumberBn becomes "০১", "০২", "০৩" ...
 * - titleBn becomes "১. [ফিচারের নাম]", "২. [ফিচারের নাম]" ...
 */
fun List<HomeFeatureItem>.renumberedFeatures(): List<HomeFeatureItem> {
    return this.mapIndexed { index, item ->
        val pos = index + 1
        val numBn = com.example.util.CalendarHelper.toBanglaNumber(pos)
        val serialBn = if (pos < 10) "০$numBn" else numBn
        item.copy(
            serialNumberBn = serialBn,
            titleBn = "$numBn. ${item.cleanTitleBn}"
        )
    }
}

/**
 * 'টপ ফিচার' (Top Features) section on the Home Screen.
 * Displays 12 primary features in three clean rows (4 icons per row)
 * with a stylish 'আরও / More' text affordance in the header
 * that opens the full features window.
 */
@Composable
fun TopFeaturesSection(
    topFeatures: List<HomeFeatureItem>,
    onOpenAllFeatures: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    PureGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(18.dp),
        accentBorderColor = IslamicGold,
        borderWidth = 1.1.dp,
        elevation = 2.dp,
        isDark = isDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 9.dp)
        ) {
            // Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Title + Icon + Tag
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = IslamicGold.copy(alpha = if (isDark) 0.22f else 0.16f),
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.55f)),
                        modifier = Modifier.size(30.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(7.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "টপ ফিচার",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color(0xFFF8FAFC) else Color(0xFF0F172A),
                                fontSize = 15.5.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isDark) Color(0xFF0284C7).copy(alpha = 0.18f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                border = BorderStroke(0.8.dp, if (isDark) Color(0xFF38BDF8).copy(alpha = 0.4f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                            ) {
                                Text(
                                    text = "${CalendarHelper.toBanglaNumber(topFeatures.size)}টি সেবা",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color(0xFF38BDF8) else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.5.dp)
                                )
                            }
                        }
                        Text(
                            text = "দৈনন্দিন জরুরি ইবাদত ও সময়সূচী",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569)
                        )
                    }
                }

                // Right: High-Contrast "More" Action Button
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isDark) Color(0xFF1E293B) else MaterialTheme.colorScheme.primary,
                    border = BorderStroke(1.dp, if (isDark) IslamicGold else IslamicGold.copy(alpha = 0.75f)),
                    shadowElevation = 1.5.dp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onOpenAllFeatures() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.5.dp)
                    ) {
                        Text(
                            text = "More",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "সকল ফিচার",
                            tint = Color.White,
                            modifier = Modifier.size(9.5.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(7.dp))

            // Grid of Feature Icons (Clean 4-column layout for up to 12 items: 3 rows of 4 items)
            // Minimized space between lines for a compact, cohesive, and beautiful appearance
            val chunkSize = 4
            val rows = topFeatures.chunked(chunkSize)

            rows.forEachIndexed { rowIndex, rowItems ->
                if (rowIndex > 0) {
                    Spacer(modifier = Modifier.height(4.5.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowItems.forEach { item ->
                        FeatureIconCell(
                            item = item,
                            isDark = isDark,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    val emptySlots = chunkSize - rowItems.size
                    if (emptySlots > 0) {
                        repeat(emptySlots) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Individual circular/squircle icon cell for the feature grid layout.
 * Designed with compact, elegant proportions: 45dp badge, 22dp icon, 3dp vertical gap,
 * and high-contrast Bengali typography that sits harmoniously between lines.
 */
@Composable
private fun FeatureIconCell(
    item: HomeFeatureItem,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { item.onClickAction() }
            .padding(horizontal = 1.dp, vertical = 0.5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon Badge Container with Frosted Glass Look
        Surface(
            shape = RoundedCornerShape(13.dp),
            color = Color.Transparent,
            border = BorderStroke(
                1.1.dp,
                item.iconColor.copy(alpha = if (isDark) 0.55f else 0.45f)
            ),
            modifier = Modifier.size(45.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                item.iconColor.copy(alpha = if (isDark) 0.30f else 0.20f),
                                item.iconColor.copy(alpha = if (isDark) 0.14f else 0.07f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.titleBn,
                    tint = item.iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        // Title Label with High-Contrast Visibility
        Text(
            text = item.shortTitleBn,
            fontSize = 11.sp,
            lineHeight = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDark) Color(0xFFF8FAFC) else Color(0xFF0F172A),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
