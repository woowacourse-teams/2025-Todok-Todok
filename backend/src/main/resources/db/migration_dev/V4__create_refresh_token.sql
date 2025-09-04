CREATE TABLE refresh_token
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at  DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    deleted_at  DATETIME(6),
    token       VARCHAR(255) NOT NULL
);
