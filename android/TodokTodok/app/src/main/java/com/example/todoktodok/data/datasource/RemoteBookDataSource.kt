package com.example.todoktodok.data.datasource

import com.example.domain.model.Book

class RemoteBookDataSource : BookDataSource {
    override suspend fun fetchBooks(): List<Book> =
        listOf(
            Book(
                id = 1L,
                title = "클린 코드",
                author = "로버트 C. 마틴",
                image = "https://image.aladin.co.kr/product/3408/36/cover200/8966260950_2.jpg",
            ),
            Book(
                id = 2L,
                title = "이펙티브 자바",
                author = "조슈아 블로크",
                image = "https://image.aladin.co.kr/product/17119/64/cover200/8966262287_1.jpg",
            ),
            Book(
                id = 3L,
                title = "자바의 정석",
                author = "남궁성",
                image = "https://image.aladin.co.kr/product/36680/21/cover200/8994492003_1.jpg",
            ),
            Book(
                id = 4L,
                title = "모던 자바 인 액션",
                author = "라울-게이브리얼 우르마 외",
                image = "\thttps://image.aladin.co.kr/product/20006/92/cover200/k042635140_1.jpg",
            ),
            Book(
                id = 5L,
                title = "오브젝트",
                author = "조영호",
                image = "https://image.aladin.co.kr/product/19368/10/cover200/k972635015_1.jpg",
            ),
            Book(
                id = 6L,
                title = "코틀린 인 액션",
                author = "드미트리 예브게니에프, 스베틀라나 이사코바",
                image = "https://image.aladin.co.kr/product/35809/98/cover200/k202036651_1.jpg",
            ),
            Book(
                id = 7L,
                title = "디자인 패턴",
                author = "에리히 감마 외",
                image = "https://image.aladin.co.kr/product/5605/15/cover500/6000835492_1.jpg",
            ),
            Book(
                id = 8L,
                title = "엘레강트 오브젝트",
                author = "야코브 지걸",
                image = "https://image.aladin.co.kr/product/25837/40/cover500/k762736538_1.jpg",
            ),
            Book(
                id = 9L,
                title = "토비의 스프링",
                author = "이일민",
                image = "https://image.aladin.co.kr/product/1950/55/cover500/8960773417_2.jpg",
            ),
            Book(
                id = 10L,
                title = "Do it! 안드로이드 앱 프로그래밍",
                author = "정재곤",
                image = "https://image.aladin.co.kr/product/35641/30/cover500/k122036609_1.jpg",
            ),
        )

    override suspend fun fetchBooks(searchInput: String): List<Book> =
        listOf(
            Book(
                id = 1L,
                title = "클린 코드",
                author = "로버트 C. 마틴",
                image = "https://image.aladin.co.kr/product/3408/36/cover200/8966260950_2.jpg",
            ),
            Book(
                id = 2L,
                title = "이펙티브 자바",
                author = "조슈아 블로크",
                image = "https://image.aladin.co.kr/product/17119/64/cover200/8966262287_1.jpg",
            ),
            Book(
                id = 3L,
                title = "자바의 정석",
                author = "남궁성",
                image = "https://image.aladin.co.kr/product/36680/21/cover200/8994492003_1.jpg",
            ),
            Book(
                id = 4L,
                title = "모던 자바 인 액션",
                author = "라울-게이브리얼 우르마 외",
                image = "\thttps://image.aladin.co.kr/product/20006/92/cover200/k042635140_1.jpg",
            ),
            Book(
                id = 5L,
                title = "오브젝트",
                author = "조영호",
                image = "https://image.aladin.co.kr/product/19368/10/cover200/k972635015_1.jpg",
            ),
            Book(
                id = 6L,
                title = "코틀린 인 액션",
                author = "드미트리 예브게니에프, 스베틀라나 이사코바",
                image = "https://image.aladin.co.kr/product/35809/98/cover200/k202036651_1.jpg",
            ),
            Book(
                id = 7L,
                title = "디자인 패턴",
                author = "에리히 감마 외",
                image = "https://image.aladin.co.kr/product/5605/15/cover500/6000835492_1.jpg",
            ),
            Book(
                id = 8L,
                title = "엘레강트 오브젝트",
                author = "야코브 지걸",
                image = "https://image.aladin.co.kr/product/25837/40/cover500/k762736538_1.jpg",
            ),
            Book(
                id = 9L,
                title = "토비의 스프링",
                author = "이일민",
                image = "https://image.aladin.co.kr/product/1950/55/cover500/8960773417_2.jpg",
            ),
            Book(
                id = 10L,
                title = "Do it! 안드로이드 앱 프로그래밍",
                author = "정재곤",
                image = "https://image.aladin.co.kr/product/35641/30/cover500/k122036609_1.jpg",
            ),
        )

    override suspend fun saveBook(bookId: Long) {
        TODO("Not yet implemented")
    }
}
