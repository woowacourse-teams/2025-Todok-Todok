package todoktodok.backend.member.application.dto.response;

import todoktodok.backend.member.domain.Member;

public record MemberResponse(
        Long memberId,
        String nickname,
        String profileImage
) {

    private static final String DELETED_MEMBER_PROFILE_IMAGE = "https://todoktodok.s3.ap-northeast-2.amazonaws.com/todoktodok-images/profile/todoki.png";
    private static final String DELETED_MEMBER_NICKNAME = "(알수없음)";

    public MemberResponse(final Member member) {
        this(
                member.getId(),
                getNickname(member),
                getProfileImage(member)
        );
    }

    public static String getNickname(final Member member) {
        return member.isDeleted() ? DELETED_MEMBER_NICKNAME : member.getNickname();
    }

    public static String getProfileImage(final Member member) {
        return member.isDeleted() ? DELETED_MEMBER_PROFILE_IMAGE : member.getProfileImage();
    }
}
