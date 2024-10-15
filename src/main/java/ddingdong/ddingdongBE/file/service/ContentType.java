package ddingdong.ddingdongBE.file.service;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum ContentType {
    JPEG("image/jpeg", false, "jpg", "jpeg", "jpe", "jif", "jfif", "jfi"),
    PNG("image/png", false, "png"),
    GIF("image/gif", false, "gif"),
    WEBP("image/webp", false, "webp"),
    TIFF("image/tiff", false, "tiff", "tif"),
    BMP("image/bmp", false, "bmp"),
    SVG("image/svg+xml", false, "svg", "svgz"),
    ICO("image/x-icon", false, "ico"),
    HEIC("image/heic", false, "heic"),
    HEIF("image/heif", false, "heif"),
    RAW("image/x-raw", false, "raw", "arw", "cr2", "nrw", "k25"),
    PSD("image/vnd.adobe.photoshop", false, "psd"),
    PDF("application/pdf", false, "pdf"),
    MSWORD("application/msword", false, "doc"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", false, "docx"),
    EXCEL("application/vnd.ms-excel", false, "xls"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", false, "xlsx"),
    TEXT("text/plain", false, "txt"),
    HTML("text/html", false, "html"),
    MP4("video/mp4", true, "mp4"),
    WEBM("video/webm", true, "webm"),
    MOV("video/quicktime", true, "mov"),
    OCTET_STREAM("application/octet-stream", false);

    private final String mimeType;
    private final boolean isVideo;
    private final List<String> extensions;

    ContentType(String mimeType, boolean isVideo, String... extensions) {
        this.mimeType = mimeType;
        this.isVideo = isVideo;
        this.extensions = Arrays.asList(extensions);
    }

    public static ContentType fromExtension(String extension) {
        String lowerExtension = extension.toLowerCase();
        for (ContentType contentType : values()) {
            if (contentType.extensions.contains(lowerExtension)) {
                return contentType;
            }
        }
        return OCTET_STREAM;
    }

    public boolean isVideo() {
        return this.isVideo;
    }
}
