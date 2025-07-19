package todoktodok.backend.member.domain;

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

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@SQLRestriction("deleted_at is NULL")
@SQLDelete(sql = "UPDATE block SET deleted_at = NOW() WHERE id = ?")
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member target;

    @Builder
    public static Block create(
            Member member, Member target
    ) {
        validateMemberAndTarget(member, target);
        return new Block(null, member, target);
    }

    private static void validateMemberAndTarget(Member member, Member target) {
        if (member.getId().equals(target.getId())) { //todo equals 기준 이야기해보기
            throw new IllegalArgumentException("자기 자신을 차단할 수 없습니다.");
        }
    }
}
