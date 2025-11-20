-- 토론방 테이블에 카운트 컬럼 추가
ALTER TABLE discussion
    ADD COLUMN like_count INT NOT NULL DEFAULT 0,
    ADD COLUMN comment_count INT NOT NULL DEFAULT 0;

-- 기존 데이터의 좋아요 카운트 초기화
UPDATE discussion d
SET like_count = (
    SELECT COUNT(*)
    FROM discussion_like dl
    WHERE dl.discussion_id = d.id
      AND dl.deleted_at IS NULL
);

-- 기존 데이터의 댓글 카운트 초기화 (댓글 + 대댓글)
UPDATE discussion d
SET comment_count = (
    SELECT COALESCE(
        (SELECT COUNT(*)
         FROM comment c
         WHERE c.discussion_id = d.id
           AND c.deleted_at IS NULL),
        0
    ) + COALESCE(
        (SELECT COUNT(*)
         FROM reply r
         JOIN comment c ON r.comment_id = c.id
         WHERE c.discussion_id = d.id
           AND r.deleted_at IS NULL
           AND c.deleted_at IS NULL),
        0
    )
);
