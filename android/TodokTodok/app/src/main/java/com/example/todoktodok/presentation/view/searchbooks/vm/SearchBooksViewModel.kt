package com.example.todoktodok.presentation.view.searchbooks.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.Book

class SearchBooksViewModel : ViewModel() {
    private val _books: MutableLiveData<List<Book>> = MutableLiveData(emptyList())
    val books: LiveData<List<Book>> get() = _books
}
