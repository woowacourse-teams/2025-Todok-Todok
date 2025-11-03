package com.team.todoktodok.presentation.xml.book.adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ItemSearchedBooksCountBinding

class SearchBooksCountViewHolder private constructor(
    val binding: ItemSearchedBooksCountBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(size: Int) {
        val context = binding.root.context
        val text = context.getString(R.string.discussion_search_result_count, size)

        val spannable = SpannableString(text)

        val start = text.indexOf(size.toString())
        val end = start + size.toString().length

        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.green_1A)),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
        )

        spannable.setSpan(
            StyleSpan(android.graphics.Typeface.BOLD),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
        )

        binding.tvSearchedBooksCount.text = spannable
    }

    companion object {
        fun SearchBooksCountViewHolder(parent: ViewGroup): SearchBooksCountViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemSearchedBooksCountBinding.inflate(inflater, parent, false)
            return SearchBooksCountViewHolder(binding)
        }
    }
}
