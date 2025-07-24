package com.example.todoktodok.presentation.view.note.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.todoktodok.presentation.view.library.booksDiffUtil
import com.example.todoktodok.presentation.view.note.adapter.MyLibraryBooksViewHolder.Companion.MyLibraryBooksViewHolder
import com.example.todoktodok.state.BookState

class MyLibraryBooksAdapter(
    private val handler: Handler,
) : ListAdapter<BookState, MyLibraryBooksViewHolder>(booksDiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyLibraryBooksViewHolder = MyLibraryBooksViewHolder(parent, handler)

    override fun onBindViewHolder(
        holder: MyLibraryBooksViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    interface Handler : MyLibraryBooksViewHolder.Handler
}
