
package models.websocket.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TimerElapsedMessageDto extends BaseMessageDto{
    private final long elapsed;

    public TimerElapsedMessageDto(
            @JsonProperty("elapsed") long elapsed
    ) {
        super("elapsed");
        this.elapsed = elapsed;
    }
}
