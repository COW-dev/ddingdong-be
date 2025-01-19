package ddingdong.ddingdongBE.file;

import ddingdong.ddingdongBE.file.service.dto.UploadFileDto;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

//TODO: 리팩토링 후 제거
public interface FileStore {

    List<UploadFileDto> storeFile(List<MultipartFile> multipartFile, String fileType, String filePath);

    List<UploadFileDto> storeDownloadableFile(List<MultipartFile> multipartFile, String fileType, String filePath);
    void deleteFile(String fileType, String filePath, String uploadFileName);

    String getImageUrlPrefix();

}
