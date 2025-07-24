package com.example.todoktodok.presentation.view.library.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Books
import com.example.domain.repository.BookRepository
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val bookRepository: BookRepository,
) : ViewModel() {
    private val _books: MutableLiveData<Books> = MutableLiveData(Books(emptyList()))
    val books: LiveData<Books> get() = _books

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _books.value = bookRepository.getBooks()
        }
    }
}
