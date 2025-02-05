package ddingdong.ddingdongBE.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    public static LocalDateTime parseToLocalDateTime(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }

        return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static LocalDateTime processDate(String dateString, LocalDateTime currentDate) {
        if (dateString == null) {
            return null;
        }

        if (dateString.isBlank()) {
            return null;
        }

        return parseToLocalDateTime(dateString);
    }

    public static boolean isDateInRange(LocalDate nowDate, LocalDate startDate, LocalDate endDate) {
        if (nowDate == null || startDate == null || endDate == null) {
            return false;
        }
        return !nowDate.isBefore(startDate) && !nowDate.isAfter(endDate);
    }

    public static boolean isFirstHalf(int month) {
        return month >= 1 && month <= 6;
    }
}
