package todoktodok.backend.global.config;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class FixedClockConfig {

    @Bean
    public Clock testClock() {
        return Clock.fixed(
                Instant.parse("2025-08-15T12:00:00Z"),
                ZoneId.of("Asia/Seoul")
        );
    }
}
