package com.example.todoktodok.presentation.view.discussion.create.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.model.Note
import com.example.todoktodok.presentation.view.discussion.create.adapter.NoteViewHolder.Companion.NoteViewHolder

class NoteAdapter : ListAdapter<Note, NoteViewHolder>(notesDiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NoteViewHolder = NoteViewHolder(parent)

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position])
    }
}
