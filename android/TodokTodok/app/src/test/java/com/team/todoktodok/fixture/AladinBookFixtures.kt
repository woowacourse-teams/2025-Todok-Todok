package com.team.todoktodok.fixture

import com.team.domain.model.book.AladinBook
import com.team.domain.model.book.AladinBooks
import com.team.domain.model.book.BookAuthor
import com.team.domain.model.book.BookImage
import com.team.domain.model.book.BookTitle
import com.team.domain.model.book.ISBN

object AladinBookFixtures {
    val books: AladinBooks =
        AladinBooks(
            listOf(
                AladinBook(
                    ISBN(9791158391409L),
                    BookTitle("오브젝트 - 코드로 이해하는 객체지향 설계"),
                    BookAuthor("조영호 (지은이)"),
                    BookImage("https://image.aladin.co.kr/product/19368/10/coversum/k972635015_1.jpg"),
                ),
                AladinBook(
                    ISBN(9791158392147L),
                    BookTitle("오브젝트 디자인 스타일 가이드 - 팀의 생산성을 높이는 고품질 객체지향 코드 작성법"),
                    BookAuthor("마티아스 노박 (지은이), 이상주 (옮긴이)"),
                    BookImage("https://image.aladin.co.kr/product/24608/31/coversum/k102631279_1.jpg"),
                ),
                AladinBook(
                    ISBN(9788960882294L),
                    BookTitle("배경 일러스트 작법서 Vol.2 - 실내.조형물 편"),
                    BookAuthor("이동익 (지은이)"),
                    BookImage("https://image.aladin.co.kr/product/14549/92/coversum/8960882291_1.jpg"),
                ),
                AladinBook(
                    ISBN(9791161756240L),
                    BookTitle("객체지향 UI 디자인 - 쓰기 편한 소프트웨어 디자인 원리"),
                    BookAuthor("소시오미디어 주식회사, 우에노 마나부, 후지이 고타 (지은이), 송지연 (옮긴이)"),
                    BookImage("https://image.aladin.co.kr/product/29010/28/coversum/s852836427_2.jpg"),
                ),
                AladinBook(
                    ISBN(9791196166021L),
                    BookTitle("패스워드"),
                    BookAuthor("마틴 폴 이브 (지은이), 최원희 (옮긴이)"),
                    BookImage("https://image.aladin.co.kr/product/12151/47/coversum/k852531246_1.jpg"),
                ),
                AladinBook(
                    ISBN(9788988784655L),
                    BookTitle("C++ Object Oriented Programming - 제3판"),
                    BookAuthor("전금문 (지은이)"),
                    BookImage("https://image.aladin.co.kr/product/34/24/coversum/8988784650_1.gif"),
                ),
                AladinBook(
                    ISBN(9791187497219L),
                    BookTitle("엘레강트 오브젝트 - 새로운 관점에서 바라본 객체지향"),
                    BookAuthor("Yegor Bugayenko (지은이), 조영호 (옮긴이)"),
                    BookImage("https://image.aladin.co.kr/product/25837/40/coversum/k762736538_1.jpg"),
                ),
                AladinBook(
                    ISBN(9788972830467L),
                    BookTitle("분산 오브젝트 지향기술 CORBA"),
                    BookAuthor("H.ONOZAWA (지은이)"),
                    BookImage("https://image.aladin.co.kr/img/noimg_sum_b.gif"),
                ),
                AladinBook(
                    ISBN(9788979148633L),
                    BookTitle("『Object-C : 맥과 아이폰 애플리케이션 프로그래밍』+『Head First iPhone Development』 세트 - 전2권"),
                    BookAuthor("댄 필로네, 오기하라 타케시, 트레이시 필로네 (지은이), 신상재, 강권학 (옮긴이)"),
                    BookImage("https://image.aladin.co.kr/product/1299/70/coversum/8979148631_1.jpg"),
                ),
                AladinBook(
                    ISBN(9788931548563L),
                    BookTitle("성공과 실패를 결정하는 1%의 객체 지향 원리"),
                    BookAuthor("아키라 히라사와 (지은이), 신동완, 이길섭 (옮긴이)"),
                    BookImage("https://image.aladin.co.kr/product/56/74/coversum/8931548567_1.jpg"),
                ),
                AladinBook(
                    ISBN(9788980545308L),
                    BookTitle("오브젝트 디자인 - 소프트웨어 개발의 성공 열쇠"),
                    BookAuthor("Rebecca Wirfs-Brock, Alan Mckean (지은이), 윤대현, 김동혁 (옮긴이)"),
                    BookImage("https://image.aladin.co.kr/product/48/25/coversum/8980545304_1.gif"),
                ),
                AladinBook(
                    ISBN(9788976279057L),
                    BookTitle("Beginning Java Objects"),
                    BookAuthor("Jacquie Barker (지은이), 고규철, 천주석, 윈도우 사용자 그룹 (옮긴이)"),
                    BookImage("https://image.aladin.co.kr/product/30/96/coversum/8976279050_1.gif"),
                ),
                AladinBook(
                    ISBN(9791197566509L),
                    BookTitle("유튜버 쌤의 코딩 클래스 : 엔트리 기초"),
                    BookAuthor("이산, 단, 뽀 (지은이)"),
                    BookImage("https://image.aladin.co.kr/product/28185/6/coversum/k232835776_1.jpg"),
                ),
                AladinBook(
                    ISBN(9788964490792L),
                    BookTitle("Object Oriented Programming using JAVA"),
                    BookAuthor("김태훈, Debnath Bhattacharyya (지은이)"),
                    BookImage("https://image.aladin.co.kr/product/778/75/coversum/8964490797_1.jpg"),
                ),
                AladinBook(
                    ISBN(9788972808350L),
                    BookTitle("AutoCAD 2011 - 실전 오브젝트 설계의 완성을 높이는"),
                    BookAuthor("윤상수, 김대중 (지은이)"),
                    BookImage("https://image.aladin.co.kr/product/769/76/coversum/8972808350_1.jpg"),
                ),
                AladinBook(
                    ISBN(9788931420593L),
                    BookTitle("프로그래밍 학습 시리즈 Java 2 - Java로 시작하는 오브젝트 지향 프로그래밍"),
                    BookAuthor("쿠와하라 신야 (지은이)"),
                    BookImage("https://image.aladin.co.kr/product/34/15/coversum/8931420595_1.gif"),
                ),
            ),
        )
}
