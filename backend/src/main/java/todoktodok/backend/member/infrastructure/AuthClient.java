package todoktodok.backend.member.infrastructure;

import todoktodok.backend.member.application.service.command.GoogleAuthMemberDto;

public interface AuthClient {

    String resolveVerifiedEmailFrom(final String idTokenRequest);

    GoogleAuthMemberDto resolveVerifiedEmailAndNicknameFrom(final String idTokenRequest);
}
