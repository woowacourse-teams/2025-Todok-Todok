package todoktodok.backend.discussion.application.service.query;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DiscussionQueryService {

    private final DiscussionRepository discussionRepository;

    public List<DiscussionResponse> getDiscussions() {
        final List<Discussion> discussions = discussionRepository.findAll();

        return discussions.stream()
                .map(DiscussionResponse::new)
                .toList();
    }
}
