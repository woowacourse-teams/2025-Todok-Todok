ALTER TABLE member_report
    ADD COLUMN reason VARCHAR(50) NOT NULL;

ALTER TABLE discussion_report
    ADD COLUMN reason VARCHAR(50) NOT NULL;

ALTER TABLE comment_report
    ADD COLUMN reason VARCHAR(50) NOT NULL;

ALTER TABLE reply_report
    ADD COLUMN reason VARCHAR(50) NOT NULL;
