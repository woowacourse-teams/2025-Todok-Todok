package com.team.todoktodok.presentation.core.ext

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE

fun RecyclerView.addOnScrollEndListener(
    threshold: Int = 3,
    callback: () -> Unit,
) {
    addOnScrollListener(
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int,
            ) {
                if (newState == SCROLL_STATE_IDLE && recyclerView.hasLessItemThan(threshold)) {
                    callback()
                }
            }

            private fun RecyclerView.hasLessItemThan(threshold: Int): Boolean {
                if (isLayoutRequested) {
                    return false
                }
                (layoutManager as? LinearLayoutManager)?.let {
                    val lastVisibleItem = it.findLastVisibleItemPosition()
                    return lastVisibleItem >= it.itemCount - threshold
                }
                return false
            }
        },
    )
}
