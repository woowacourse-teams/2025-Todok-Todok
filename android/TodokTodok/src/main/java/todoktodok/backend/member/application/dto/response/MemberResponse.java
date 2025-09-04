package todoktodok.backend.member.application.dto.response;

import todoktodok.backend.member.domain.Member;

public record MemberResponse(
        Long memberId,
        String nickname,
        String profileImage
) {

    public MemberResponse(final Member member) {
        this(
                member.getId(),
                member.getNickname(),
                member.getProfileImage()
        );
    }
}
