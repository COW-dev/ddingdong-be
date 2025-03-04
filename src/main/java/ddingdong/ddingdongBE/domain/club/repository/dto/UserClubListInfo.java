package ddingdong.ddingdongBE.domain.club.repository.dto;


import java.time.LocalDate;

public interface UserClubListInfo {

    Long getId();

    String getName();

    String getCategory();

    String getTag();

    LocalDate getStart();

    LocalDate getEnd();
}
