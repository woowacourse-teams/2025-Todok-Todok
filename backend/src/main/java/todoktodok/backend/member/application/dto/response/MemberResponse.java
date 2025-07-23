package todoktodok.backend.member.application.dto.response;

import todoktodok.backend.member.domain.Member;

public record MemberResponse(
        Long memberId,
        String nickname
) {

    public MemberResponse(final Member member) {
        this(
                member.getId(),
                member.getNickname()
        );
    }
}
