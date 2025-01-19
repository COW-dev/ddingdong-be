package ddingdong.ddingdongBE.file.service;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum ContentType {
    JPEG("image/jpeg", "IMAGE", "jpg", "jpeg", "jpe", "jif", "jfif", "jfi"),
    PNG("image/png", "IMAGE", "png"),
    GIF("image/gif", "IMAGE", "gif"),
    WEBP("image/webp", "IMAGE", "webp"),
    TIFF("image/tiff", "IMAGE", "tiff", "tif"),
    BMP("image/bmp", "IMAGE", "bmp"),
    SVG("image/svg+xml", "IMAGE", "svg", "svgz"),
    ICO("image/x-icon", "IMAGE", "ico"),
    HEIC("image/heic", "IMAGE", "heic"),
    HEIF("image/heif", "IMAGE", "heif"),
    RAW("image/x-raw", "IMAGE", "raw", "arw", "cr2", "nrw", "k25"),
    PSD("image/vnd.adobe.photoshop", "IMAGE", "psd"),
    PDF("application/pdf", "FILE", "pdf"),
    MSWORD("application/msword", "FILE", "doc"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "FILE", "docx"),
    EXCEL("application/vnd.ms-excel", "FILE", "xls"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "FILE", "xlsx"),
    TEXT("text/plain", "FILE", "txt"),
    HTML("text/html", "FILE", "html"),
    MP4("video/mp4", "VIDEO", "mp4"),
    WEBM("video/webm", "VIDEO", "webm"),
    MOV("video/quicktime", "VIDEO", "mov"),
    OCTET_STREAM("application/octet-stream","FILE");

    private final String mimeType;
    private final String keyMediaType;
    private final List<String> extensions;

    ContentType(String mimeType,String keyMediaType, String... extensions) {
        this.mimeType = mimeType;
        this.keyMediaType = keyMediaType;
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


    public static String getMediaTypeFromMimeType(String mimeType) {
        for (ContentType contentType : values()) {
            if (contentType.getMimeType().equals(mimeType)) {
                return contentType.getKeyMediaType();
            }
        }
        return OCTET_STREAM.getKeyMediaType();
    }
}
