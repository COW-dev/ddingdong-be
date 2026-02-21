package ddingdong.ddingdongBE.domain.feed.controller;

import ddingdong.ddingdongBE.domain.feed.api.FeedCommentApi;
import ddingdong.ddingdongBE.domain.feed.controller.dto.request.CreateFeedCommentRequest;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.CreateFeedCommentResponse;
import ddingdong.ddingdongBE.domain.feed.service.FacadeFeedCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class FeedCommentController implements FeedCommentApi {

    private final FacadeFeedCommentService facadeFeedCommentService;

    @Override
    public CreateFeedCommentResponse createComment(Long feedId, String uuid,
            CreateFeedCommentRequest request) {
        return CreateFeedCommentResponse.from(
                facadeFeedCommentService.create(request.toCommand(uuid, feedId)));
    }

    @Override
    public void deleteComment(Long feedId, Long commentId, String uuid) {
        facadeFeedCommentService.delete(feedId, commentId, uuid);
    }
}
