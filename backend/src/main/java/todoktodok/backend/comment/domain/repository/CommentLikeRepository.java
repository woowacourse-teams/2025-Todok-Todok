package todoktodok.backend.comment.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.comment.domain.CommentLike;
import todoktodok.backend.member.domain.Member;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByMemberAndComment(final Member member, final Comment comment);

    int countCommentLikesByComment(final Comment comment);
}
