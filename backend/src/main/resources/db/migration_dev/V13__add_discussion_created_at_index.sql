-- 인기 토론방 조회 성능 개선을 위한 인덱스 추가
-- WHERE created_at >= :sinceDate 필터링 최적화
CREATE INDEX idx_discussion_created_at ON discussion(created_at DESC);
