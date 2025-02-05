package ddingdong.ddingdongBE.domain.form.service.vo;

import ddingdong.ddingdongBE.common.utils.TimeUtils;
import java.time.LocalDate;

public class HalfYear {

    private int year;
    private boolean isFirstHalf;

    private HalfYear(int year, boolean isFirstHalf) {
        this.year = year;
        this.isFirstHalf = isFirstHalf;
    }

    public static HalfYear from(LocalDate date) {
        return new HalfYear(date.getYear(), TimeUtils.isFirstHalf(date.getMonthValue()));
    }

    public void minusHalves() {
        if (isFirstHalf) {
            year = year - 1;
            isFirstHalf = false;
            return;
        }
        isFirstHalf = true;
    }

    public LocalDate getHalfStartDate() {
        if(isFirstHalf) {
            return LocalDate.of(year, 1, 1);
        }
        return LocalDate.of(year, 7, 1);
    }

    public LocalDate getHalfEndDate() {
        if(isFirstHalf) {
            return LocalDate.of(year, 6, 30);
        }
        return LocalDate.of(year, 12, 31);
    }

    public String getLabel() {
        if (isFirstHalf) {
            return year + "-1";
        }
        return year + "-2";
    }
}
