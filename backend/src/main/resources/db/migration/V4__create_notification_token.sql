CREATE TABLE notification_token
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at  DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    deleted_at  DATETIME(6),
    token       VARCHAR(255) NOT NULL UNIQUE,
    fid         VARCHAR(255) NOT NULL,
    member_id   BIGINT      NOT NULL
);
