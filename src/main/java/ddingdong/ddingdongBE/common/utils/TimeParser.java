package ddingdong.ddingdongBE.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeParser {

  private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

  public static LocalDateTime parseToLocalDateTime(String dateString) {
    if (dateString == null || dateString.isBlank()) {
      return null;
    }

    return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(DATE_FORMAT));
  }

  public static LocalDateTime processDate(String dateString, LocalDateTime currentDate) {
    if (dateString == null) {
      return currentDate;
    }

    if (dateString.isBlank()) {
      return null;
    }

    return parseToLocalDateTime(dateString);
  }
}
