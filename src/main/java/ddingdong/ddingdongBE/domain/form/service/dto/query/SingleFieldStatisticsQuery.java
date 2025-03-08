package ddingdong.ddingdongBE.domain.form.service.dto.query;

import ddingdong.ddingdongBE.domain.formapplication.repository.dto.FileApplicationInfo;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.TextAnswerInfo;
import java.util.List;

public record SingleFieldStatisticsQuery(
        String type,
        List<SingleStatisticsQuery> answers
) {

    public record SingleStatisticsQuery(
            Long applicationId,
            String name,
            String answer
    ) {

        public static SingleStatisticsQuery fromFileInfo(FileApplicationInfo info) {
            return new SingleStatisticsQuery(info.getId(), info.getName(), info.getFileName());
        }

        public static SingleStatisticsQuery fromTextInfo(TextAnswerInfo info, String answer) {
            return new SingleStatisticsQuery(info.getId(), info.getName(), answer);
        }
    }
}
