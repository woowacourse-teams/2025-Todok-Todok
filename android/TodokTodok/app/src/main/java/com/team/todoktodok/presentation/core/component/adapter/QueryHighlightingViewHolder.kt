package com.team.todoktodok.presentation.core.component.adapter

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ItemDiscussionBinding
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState

class QueryHighlightingViewHolder private constructor(
    binding: ItemDiscussionBinding,
    handler: Handler,
) : BaseDiscussionViewHolder(binding, handler) {
    override fun bindExtra(item: DiscussionUiState) {
        val keyword = item.searchKeyword
        val contextText = extractContext(item.discussionTitle, keyword, 10)
        val spannable = highlightKeyword(keyword, contextText)
        binding.tvDiscussionTitle.text = spannable
    }

    private fun highlightKeyword(
        keyword: String,
        text: String,
    ): SpannableString {
        val spannable = SpannableString(text)
        var startIndex = text.indexOf(keyword, ignoreCase = true)

        while (startIndex >= 0) {
            val endIndex = startIndex + keyword.length
            spannable.setSpan(
                ForegroundColorSpan(itemView.context.getColor(R.color.green_1A)),
                startIndex,
                endIndex,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            startIndex = text.indexOf(keyword, startIndex + 1, ignoreCase = true)
        }

        return spannable
    }

    private fun extractContext(
        discussionTitle: String,
        searchKeyword: String,
        contextLength: Int,
    ): String {
        val firstIndex = discussionTitle.indexOf(searchKeyword, ignoreCase = true)
        if (firstIndex < 0) return discussionTitle

        val start = (firstIndex - contextLength).coerceAtLeast(0)
        val end =
            (firstIndex + searchKeyword.length + contextLength).coerceAtMost(discussionTitle.length)

        return discussionTitle.substring(start, end)
    }

    companion object {
        fun QueryHighlightingViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): QueryHighlightingViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemDiscussionBinding.inflate(inflater, parent, false)
            return QueryHighlightingViewHolder(binding, handler)
        }
    }
}
