package models.websocket.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import utils.JsonUtils;

@Data
public class BaseMessageDto {
    private final String type;

    @JsonCreator
    public BaseMessageDto(
            @JsonProperty("type") String type
    ) {
        this.type = type;
    }


    public String toString() {
        return JsonUtils.convert(this);
    }
}
