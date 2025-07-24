package com.example.todoktodok.presentation.view.discussion.create.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.Note

val notesDiffUtil =
    object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(
            oldItem: Note,
            newItem: Note,
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Note,
            newItem: Note,
        ): Boolean = oldItem == newItem
    }
