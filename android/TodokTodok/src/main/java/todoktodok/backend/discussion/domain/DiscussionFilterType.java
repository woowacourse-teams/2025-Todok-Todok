package todoktodok.backend.discussion.domain;

public enum DiscussionFilterType {

    ALL,
    MINE,
    ;

    public boolean isTypeMine() {
        return this == MINE;
    }
}
