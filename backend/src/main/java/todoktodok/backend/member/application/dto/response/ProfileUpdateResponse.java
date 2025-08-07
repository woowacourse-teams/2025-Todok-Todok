package todoktodok.backend.member.application.dto.response;

import todoktodok.backend.member.domain.Member;

public record ProfileUpdateResponse(
        String nickname,
        String profileMessage
) {

    public ProfileUpdateResponse(final Member member) {
        this(
                member.getNickname(),
                member.getProfileMessage()
        );
    }
}
