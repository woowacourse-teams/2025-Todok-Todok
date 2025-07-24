package com.example.todoktodok.presentation.view.discussion.create.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.model.Note
import com.example.todoktodok.presentation.view.discussion.create.adapter.NoteViewHolder.Companion.NoteViewHolder

class NoteAdapter(
    private val onClickListener: OnClickListener,
) : ListAdapter<Note, NoteViewHolder>(notesDiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NoteViewHolder = NoteViewHolder(parent, onClickListener)

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position])
    }

    fun interface OnClickListener {
        fun selectNote(note: Note)
    }
}
