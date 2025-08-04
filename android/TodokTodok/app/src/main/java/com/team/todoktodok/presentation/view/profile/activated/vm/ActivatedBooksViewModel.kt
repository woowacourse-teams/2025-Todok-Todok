package com.team.todoktodok.presentation.view.profile.activated.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.domain.model.Book
import com.team.domain.model.Books

class ActivatedBooksViewModel : ViewModel() {
    private val _books = MutableLiveData<Books>()
    val books: LiveData<Books> get() = _books

    init {
        _books.value =
            Books(
                listOf(
                    Book(0L, "코틀린 코루틴의 정석", "", ""),
                    Book(1L, "Android Programming: The Big Nerd Ranch Guide", "", ""),
                    Book(2L, "Android Clean Architecture", "", ""),
                    Book(3L, "Kotlin Android 개발 완벽 가이드", "", ""),
                    Book(4L, "Jetpack Compose로 시작하는 안드로이드 UI", "", ""),
                    Book(5L, "안드로이드 마스터북", "", ""),
                    Book(6L, "Android Jetpack 실전 가이드", "", ""),
                    Book(7L, "Effective Android", "", ""),
                    Book(8L, "안드로이드 성능 최적화", "", ""),
                    Book(9L, "모던 안드로이드 앱 개발 with Kotlin", "", ""),
                ),
            )
    }
}
