package com.example.todoktodok.presentation.view.library.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Book
import com.example.domain.repository.BookRepository
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val bookRepositoryImpl: BookRepository,
) : ViewModel() {
    val _books: MutableLiveData<List<Book>> = MutableLiveData()
    val books: LiveData<List<Book>> = _books

    init {
        viewModelScope.launch {
            getBooks()
        }
    }

    private suspend fun getBooks() {
        _books.value = bookRepositoryImpl.getBooks()
    }
}
