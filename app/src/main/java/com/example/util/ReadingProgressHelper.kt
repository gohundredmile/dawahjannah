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

        // 3. Final item reached check: If the last item or second to last (e.g. trailing spacer) is visible
        val maxVisibleItem = visibleItems.maxByOrNull { it.index }
        if (maxVisibleItem != null && maxVisibleItem.index >= totalItems - 2) {
            if (!listState.canScrollForward) {
                return 1f
            }
            // If the final item's bottom is in or near the viewport
            val lastItemBottom = maxVisibleItem.offset + maxVisibleItem.size
            if (lastItemBottom <= viewportEnd + 120) {
                return 1f
            }
        }

        // 4. Continuous pixel-accurate scroll progress:
        // Measure where the top of the viewport is relative to the scrollable range
        val maxScrollableItems = (totalItems - 1).coerceAtLeast(1)
        val firstItem = visibleItems.firstOrNull { it.index == listState.firstVisibleItemIndex }
        val firstItemHeight = (firstItem?.size ?: visibleItems.firstOrNull()?.size ?: 1).coerceAtLeast(1)
        val firstItemFraction = (listState.firstVisibleItemScrollOffset.toFloat() / firstItemHeight.toFloat()).coerceIn(0f, 1f)
        val continuousScrolledItems = listState.firstVisibleItemIndex.toFloat() + firstItemFraction
        val scrollProgress = (continuousScrolledItems / maxScrollableItems.toFloat()).coerceIn(0f, 1f)

        // 5. Furthest item visible in viewport:
        val furthestProgress = if (maxVisibleItem != null) {
            val itemTop = maxVisibleItem.offset
            val itemSize = maxVisibleItem.size.coerceAtLeast(1)
            val fractionInsideViewport = ((viewportEnd - itemTop).toFloat() / itemSize.toFloat()).coerceIn(0f, 1f)
            val furthestIndex = maxVisibleItem.index.toFloat() + fractionInsideViewport
            (furthestIndex / totalItems.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }

        // 6. Blend: Ensure immediate, smooth reactivity on every pixel scroll
        val blended = maxOf(scrollProgress, furthestProgress * 0.96f)
        return blended.coerceIn(0f, 1f)
    }
}
