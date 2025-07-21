package com.example.todoktodok.presentation.view.note.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.repository.BookRepository

class NoteViewModelFactory(
    private val bookRepository: BookRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(bookRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
