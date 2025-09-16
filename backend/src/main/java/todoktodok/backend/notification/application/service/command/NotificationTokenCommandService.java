package todoktodok.backend.notification.application.service.command;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
        final String fid = notificationTokenRequest.fid();

        validateDuplicatedNotificationToken(token, member);

        notificationTokenRepository.deleteByFidAndMember(fid, member);

        final NotificationToken notificationToken = NotificationToken.builder()
                .token(token)
                .fid(fid)
                .member(member)
                .build();

        saveNotificationTokenIfUnique(memberId, notificationToken);
    }

    private void validateDuplicatedNotificationToken(
            final String token,
            final Member member
    ) {
        if (notificationTokenRepository.existsByTokenAndMember(token, member)) {
            return;
        }
        if (notificationTokenRepository.existsByToken(token)) {
            throw new IllegalArgumentException(
                    String.format("다른 계정에 등록된 토큰입니다: memberId= %d, token= %s", member.getId(), maskToken(token))
            );
        }
    }

    private void saveNotificationTokenIfUnique(
            final Long memberId,
            final NotificationToken notificationToken
    ) {
        try {
            notificationTokenRepository.save(notificationToken);
        } catch (DataIntegrityViolationException e) {
            throw new ConcurrentModificationException(
                    String.format("중복된 알람 토큰 발급 요청입니다: memberId = %d, token= %s", memberId, notificationToken.getToken())
            );
        }
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new NoSuchElementException(
                                String.format("해당 회원을 찾을 수 없습니다: memberId= %s", memberId)
                        )
                );
    }

    private String maskToken(final String token) {
        if (token == null || token.length() < 8) {
            return "****";
        }
        return token.substring(0, 4) + "****" + token.substring(token.length() - 4);
    }
}
