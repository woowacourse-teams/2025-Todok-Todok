package todoktodok.backend.global.report;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ContentReportReason {

    OFF_TOPIC("토론 주제와 무관한 내용"),
    INAPPROPRIATE("부적절하거나 불쾌감을 주는 내용"),
    SPAM("도배/광고성 게시글"),
    ILLEGAL("불법 정보 또는 정책 위반");

    private final String description;

    ContentReportReason(final String description) {
        this.description = description;
    }

    public static ContentReportReason fromDescription(final String description) {
        return Arrays.stream(values())
                .filter(reason -> reason.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("적절하지 않은 신고 사유입니다: " + description));
    }
}
