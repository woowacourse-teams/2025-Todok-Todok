CREATE TABLE member
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at      DATETIME(6) NOT NULL,
    modified_at     DATETIME(6) NOT NULL,
    deleted_at      DATETIME(6),
    email           VARCHAR(255) NOT NULL UNIQUE,
    nickname        VARCHAR(255) NOT NULL,
    profile_image   VARCHAR(255) NOT NULL,
    profile_message VARCHAR(255)
);

CREATE TABLE book
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at  DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    deleted_at  DATETIME(6),
    isbn        VARCHAR(13)  NOT NULL UNIQUE,
    author      VARCHAR(255) NOT NULL,
    image       VARCHAR(255),
    publisher   VARCHAR(255) NOT NULL,
    summary     VARCHAR(255),
    title       VARCHAR(255) NOT NULL
);

CREATE TABLE block
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at  DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    deleted_at  DATETIME(6),
    member_id   BIGINT NOT NULL,
    target_id   BIGINT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (target_id) REFERENCES member (id)
);

CREATE TABLE discussion
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at  DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    deleted_at  DATETIME(6),
    member_id   BIGINT        NOT NULL,
    book_id     BIGINT        NOT NULL,
    title       VARCHAR(255)  NOT NULL,
    content     VARCHAR(2048) NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (book_id) REFERENCES book (id)
);

CREATE TABLE comment
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at    DATETIME(6) NOT NULL,
    modified_at   DATETIME(6) NOT NULL,
    deleted_at    DATETIME(6),
    discussion_id BIGINT        NOT NULL,
    member_id     BIGINT        NOT NULL,
    content       VARCHAR(2048) NOT NULL,
    FOREIGN KEY (discussion_id) REFERENCES discussion (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE reply
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at  DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    deleted_at  DATETIME(6),
    comment_id  BIGINT        NOT NULL,
    member_id   BIGINT        NOT NULL,
    content     VARCHAR(2048) NOT NULL,
    FOREIGN KEY (comment_id) REFERENCES comment (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE discussion_like
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at    DATETIME(6) NOT NULL,
    modified_at   DATETIME(6) NOT NULL,
    deleted_at    DATETIME(6),
    discussion_id BIGINT NOT NULL,
    member_id     BIGINT NOT NULL,
    FOREIGN KEY (discussion_id) REFERENCES discussion (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE comment_like
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at  DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    deleted_at  DATETIME(6),
    comment_id  BIGINT NOT NULL,
    member_id   BIGINT NOT NULL,
    FOREIGN KEY (comment_id) REFERENCES comment (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE reply_like
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at  DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    deleted_at  DATETIME(6),
    reply_id    BIGINT NOT NULL,
    member_id   BIGINT NOT NULL,
    FOREIGN KEY (reply_id) REFERENCES reply (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE member_report
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at  DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    deleted_at  DATETIME(6),
    member_id   BIGINT      NOT NULL,
    target_id   BIGINT      NOT NULL,
    reason      VARCHAR(50) NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (target_id) REFERENCES member (id)
);

CREATE TABLE discussion_report
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at    DATETIME(6) NOT NULL,
    modified_at   DATETIME(6) NOT NULL,
    deleted_at    DATETIME(6),
    discussion_id BIGINT      NOT NULL,
    member_id     BIGINT      NOT NULL,
    reason        VARCHAR(50) NOT NULL,
    FOREIGN KEY (discussion_id) REFERENCES discussion (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE comment_report
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at  DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    deleted_at  DATETIME(6),
    comment_id  BIGINT      NOT NULL,
    member_id   BIGINT      NOT NULL,
    reason      VARCHAR(50) NOT NULL,
    FOREIGN KEY (comment_id) REFERENCES comment (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE reply_report
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at  DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    deleted_at  DATETIME(6),
    reply_id    BIGINT      NOT NULL,
    member_id   BIGINT      NOT NULL,
    reason      VARCHAR(50) NOT NULL,
    FOREIGN KEY (reply_id) REFERENCES reply (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);
