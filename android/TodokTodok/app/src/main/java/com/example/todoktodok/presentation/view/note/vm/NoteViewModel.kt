package com.example.todoktodok.presentation.view.note.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Book
import com.example.domain.repository.BookRepository
import com.example.todoktodok.presentation.core.event.MutableSingleLiveData
import com.example.todoktodok.presentation.core.event.SingleLiveData
import com.example.todoktodok.presentation.view.note.NoteUiEvent
import com.example.todoktodok.presentation.view.serialization.SerializationBook
import kotlinx.coroutines.launch

class NoteViewModel(
    private val defaultBookRepository: BookRepository,
) : ViewModel() {
    private val _uiEvent: MutableSingleLiveData<NoteUiEvent> = MutableSingleLiveData()
    val uiEvent: SingleLiveData<NoteUiEvent> get() = _uiEvent

    fun loadBooks() {
        viewModelScope.launch {
            val books = mapToParcelableBook(defaultBookRepository.getBooks())
            onUiEvent(NoteUiEvent.ShowOwnBooks(books))
        }
    }

    private fun mapToParcelableBook(books: List<Book>): List<SerializationBook> =
        books.map {
            SerializationBook(
                id = it.id,
                title = it.title,
                author = it.author,
                image = it.image,
            )
        }

    private fun onUiEvent(event: NoteUiEvent) {
        _uiEvent.setValue(event)
    }
}
