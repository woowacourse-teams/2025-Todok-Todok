package todoktodok.backend.discussion.application.service.query;

public record LikeCountAndIsLikedByMeDto(
        int likeCount,
        boolean isLikedByMe
) {
}
