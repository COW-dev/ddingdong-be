package ddingdong.ddingdongBE.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ddingdong.ddingdongBE.common.exception.ConvertException.ListToStringConvertException;
import ddingdong.ddingdongBE.common.exception.ConvertException.StringToListConvertException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> dataList) {
        try {
            return mapper.writeValueAsString(dataList);
        } catch (JsonProcessingException e) {
            throw new ListToStringConvertException();
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String data) {
        try {
            return mapper.readValue(data, List.class);
        } catch (JsonProcessingException e) {
            throw new StringToListConvertException();
        }
    }
}
