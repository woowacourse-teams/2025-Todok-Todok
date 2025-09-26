ALTER TABLE member
ADD CONSTRAINT uq_member_nickname UNIQUE (nickname);
