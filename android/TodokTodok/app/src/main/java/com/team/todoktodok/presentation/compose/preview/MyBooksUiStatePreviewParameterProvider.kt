package com.team.todoktodok.presentation.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.common.collect.ImmutableList
import com.team.domain.model.Book
import com.team.todoktodok.presentation.compose.my.books.MyBooksUiModel

class MyBooksUiStatePreviewParameterProvider : PreviewParameterProvider<MyBooksUiModel> {
    override val values: Sequence<MyBooksUiModel>
        get() =
            sequenceOf(
                MyBooksUiModel(
                    books =
                        ImmutableList.of(
                            Book(
                                id = 1,
                                title = "Effective Kotlin - Best Practices",
                                author = "Marcin Moskala (JetBrains)",
                                image =
                                    "https://search2.kakaocdn.net/thumb/R120x174.q85/" +
                                        "?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038",
                                "",
                                "",
                            ),
                            Book(
                                id = 2,
                                title = "Clean Code - A Handbook of Agile Software Craftsmanship",
                                author = "Robert C. Martin (Uncle Bob)",
                                image =
                                    "https://search2.kakaocdn.net/thumb/R120x174.q85/" +
                                        "?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038",
                                "",
                                "",
                            ),
                            Book(
                                id = 3,
                                title = "Android Programming - The Big Nerd Ranch Guide",
                                author = "Bill Phillips (Big Nerd Ranch)",
                                image =
                                    "https://search2.kakaocdn.net/thumb/R120x174.q85/" +
                                        "?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038",
                                "",
                                "",
                            ),
                            Book(
                                id = 4,
                                title = "Kotlin in Action",
                                author = "Dmitry Jemerov (JetBrains)",
                                image =
                                    "https://search2.kakaocdn.net/thumb/R120x174.q85/" +
                                        "?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038",
                                "",
                                "",
                            ),
                            Book(
                                id = 5,
                                title = "Refactoring - Improving the Design of Existing Code",
                                author = "Martin Fowler (ThoughtWorks)",
                                image =
                                    "https://search2.kakaocdn.net/thumb/R120x174.q85/" +
                                        "?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038",
                                "",
                                "",
                            ),
                            Book(
                                id = 6,
                                title = "Design Patterns - Elements of Reusable Object-Oriented Software",
                                author = "Erich Gamma (GoF)",
                                image =
                                    "https://search2.kakaocdn.net/thumb/R120x174.q85/" +
                                        "?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038",
                                "",
                                "",
                            ),
                            Book(
                                id = 7,
                                title = "You Don’t Know JS - ES6 & Beyond",
                                author = "Kyle Simpson (O’Reilly)",
                                image =
                                    "https://search2.kakaocdn.net/thumb/R120x174.q85/" +
                                        "?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038",
                                "",
                                "",
                            ),
                            Book(
                                id = 8,
                                title = "The Pragmatic Programmer - 20th Anniversary Edition",
                                author = "Andy Hunt (PragProg)",
                                image =
                                    "https://search2.kakaocdn.net/thumb/R120x174.q85/" +
                                        "?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038",
                                "",
                                "",
                            ),
                            Book(
                                id = 9,
                                title = "Head First Design Patterns",
                                author = "Eric Freeman (O’Reilly)",
                                image =
                                    "https://search2.kakaocdn.net/thumb/R120x174.q85/" +
                                        "?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038",
                                "",
                                "",
                            ),
                            Book(
                                id = 10,
                                title = "Test-Driven Development - By Example",
                                author = "Kent Beck (XP)",
                                image =
                                    "https://search2.kakaocdn.net/thumb/R120x174.q85/" +
                                        "?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038",
                                "",
                                "",
                            ),
                        ),
                ),
            )
}
