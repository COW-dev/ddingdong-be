package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import lombok.Getter;

@Getter
public class CurrentTermResponse {

    private String term;

    public CurrentTermResponse(String term) {
        this.term = term;
    }

    public static CurrentTermResponse from(String term) {
        return new CurrentTermResponse(term);
    }
}
