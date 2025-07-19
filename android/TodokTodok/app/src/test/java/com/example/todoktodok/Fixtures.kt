package com.example.todoktodok

import com.example.domain.model.Book
import com.example.domain.model.DiscussionRoom
import com.example.domain.model.member.Nickname
import com.example.domain.model.member.User
import java.time.LocalDateTime

val DISCUSSION_ROOMS = listOf(
    DiscussionRoom(
        id = 1L,
        discussionTitle = "JPA 성능 최적화",
        book = Book(1L, "자바 ORM 표준 JPA 프로그래밍", "김영한", ""),
        writer = User(1L, Nickname("홍길동")),
        createAt = LocalDateTime.of(2025, 7, 15, 12, 0),
        snap = "왜 해야함",
        discussionOpinion = "fetch join을 남발하면 안됩니다."
    ),
    DiscussionRoom(
        id = 2L,
        discussionTitle = "코틀린 코루틴 완전 정복",
        book = Book(2L, "Kotlin in Action", "Dmitry Jemerov", ""),
        writer = User(2L, Nickname("박코루틴")),
        createAt = LocalDateTime.of(2025, 7, 13, 12, 0),
        snap = "비동기 처리 어떻게?",
        discussionOpinion = "suspend fun과 launch 차이를 이해해야 합니다."
    ),
    DiscussionRoom(
        id = 3L,
        discussionTitle = "MVVM 구조 제대로 이해하기",
        book = Book(3L, "안드로이드 아키텍처 가이드", "구글", ""),
        writer = User(3L, Nickname("김아키텍처")),
        createAt = LocalDateTime.of(2025, 7, 16, 12, 0),
        snap = "ViewModel 왜 씀?",
        discussionOpinion = "UI와 로직을 분리해 유지보수가 쉬워집니다."
    ),
    DiscussionRoom(
        id = 4L,
        discussionTitle = "클린 코드란 무엇인가?",
        book = Book(4L, "Clean Code", "Robert C. Martin", ""),
        writer = User(4L, Nickname("이클린")),
        createAt = LocalDateTime.of(2025, 7, 14, 12, 0),
        snap = "왜 깔끔해야 하나요",
        discussionOpinion = "의도를 드러내는 코드가 중요합니다."
    ),
    DiscussionRoom(
        id = 5L,
        discussionTitle = "디자인 패턴 다시 보기",
        book = Book(5L, "Head First Design Patterns", "Eric Freeman", ""),
        writer = User(5L, Nickname("정디자인")),
        createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
        snap = "싱글턴 또 봐?",
        discussionOpinion = "상황에 맞는 패턴 선택이 중요합니다."
    ),
    DiscussionRoom(
        id = 6L,
        discussionTitle = "Gradle 제대로 쓰기",
        book = Book(6L, "Gradle in Action", "Benjamin Muschko", ""),
        writer = User(6L, Nickname("배그레이들")),
        createAt = LocalDateTime.of(2025, 7, 15, 12, 0),
        snap = "build.gradle 무슨 뜻임?",
        discussionOpinion = "KTS와 Groovy의 차이를 아시나요?"
    ),
    DiscussionRoom(
        id = 7L,
        discussionTitle = "Jetpack Compose vs XML",
        book = Book(7L, "Jetpack Compose Essentials", "John Smith", ""),
        writer = User(7L, Nickname("유컴포즈")),
        createAt = LocalDateTime.of(2025, 7, 17, 12, 0),
        snap = "진짜 Compose 써야해?",
        discussionOpinion = "미래는 Compose입니다."
    ),
    DiscussionRoom(
        id = 8L,
        discussionTitle = "테스트 코드 작성 전략",
        book = Book(8L, "Test-Driven Development", "Kent Beck", ""),
        writer = User(8L, Nickname("최테스트")),
        createAt = LocalDateTime.of(2025, 7, 11, 12, 0),
        snap = "언제 테스트 함?",
        discussionOpinion = "작은 단위부터 시작하세요."
    ),
    DiscussionRoom(
        id = 9L,
        discussionTitle = "Dependency Injection 원리",
        book = Book(9L, "안드로이드 DI 패턴", "Jake Wharton", ""),
        writer = User(9L, Nickname("강의존성")),
        createAt = LocalDateTime.of(2025, 7, 16, 12, 0),
        snap = "왜 Hilt 써야함?",
        discussionOpinion = "생성자 주입이 기본입니다."
    ),
    DiscussionRoom(
        id = 10L,
        discussionTitle = "Room DB 실전 활용법",
        book = Book(10L, "Android Room Guide", "Google", ""),
        writer = User(10L, Nickname("룸개발자")),
        createAt = LocalDateTime.of(2025, 7, 13, 12, 0),
        snap = "쿼리 왜 안됨?",
        discussionOpinion = "LiveData나 Flow로 감싸세요."
    ),
    DiscussionRoom(
        id = 11L,
        discussionTitle = "API 에러 핸들링 전략",
        book = Book(11L, "RESTful API Design", "Mark Masse", ""),
        writer = User(11L, Nickname("에러수집가")),
        createAt = LocalDateTime.of(2025, 7, 15, 12, 0),
        snap = "500에러 왜 나옴?",
        discussionOpinion = "에러 응답 스펙을 명확히 해야 합니다."
    ),
    DiscussionRoom(
        id = 12L,
        discussionTitle = "RxJava 완전정복",
        book = Book(12L, "RxJava 2", "Tomasz Nurkiewicz", ""),
        writer = User(12L, Nickname("이반응형")),
        createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
        snap = "왜 아직도 Rx 씀?",
        discussionOpinion = "복잡한 흐름 제어에 유리합니다."
    ),
    DiscussionRoom(
        id = 13L,
        discussionTitle = "Paging3 실전 적용",
        book = Book(13L, "Jetpack Paging Guide", "Google", ""),
        writer = User(13L, Nickname("페이징왕")),
        createAt = LocalDateTime.of(2025, 7, 17, 12, 0),
        snap = "null 왜 나와요?",
        discussionOpinion = "Placeholders와 LoadState를 확인하세요."
    ),
    DiscussionRoom(
        id = 14L,
        discussionTitle = "Flow와 StateFlow 차이",
        book = Book(14L, "Kotlin Flow Deep Dive", "JetBrains", ""),
        writer = User(14L, Nickname("이플로우")),
        createAt = LocalDateTime.of(2025, 7, 16, 12, 0),
        snap = "LiveData랑 뭐가 달라?",
        discussionOpinion = "콜드 스트림과 핫 스트림의 차이 이해 필요"
    ),
    DiscussionRoom(
        id = 15L,
        discussionTitle = "유닛 테스트 vs 통합 테스트",
        book = Book(15L, "Effective Testing", "Lisa Crispin", ""),
        writer = User(15L, Nickname("테스트마스터")),
        createAt = LocalDateTime.of(2025, 7, 15, 12, 0),
        snap = "테스트 나눠야 하나요?",
        discussionOpinion = "단위 테스트는 빠르고 명확해야 합니다."
    ),
    DiscussionRoom(
        id = 16L,
        discussionTitle = "Compose에서 상태관리",
        book = Book(16L, "Compose State Management", "Android Team", ""),
        writer = User(16L, Nickname("스테이트러버")),
        createAt = LocalDateTime.of(2025, 7, 14, 12, 0),
        snap = "remember 왜 씀?",
        discussionOpinion = "State Hoisting이 핵심입니다."
    )
)
