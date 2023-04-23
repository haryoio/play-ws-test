package models.websocket.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.Getter;
import utils.JsonUtils;

@Getter
public class Message extends BaseMessageDto {

    @JsonCreator
    public Message(String content, String sender) {
        super("message");
        this.content=content;
        this.sender=sender;
    }
    /** メッセージの本文 **/
    private final String content;
    /** メッセージの送り主 **/
    private final String sender;

    public String toString() {
        return super.toString();
    }
}
