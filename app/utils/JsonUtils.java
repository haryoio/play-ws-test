package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {

    /**
     *  JSON文字列をDTOに変換する　
     */
    public static<T> T parse(Class<T> dto, String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, dto);
        }catch(IOException e) {
            return null;
        }
    }

    /**
     * DTOをJSON文字列に変換する
     */
    public static String convert(Object dto) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
        } catch(JsonProcessingException e) {
            return null;
        }
    }
}
