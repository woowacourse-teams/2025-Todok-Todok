CREATE TABLE notification
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at        DATETIME(6) NOT NULL,
    modified_at       DATETIME(6) NOT NULL,
    deleted_at        DATETIME(6),
    is_read           TINYINT(1) NOT NULL DEFAULT 0,
    recipient_id      BIGINT NOT NULL,
    discussion_id     BIGINT NOT NULL,
    comment_id        BIGINT DEFAULT NULL,
    reply_id          BIGINT DEFAULT NULL,
    member_nickname   VARCHAR(255) NOT NULL,
    discussion_title  VARCHAR(255) NOT NULL,
    content           VARCHAR(2048) DEFAULT NULL,
    notification_type VARCHAR(50) NOT NULL,
    notification_target VARCHAR(50) NOT NULL,
    FOREIGN KEY (recipient_id) REFERENCES member (id)
);
