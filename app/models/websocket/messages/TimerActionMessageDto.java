package models.websocket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TimerActionMessageDto extends BaseMessageDto {

    private final String action;

    public TimerActionMessageDto(
        @JsonProperty("action") String action
    ) {
        super("timer");
        this.action = action;
    }

}
