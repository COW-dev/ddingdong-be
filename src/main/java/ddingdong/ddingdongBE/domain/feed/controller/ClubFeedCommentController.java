package ddingdong.ddingdongBE.domain.feed.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.feed.api.ClubFeedCommentApi;
import ddingdong.ddingdongBE.domain.feed.service.FacadeClubFeedCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubFeedCommentController implements ClubFeedCommentApi {

    private final FacadeClubFeedCommentService facadeClubFeedCommentService;

    @Override
    public void forceDeleteComment(Long feedId, Long commentId,
            PrincipalDetails principalDetails) {
        facadeClubFeedCommentService.forceDelete(principalDetails.getUser(), feedId, commentId);
    }
}
