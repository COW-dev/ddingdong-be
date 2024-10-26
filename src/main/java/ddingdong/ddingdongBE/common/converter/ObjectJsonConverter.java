package ddingdong.ddingdongBE.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ddingdong.ddingdongBE.common.exception.ConvertException.JsonToObjectConvertException;
import ddingdong.ddingdongBE.common.exception.ConvertException.ObjectToJsonConvertException;
import ddingdong.ddingdongBE.common.vo.FileInfo;
import jakarta.persistence.AttributeConverter;
import java.util.List;

public class ObjectJsonConverter implements AttributeConverter<List<FileInfo>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<FileInfo> fileInfos) {
        try {
            return mapper.writeValueAsString(fileInfos);
        } catch (JsonProcessingException e) {
            throw new ObjectToJsonConvertException();
        }
    }

    @Override
    public List<FileInfo> convertToEntityAttribute(String data) {
        try {
            return mapper.readValue(data, new TypeReference<List<FileInfo>>() {});
        } catch (JsonProcessingException e) {
            throw new JsonToObjectConvertException();
        }    }
}
