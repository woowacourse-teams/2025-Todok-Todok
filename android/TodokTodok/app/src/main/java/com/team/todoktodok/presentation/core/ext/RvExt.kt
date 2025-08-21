package com.team.todoktodok.presentation.core.ext

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.R

private var RecyclerView.scrollEndListener: RecyclerView.OnScrollListener?
    get() = getTag(R.id.scroll_listener_tag) as? RecyclerView.OnScrollListener
    set(value) {
        setTag(R.id.scroll_listener_tag, value)
    }

fun RecyclerView.addOnScrollEndListener(
    threshold: Int = 3,
    callback: () -> Unit,
) {
    scrollEndListener?.let { removeOnScrollListener(it) }

    val listener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int,
            ) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.hasLessItemThan(threshold)) {
                    callback()
                }
            }

            private fun RecyclerView.hasLessItemThan(threshold: Int): Boolean {
                if (isLayoutRequested) return false
                (layoutManager as? LinearLayoutManager)?.let {
                    val lastVisibleItem = it.findLastVisibleItemPosition()
                    return lastVisibleItem >= it.itemCount - threshold
                }
                return false
            }
        }

    addOnScrollListener(listener)
    scrollEndListener = listener
}

fun RecyclerView.clearOnScrollEndListener() {
    scrollEndListener?.let { removeOnScrollListener(it) }
    scrollEndListener = null
}
