package todoktodok.backend.member.domain;

public enum MyDiscussionFilterType {

    CREATED,
    PARTICIPATED
    ;

    public boolean isTypeCreated() {
        return this == CREATED;
    }
}
