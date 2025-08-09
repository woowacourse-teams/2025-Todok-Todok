package com.team.todoktodok.presentation.view.discussions.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team.domain.model.Discussion
import com.team.todoktodok.databinding.ItemDiscussionBinding
import com.team.todoktodok.presentation.core.HighlighterSpan

class DiscussionViewHolder private constructor(
    private val binding: ItemDiscussionBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            handler.onItemClick(bindingAdapterPosition)
        }
    }

    fun bind(
        item: Discussion,
        keyword: String?,
    ) {
        with(binding) {
            tvBookTitle.text = item.book.title
            tvDiscussionTitle.text = item.discussionTitle
            tvDiscussionContent.text = item.discussionOpinion

            keyword?.let {
                textHighlighting(tvBookTitle, it)
                textHighlighting(tvDiscussionTitle, it)
                textHighlighting(tvDiscussionContent, it)
            }

            Glide
                .with(root.context)
                .load(item.book.image)
                .into(ivBook)
        }
    }

    private fun textHighlighting(
        view: TextView,
        keyword: String,
    ) {
        val text = view.text.toString()
        if (keyword.isBlank()) return

        val highlighterColors =
            listOf(
                Color.argb(120, 255, 255, 0), // 노랑
                Color.argb(120, 0, 255, 255), // 하늘색
                Color.argb(120, 144, 238, 144), // 연두색
                Color.argb(120, 221, 160, 221), // 보라
            )

        val spannable = SpannableString(text)
        var startIndex = text.indexOf(keyword, ignoreCase = true)

        while (startIndex != -1) {
            val endIndex = startIndex + keyword.length
            val randomColor = highlighterColors.random()

            spannable.setSpan(
                HighlighterSpan(randomColor),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )

            startIndex = text.indexOf(keyword, startIndex + keyword.length, ignoreCase = true)
        }

        view.text = spannable
    }

    companion object {
        fun DiscussionViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): DiscussionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemDiscussionBinding.inflate(inflater, parent, false)
            return DiscussionViewHolder(binding, handler)
        }
    }

    interface Handler {
        fun onItemClick(index: Int)
    }
}
