package com.team.todoktodok.presentation.view.discussion.create.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team.domain.model.Note
import com.team.todoktodok.databinding.ItemNoteBinding

class NoteViewHolder private constructor(
    private val binding: ItemNoteBinding,
    private val onClickListener: NoteAdapter.OnClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(note: Note) {
        with(binding) {
            Glide.with(binding.root.context).load(note.book.image).into(ivBookImage)
            tvBookTitle.text = note.book.title
            tvSnapSummary.text = note.snap
            tvMemoSummary.text = note.memo
            root.setOnClickListener {
                onClickListener.selectNote(note)
            }
        }
    }

    companion object {
        fun NoteViewHolder(
            parent: ViewGroup,
            onClickListener: NoteAdapter.OnClickListener,
        ): NoteViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemNoteBinding.inflate(layoutInflater, parent, false)
            return NoteViewHolder(binding, onClickListener)
        }
    }
}
