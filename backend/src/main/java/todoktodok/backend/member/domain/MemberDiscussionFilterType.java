package todoktodok.backend.member.domain;

public enum MemberDiscussionFilterType {

    CREATED,
    PARTICIPATED
    ;

    public boolean isTypeCreated() {
        return this == CREATED;
    }
}
