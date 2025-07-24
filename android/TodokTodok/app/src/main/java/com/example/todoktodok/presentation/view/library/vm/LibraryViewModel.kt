package com.example.todoktodok.presentation.view.library.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.BookRepository
import com.example.todoktodok.state.BookState
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val bookRepository: BookRepository,
) : ViewModel() {
    private val _books: MutableLiveData<List<BookState>> = MutableLiveData(emptyList())
    val books: LiveData<List<BookState>> get() = _books

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _books.value =
                bookRepository.getBooks().map { BookState(it.id, it.title, it.author, it.image) }
        }
    }
}
