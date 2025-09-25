-- Member.nickname, Member.id에 인덱스 추가
-- 닉네임으로 회원 조회 시 인덱스 적용
ALTER TABLE member ADD INDEX member_nickname_idx (nickname, id);

-- Book.title, Discussion.title에 전문 검색 인덱스 추가
-- 도서 혹은 책 제목으로 검색 시 인덱스 적용
ALTER TABLE discussion ADD FULLTEXT INDEX ft_discussion_title_idx (title) WITH PARSER ngram;
ALTER TABLE book ADD FULLTEXT INDEX ft_book_title_idx (title) WITH PARSER ngram;

-- Comment.discussion_id, Comment.created_at, Comment.id 복합인덱스 추가
-- 활성화된 토론방 조회 시 인덱스 적용
ALTER TABLE comment ADD INDEX discussion_created_idx (discussion_id, created_at, id);
