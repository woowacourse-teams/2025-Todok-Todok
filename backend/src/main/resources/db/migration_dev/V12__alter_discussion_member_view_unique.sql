ALTER TABLE discussion_member_view
ADD CONSTRAINT uq_discussion_member UNIQUE (discussion_id, member_id);
