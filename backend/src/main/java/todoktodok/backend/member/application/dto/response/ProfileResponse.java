package todoktodok.backend.member.application.dto.response;

import todoktodok.backend.member.domain.Member;

public record ProfileResponse(
        Long memberId,
        String nickname,
        String profileMessage,
        String profileImage
) {

    public ProfileResponse(final Member member) {
        this(
                member.getId(),
                member.getNickname(),
                member.getProfileMessage(),
                member.getProfileImage()
        );
    }
}
