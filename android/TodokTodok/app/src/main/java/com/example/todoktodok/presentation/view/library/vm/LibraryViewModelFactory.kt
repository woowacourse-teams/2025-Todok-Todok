package com.example.todoktodok.presentation.view.library.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.repository.BookRepository
import kotlin.jvm.java

class LibraryViewModelFactory(
    private val bookRepository: BookRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            return LibraryViewModel(bookRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
