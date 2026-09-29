package com.example.util

import androidx.compose.foundation.lazy.LazyListState

object ReadingProgressHelper {

    /**
     * Calculates the reading advancement fraction (0.0f .. 1.0f) for a LazyColumn article / list.
     * 
     * Key characteristics:
     * - Returns 0.0f when the reader is at the start (top) of the article.
     * - Returns 1.0f (100% complete) when the reader has scrolled to the bottom (e.g. read 15/15 items).
     * - Smoothly increases with every single pixel scrolled down.
     * - Smoothly decreases with every single pixel scrolled up.
     * - Immune to pinned sticky headers that would otherwise pin visibleItemsInfo.first() at offset 0.
     */
    fun calculateReadingProgress(listState: LazyListState): Float {
        val layoutInfo = listState.layoutInfo
        val totalItems = layoutInfo.totalItemsCount
        if (totalItems <= 1 || (!listState.canScrollBackward && !listState.canScrollForward)) {
            return 1f
        }

        // 1. Boundary check: Scrolled to the very end of the article
        if (!listState.canScrollForward) {
            return 1f
        }

        // 2. Boundary check: At the very top before any scroll
        if (!listState.canScrollBackward && listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0) {
            return 0f
        }

        val visibleItems = layoutInfo.visibleItemsInfo
        if (visibleItems.isEmpty()) return 0f

        val viewportStart = layoutInfo.viewportStartOffset
        val viewportEnd = layoutInfo.viewportEndOffset
        val viewportHeight = (viewportEnd - viewportStart).toFloat()
        if (viewportHeight <= 0f) return 0f

        // 3. Check if the final item is within the viewport
        val lastVisibleItem = visibleItems.maxByOrNull { it.index }
        if (lastVisibleItem != null && lastVisibleItem.index >= totalItems - 1) {
            val lastItemBottom = lastVisibleItem.offset + lastVisibleItem.size
            if (lastItemBottom <= viewportEnd + 48) { // within 48px of bottom or overscrolled
                return 1f
            }
        }

        // 4. Continuous pixel-level scroll progress:
        // Calculate the average item height across currently visible items to determine scroll ratio
        val averageItemHeight = visibleItems.map { it.size }.average().toFloat().coerceAtLeast(1f)
        val totalEstimatedContentHeight = totalItems * averageItemHeight
        val maxScrollOffset = (totalEstimatedContentHeight - viewportHeight).coerceAtLeast(1f)
        val currentScrollOffset = (listState.firstVisibleItemIndex * averageItemHeight + listState.firstVisibleItemScrollOffset).coerceAtLeast(0f)
        val continuousScrollProgress = (currentScrollOffset / maxScrollOffset).coerceIn(0f, 1f)

        // 5. Revealed cards advancement:
        // By using maxByOrNull { it.index }, pinned sticky headers (which stay at top with low indices)
        // never skew the calculation.
        val itemProgress = if (lastVisibleItem != null) {
            val itemTop = lastVisibleItem.offset
            val itemSize = lastVisibleItem.size.coerceAtLeast(1)
            val fractionInsideViewport = ((viewportEnd - itemTop).toFloat() / itemSize.toFloat()).coerceIn(0f, 1f)
            val furthestReached = lastVisibleItem.index.toFloat() + fractionInsideViewport
            (furthestReached / totalItems.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }

        // 6. Blend scroll progress and furthest revealed item for ultra-smooth responsiveness
        val blended = maxOf(continuousScrollProgress, itemProgress * 0.95f)
        return blended.coerceIn(0f, 1f)
    }
}
