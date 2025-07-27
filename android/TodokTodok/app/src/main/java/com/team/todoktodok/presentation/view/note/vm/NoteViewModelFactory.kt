package com.team.todoktodok.presentation.view.note.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.BookRepository
import com.team.domain.repository.NoteRepository

class NoteViewModelFactory(
    private val bookRepository: BookRepository,
    private val noteRepository: NoteRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(bookRepository, noteRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
