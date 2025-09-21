CREATE TABLE discussion_member_view
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at        DATETIME(6) NOT NULL,
    modified_at       DATETIME(6) NOT NULL,
    deleted_at        DATETIME(6),
    discussion_id     BIGINT NOT NULL,
    member_id         BIGINT NOT NULL,
    FOREIGN KEY (discussion_id) REFERENCES discussion (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);
