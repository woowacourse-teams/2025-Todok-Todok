package todoktodok.backend.notification.application.service.command;

import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.notification.application.dto.request.NotificationTokenRequest;
import todoktodok.backend.notification.domain.NotificationToken;
import todoktodok.backend.notification.domain.repository.NotificationTokenRepository;

@Service
@Transactional
@AllArgsConstructor
public class NotificationTokenCommandService {

    private final NotificationTokenRepository notificationTokenRepository;
    private final MemberRepository memberRepository;

    public void save(
            final Long memberId,
            final NotificationTokenRequest notificationTokenRequest
    ) {
        final Member member = findMember(memberId);
        final String token = notificationTokenRequest.token();

        validateDuplicatedNotificationToken(token, memberId);

        final NotificationToken notificationToken = NotificationToken.builder()
                .token(token)
                .member(member)
                .build();

        notificationTokenRepository.save(notificationToken);
    }

    private void validateDuplicatedNotificationToken(
            final String token,
            final Long memberId
    ) {
        if (notificationTokenRepository.existsByToken(token)) {
            throw new IllegalArgumentException(
                    String.format("해당 토큰은 중복된 토큰입니다: memberId= %d", memberId)
            );
        }
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException(
                                String.format("해당 회원을 찾을 수 없습니다: memberId= %s", memberId)
                        )
                );
    }
}
