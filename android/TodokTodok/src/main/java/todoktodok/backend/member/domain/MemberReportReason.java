package todoktodok.backend.member.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum MemberReportReason {

    ABUSIVE("욕설/인신공격"),
    HARASSMENT("괴롭힘/스토킹"),
    HATE_SPEECH("혐오/차별 발언"),
    SPAM("스팸/홍보 계정");

    private final String description;

    MemberReportReason(final String description) {
        this.description = description;
    }

    public static MemberReportReason fromDescription(final String description) {
        return Arrays.stream(values())
                .filter(reason -> reason.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("적절하지 않은 신고 사유입니다: " + description));
    }
}

