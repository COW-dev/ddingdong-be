package ddingdong.ddingdongBE.common.utils;

import ddingdong.ddingdongBE.common.exception.ParsingFileException.UnsupportedFileTypeException;
import ddingdong.ddingdongBE.file.entity.FileCategory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class FileTypeClassifier {

  private static final Set<String> IMAGE_EXTENSIONS = new HashSet<>(Arrays.asList(
      "jpg", "jpeg", "png", "gif", "bmp", "webp", "tiff", "tif", "svg", "heic",
      "raw", "nef", "cr2", "cr3", "arw", "orf", "rw2", "dng"
  ));

  private static final Set<String> VIDEO_EXTENSIONS = new HashSet<>(Arrays.asList(
      "mp4", "avi", "mov", "wmv", "flv", "mkv", "webm", "m4v", "3gp", "mts", "m2ts"
  ));

  public FileCategory classifyFileType(String fileName) {
    String extension = getFileExtension(fileName);
    if (IMAGE_EXTENSIONS.contains(extension)) {
      return FileCategory.CLUB_POST_IMAGE;
    }
    if (VIDEO_EXTENSIONS.contains(extension)) {
      return FileCategory.CLUB_POST_VIDEO;
    }
    throw new UnsupportedFileTypeException();
  }

  private String getFileExtension(String fileName) {
    int lastIndexOf = fileName.lastIndexOf(".");
    if (lastIndexOf == -1) {
      return ""; // 확장자가 없는 경우
    }
    return fileName.substring(lastIndexOf + 1).toLowerCase();
  }
}
