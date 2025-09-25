package todoktodok.backend.member.application.dto.response;

import java.time.LocalDateTime;
import todoktodok.backend.member.domain.Block;

public record BlockMemberResponse(
        Long memberId,
        String nickname,
        LocalDateTime createdAt
) {

    public BlockMemberResponse(final Block block) {
        this(
            block.getTarget().getId(),
            block.getTarget().getNickname(),
            block.getCreatedAt()
        );
    }
}
