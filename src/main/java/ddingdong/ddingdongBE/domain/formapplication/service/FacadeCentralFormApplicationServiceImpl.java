package ddingdong.ddingdongBE.domain.formapplication.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.PagingQuery;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.service.FormService;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.FormApplicationListQuery;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.MyFormApplicationPageQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeCentralFormApplicationServiceImpl implements FacadeCentralFormApplicationService {

    private final ClubService clubService;
    private final FormService formService;
    private final FormApplicationService formApplicationService;

    @Override
    public MyFormApplicationPageQuery getMyFormApplicationPage(Long formId, User user, int size, Long currentCursorId) {
        Club club = clubService.getByUserId(user.getId());
        Form form = formService.getById(formId);
        Slice<FormApplication> formApplicationPage = formApplicationService.getFormApplicationPageByFormId(formId, size, currentCursorId);
        if (formApplicationPage == null) {
            return MyFormApplicationPageQuery.createEmpty();
        }
        if (!form.getClub().equals(club)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
        List<FormApplication> completeFormApplications = formApplicationPage.getContent();
        List<FormApplicationListQuery> formApplicationListQueries = completeFormApplications.stream()
                .map(FormApplicationListQuery::of)
                .toList();
        PagingQuery pagingQuery = PagingQuery.of(currentCursorId, completeFormApplications, formApplicationPage.hasNext());

        return MyFormApplicationPageQuery.of(formApplicationListQueries, pagingQuery);

    }
}
