package com.team.todoktodok.presentation.view.book.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.BookRepository

class SelectBookViewModelFactory(
    private val bookRepository: BookRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelectBookViewModel::class.java)) {
            return SelectBookViewModel(bookRepository) as T
        } else {
            throw IllegalArgumentException("알 수 없는 ViewModel 클래스입니다: ${modelClass.name}")
        }
    }
}
