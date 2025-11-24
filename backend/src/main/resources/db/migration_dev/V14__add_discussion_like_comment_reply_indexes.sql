-- 기존 인덱스(discussion_id)가 단일 인덱스였다면 DROP 후 생성하거나,
-- 새로운 이름으로 생성하여 옵티마이저가 이 복합 인덱스를 선택하도록 유도합니다.
CREATE INDEX idx_dl_score_optimized
    ON discussion_like (discussion_id, deleted_at, created_at, id);

-- 기존 인덱스 삭제 (discussion_created_idx)
 ALTER TABLE comment DROP INDEX discussion_created_idx;

-- 새로운 최적화된 복합 인덱스 생성 (deleted_at과 id 추가)
CREATE INDEX idx_c_score_optimized
    ON comment (discussion_id, deleted_at, created_at, id);

-- 새로운 최적화된 복합 인덱스 생성
CREATE INDEX idx_r_score_optimized
    ON reply (comment_id, deleted_at, created_at, id);
