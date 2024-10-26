package ddingdong.ddingdongBE.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ddingdong.ddingdongBE.common.vo.FileInfo;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FileInfoConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String convertToString(List<FileInfo> fileInfos) {
        try {
            return objectMapper.writeValueAsString(fileInfos);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("FileInfo list를 json string으로 변환하는데 실패했습니다.", e);
        }
    }

    public List<FileInfo> convertToJson(String json) {
        try {
            return objectMapper.readValue(json,
                new TypeReference<>() {
                });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON을 FileInfo list로 변환하는데 실패했습니다.", e);
        }
    }
}

