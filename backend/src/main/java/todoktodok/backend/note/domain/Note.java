package todoktodok.backend.note.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.member.domain.Member;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@SQLRestriction("deleted_at is NULL")
@SQLDelete(sql = "UPDATE note SET deleted_at = NOW() WHERE id = ?")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1024)
    private String snap;

    @Column(length = 1024)
    private String memo;

//    @ManyToOne
//    @JoinColumn(nullable = false)
//    private Book book;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    @Builder
    public static Note create(
            String snap, String memo, Book book, Member member
    ) {
        validateBlankSnapAndMemo(snap, memo);
        return new Note(null, snap, memo, member); //todo book 추가하기
    }

    private static void validateBlankSnapAndMemo(String snap, String memo) {
        if (snap.isBlank() && memo.isBlank()) {
            throw new IllegalArgumentException("스냅과 메모 중 최소 하나를 입력해주세요");
        }
    }
}
