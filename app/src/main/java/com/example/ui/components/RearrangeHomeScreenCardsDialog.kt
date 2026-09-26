package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.example.data.model.HomeScreenCardId
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.CalendarHelper
import kotlinx.coroutines.launch

/**
 * Full-screen or Modal Dialog for Rearranging HomeScreen Cards.
 * Supports:
 * 1. Finger drag & drop via Drag Handle or long press.
 * 2. Dedicated Up/Down arrow buttons for precise single-step reordering.
 * 3. Quick Reset to Default button.
 * 4. Immediate state synchronization with ViewModel & DataStore.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RearrangeHomeScreenCardsDialog(
    currentOrder: List<HomeScreenCardId>,
    onSaveOrder: (List<HomeScreenCardId>) -> Unit,
    onResetOrder: () -> Unit,
    onDismiss: () -> Unit
) {
    var itemsList by remember(currentOrder) { mutableStateOf(currentOrder) }
    val isDark = isSystemInDarkTheme()
    val banglaFont = LocalBanglaFontFamily.current

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    // Drag-to-reorder state
    var draggingItemId by remember { mutableStateOf<String?>(null) }
    var dragOffsetY by remember { mutableStateOf(0f) }
    var measuredItemHeightPx by remember { mutableStateOf(0f) }
    val fallbackStepPx = with(density) { 80.dp.toPx() }

    fun handleDragDelta(cardId: String, deltaY: Float) {
        dragOffsetY += deltaY
        val currentIdx = itemsList.indexOfFirst { it.id == cardId }
        if (currentIdx != -1) {
            val step = if (measuredItemHeightPx > 0f) measuredItemHeightPx else fallbackStepPx
            val threshold = step * 0.45f

            if (dragOffsetY > threshold && currentIdx < itemsList.size - 1) {
                val mutable = itemsList.toMutableList()
                val moved = mutable.removeAt(currentIdx)
                mutable.add(currentIdx + 1, moved)
                itemsList = mutable
                dragOffsetY -= step
                onSaveOrder(mutable)
            } else if (dragOffsetY < -threshold && currentIdx > 0) {
                val mutable = itemsList.toMutableList()
                val moved = mutable.removeAt(currentIdx)
                mutable.add(currentIdx - 1, moved)
                itemsList = mutable
                dragOffsetY += step
                onSaveOrder(mutable)
            }

            // Auto-scroll list if dragging near viewport boundaries
            val visibleItems = lazyListState.layoutInfo.visibleItemsInfo
            if (visibleItems.isNotEmpty()) {
                val firstVisible = visibleItems.first().index
                val lastVisible = visibleItems.last().index
                if (deltaY < 0 && currentIdx <= firstVisible + 1) {
                    coroutineScope.launch { lazyListState.scrollBy(deltaY) }
                } else if (deltaY > 0 && currentIdx >= lastVisible - 1) {
                    coroutineScope.launch { lazyListState.scrollBy(deltaY) }
                }
            }
        }
    }

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
            color = if (isDark) Color(0xFF071B12) else Color(0xFFF8FAFC)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Header: Title & Close Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = IslamicGold.copy(alpha = if (isDark) 0.25f else 0.15f),
                            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f)),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.SwapVert,
                                    contentDescription = null,
                                    tint = IslamicGold,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "হোমস্ক্রিন কার্ড পুনর্বিন্যাস",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont,
                                color = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)
                            )
                            Text(
                                text = "Rearrange HomeScreen Cards",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = if (isDark) Color(0x2EFFFFFF) else Color(0xFFE2E8F0),
                        modifier = Modifier.size(36.dp)
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "বন্ধ করুন",
                                tint = if (isDark) Color(0xFFE2E8F0) else Color(0xFF334155),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Subtitle Instruction and Reset Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "আঙুল দিয়ে টেনে বা ⬆ ⬇ অ্যারো চেপে সাজান",
                        fontSize = 12.sp,
                        fontFamily = banglaFont,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedButton(
                        onClick = {
                            val def = HomeScreenCardId.defaultList
                            itemsList = def
                            onResetOrder()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "রিসেট",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "ডিফল্ট ক্রম",
                            fontSize = 11.5.sp,
                            fontFamily = banglaFont,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // List of Reorderable Cards
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(9.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    itemsIndexed(
                        items = itemsList,
                        key = { _, card -> card.id }
                    ) { index, card ->
                        val isDragging = draggingItemId == card.id

                        RearrangeCardItem(
                            card = card,
                            index = index,
                            totalCount = itemsList.size,
                            isDark = isDark,
                            isDragging = isDragging,
                            dragOffsetY = if (isDragging) dragOffsetY else 0f,
                            onMeasureHeight = { heightPx -> measuredItemHeightPx = heightPx },
                            onDragStart = {
                                draggingItemId = card.id
                                dragOffsetY = 0f
                            },
                            onDragDelta = { deltaY ->
                                handleDragDelta(card.id, deltaY)
                            },
                            onDragEnd = {
                                draggingItemId = null
                                dragOffsetY = 0f
                            },
                            onMoveUp = {
                                if (index > 0) {
                                    val mutable = itemsList.toMutableList()
                                    val moved = mutable.removeAt(index)
                                    mutable.add(index - 1, moved)
                                    itemsList = mutable
                                    onSaveOrder(mutable)
                                }
                            },
                            onMoveDown = {
                                if (index < itemsList.size - 1) {
                                    val mutable = itemsList.toMutableList()
                                    val moved = mutable.removeAt(index)
                                    mutable.add(index + 1, moved)
                                    itemsList = mutable
                                    onSaveOrder(mutable)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RearrangeCardItem(
    card: HomeScreenCardId,
    index: Int,
    totalCount: Int,
    isDark: Boolean,
    isDragging: Boolean,
    dragOffsetY: Float,
    onMeasureHeight: (Float) -> Unit,
    onDragStart: () -> Unit,
    onDragDelta: (Float) -> Unit,
    onDragEnd: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isDragging) {
            if (isDark) Color(0xFF1B3D2F) else Color(0xFFE2F7ED)
        } else {
            if (isDark) Color(0xFF10281E) else Color(0xFFEEF3F6)
        },
        border = BorderStroke(
            if (isDragging) 1.8.dp else 1.dp,
            if (isDragging) IslamicGold else (if (isDark) Color(0x334ADE80) else Color(0xFFE2E8F0))
        ),
        shadowElevation = if (isDragging) 16.dp else 0.5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(if (isDragging) 10f else 1f)
            .graphicsLayer {
                translationY = dragOffsetY
                scaleX = if (isDragging) 1.02f else 1f
                scaleY = if (isDragging) 1.02f else 1f
            }
            .onGloballyPositioned { coords ->
                onMeasureHeight(coords.size.height.toFloat())
            }
            .pointerInput(card.id) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { onDragStart() },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDragDelta(dragAmount.y)
                    },
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragEnd() }
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Number & Icon
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = card.iconColor.copy(alpha = if (isDark) 0.22f else 0.12f),
                    border = BorderStroke(1.dp, card.iconColor.copy(alpha = if (isDark) 0.5f else 0.35f)),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = card.icon,
                            contentDescription = card.titleBn,
                            tint = card.iconColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                // Serial Number Badge
                Surface(
                    shape = RoundedCornerShape(5.dp),
                    color = if (isDark) Color(0xFF0F172A) else Color(0xFFF1F5F9),
                    border = BorderStroke(0.8.dp, IslamicGold.copy(alpha = 0.7f)),
                    modifier = Modifier.padding(end = 1.dp, bottom = 1.dp)
                ) {
                    Text(
                        text = CalendarHelper.toBanglaNumber(index + 1),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) IslamicGold else Color(0xFF92400E),
                        modifier = Modifier.padding(horizontal = 3.5.dp, vertical = 0.5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Title & Subtitle
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = card.titleBn,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont,
                    color = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = card.subtitleBn,
                    fontSize = 11.sp,
                    fontFamily = banglaFont,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Action Controls: Drag Handle (=) + Up Arrow (^) + Down Arrow (v)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Drag Handle
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDragging) IslamicGold.copy(alpha = 0.25f) else Color.Transparent)
                        .pointerInput(card.id) {
                            detectDragGestures(
                                onDragStart = { onDragStart() },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    onDragDelta(dragAmount.y)
                                },
                                onDragEnd = { onDragEnd() },
                                onDragCancel = { onDragEnd() }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DragHandle,
                        contentDescription = "টেনে সাজান",
                        tint = if (isDragging) IslamicGold else (if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Up Arrow
                IconButton(
                    onClick = onMoveUp,
                    enabled = index > 0,
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "উপরে নিন",
                        tint = if (index > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Down Arrow
                IconButton(
                    onClick = onMoveDown,
                    enabled = index < totalCount - 1,
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "নিচে নিন",
                        tint = if (index < totalCount - 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
