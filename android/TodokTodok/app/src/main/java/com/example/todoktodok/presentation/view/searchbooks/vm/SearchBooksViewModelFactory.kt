package com.example.todoktodok.presentation.view.searchbooks.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.repository.BookRepository

class SearchBooksViewModelFactory(
    private val bookRepository: BookRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchBooksViewModel::class.java)) {
            return SearchBooksViewModel(bookRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
