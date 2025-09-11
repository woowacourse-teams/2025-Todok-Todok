package todoktodok.backend.member.application.service.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.jwt.JwtTokenProvider;
import todoktodok.backend.global.jwt.TokenInfo;
import todoktodok.backend.member.application.dto.request.RefreshTokenRequest;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.RefreshToken;
import todoktodok.backend.member.domain.repository.RefreshTokenRepository;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
class MemberCommandServiceConcurrencyTest {

    @Autowired
    MemberCommandService memberCommandService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @MockitoBean
    JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("리프레시 토큰 발급 요청을 중복으로 보낼 때, 중복된 토큰 생성 시 IllegalArgumentException으로 매핑한다")
    void refresh_validateDuplicatedToken() throws Exception {
        // given
        final String oldRefresh = "old-token";
        final String newFixed = "new-fixed-token";
        final long memberId = 1L;

        // 기존 토큰/회원 식별 가능하도록 JwtTokenProvider 동작 고정
        Mockito.when(jwtTokenProvider.getInfoByRefreshToken(oldRefresh))
                .thenReturn(new TokenInfo(memberId, "chanho680526@gmail.com", Role.USER));
        Mockito.when(jwtTokenProvider.createAccessToken(Mockito.any(Member.class)))
                .thenReturn("any-access");
        Mockito.when(jwtTokenProvider.createRefreshToken(Mockito.any(Member.class)))
                .thenReturn(newFixed); // 두 스레드 모두 같은 새 토큰 생성

        // 기존 oldRefresh 엔트리 미리 넣어두기(상황에 맞게)
        refreshTokenRepository.save(RefreshToken.create(oldRefresh));

        // 동시 시작 장치
        final int threads = 2;
        final CountDownLatch ready = new CountDownLatch(threads);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(threads);

        final List<Throwable> errors = Collections.synchronizedList(new ArrayList<>());

        final Runnable task = () -> {
            ready.countDown();
            try {
                start.await();
                memberCommandService.refresh(new RefreshTokenRequest(oldRefresh));
            } catch (Throwable t) {
                errors.add(t);
            } finally {
                done.countDown();
            }
        };

        new Thread(task, "t1").start();
        new Thread(task, "t2").start();

        // when: 두 스레드가 준비될 때까지 대기 후 동시에 시작
        ready.await();
        start.countDown();
        done.await();

        // then: 첫 요청은 DB 저장 성공, 두번째 요청은 UNIQUE 위반 예외로 ConcurrentModificationException 발생 후 저장되지 않음
        final boolean saved = refreshTokenRepository.findByToken(newFixed).isPresent();
        final List<Throwable> thrownExceptions = errors.stream().filter(e -> e instanceof Exception).toList();

        assertAll(
                () -> assertThat(saved).isTrue(),
                () -> assertThat(thrownExceptions).hasSize(1),
                () -> assertThat(thrownExceptions.getFirst()).isInstanceOf(ConcurrentModificationException.class)
                        .hasMessageContaining("중복된 리프레시 토큰 발급 요청입니다")
        );
    }
}
