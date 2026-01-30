package ddingdong.ddingdongBE.domain.pairgame.service.dto.command;

import ddingdong.ddingdongBE.domain.pairgame.entity.PairGameApplier;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record CreatePairGameApplierCommand(
        String name,
        String department,
        String studentNumber,
        String phoneNumber,
        MultipartFile studentFeeImageFile
){
    public PairGameApplier toEntity(String studentFeeImageUrl) {
        return PairGameApplier.builder()
                .name(name)
                .department(department)
                .studentNumber(studentNumber)
                .phoneNumber(phoneNumber)
                .studentFeeImageUrl(studentFeeImageUrl)
                .build();
    }
}