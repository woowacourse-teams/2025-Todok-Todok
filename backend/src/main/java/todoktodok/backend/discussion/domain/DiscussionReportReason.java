package todoktodok.backend.discussion.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum DiscussionReportReason {
    OFF_TOPIC("토론과 무관함"),
    INAPPROPRIATE("부적절한 내용"),
    ABUSIVE("욕설/혐오 표현"),
    SPAM_AD("스팸/광고");

    private final String description;

    DiscussionReportReason(String description) {
        this.description = description;
    }

    public static DiscussionReportReason fromDescription(String description) {
        return Arrays.stream(values())
                .filter(reason -> reason.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("적절하지 않은 신고 사유입니다: " + description));
    }
}
