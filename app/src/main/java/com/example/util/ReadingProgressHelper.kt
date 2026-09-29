package com.example.util

import androidx.compose.foundation.lazy.LazyListState

object ReadingProgressHelper {

    /**
     * Calculates the reading progress fraction (0.0f .. 1.0f) for a LazyColumn.
     * Accurately handles pinned sticky headers, scrolling items, and list boundaries,
     * ensuring progress smoothly increases and decreases with scrolling and reaches 100%
     * when the bottom of the article is reached.
     */
    fun calculateReadingProgress(listState: LazyListState): Float {
        val layoutInfo = listState.layoutInfo
        val totalItems = layoutInfo.totalItemsCount
        if (totalItems <= 1) return 0f

        // When user has scrolled to the very bottom, report complete 100%
        if (!listState.canScrollForward) return 1f
        // When user is at the very top, report 0%
        if (!listState.canScrollBackward) return 0f

        val visibleItems = layoutInfo.visibleItemsInfo
        if (visibleItems.isEmpty()) return 0f

        // Sort visible items by their index in the LazyColumn
        val sortedItems = visibleItems.sortedBy { it.index }

        // Skip any pinned sticky headers that remain in visibleItemsInfo
        // while later items have already scrolled off-screen.
        var scrollingStartIndex = 0
        while (scrollingStartIndex < sortedItems.size - 1 &&
            sortedItems[scrollingStartIndex + 1].index > sortedItems[scrollingStartIndex].index + 1
        ) {
            scrollingStartIndex++
        }

        val topItem = sortedItems[scrollingStartIndex]
        val lastItem = sortedItems.last()

        // Calculate fraction of the top-most scrolling item that has scrolled above view
        val itemFraction = if (topItem.size > 0 && topItem.offset < 0) {
            (-topItem.offset.toFloat() / topItem.size.toFloat()).coerceIn(0f, 1f)
        } else 0f

        val currentPosition = topItem.index.toFloat() + itemFraction
        val maxScrollablePosition = (totalItems - 1).toFloat()
        val rawProgress = (currentPosition / maxScrollablePosition).coerceIn(0f, 1f)

        // Smooth transition near the bottom when the final item is on-screen
        val viewportHeight = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
        val bottomOfLastItem = lastItem.offset + lastItem.size
        val distanceRemaining = bottomOfLastItem - viewportHeight

        return if (lastItem.index >= totalItems - 1) {
            if (distanceRemaining <= 0 || !listState.canScrollForward) {
                1f
            } else {
                val lastItemVisibleFraction = if (lastItem.size > 0) {
                    (1f - (distanceRemaining.toFloat() / lastItem.size.toFloat())).coerceIn(0f, 1f)
                } else 1f
                val blended = rawProgress + (1f - rawProgress) * lastItemVisibleFraction
                blended.coerceIn(0f, 1f)
            }
        } else {
            rawProgress
        }
    }
}
