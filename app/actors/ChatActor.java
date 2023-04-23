package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.websocket.messages.JoinRoomMessageDto;
import models.websocket.messages.Message;
import services.RoomManager;
import services.TimerManager;

public class ChatActor extends AbstractActor {

    private final String roomName;
    private final RoomManager roomManager;
    private final TimerManager timerManager;

    public static Props props(RoomManager roomManager, String roomName) {
        return Props.create(ChatActor.class, () -> new ChatActor(roomManager, roomName));
    }

    public ChatActor(RoomManager roomManager, String roomName) {
        this.roomManager = roomManager;
        this.roomName = roomName;
        var currentRoom = roomManager.getRoomByName(roomName);
        if (currentRoom == null) {
            roomManager.joinRoom(roomName, ActorRef.noSender());
            currentRoom = roomManager.getRoomByName(roomName);
        }
        this.timerManager = new TimerManager(currentRoom);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                /* roomManager内にあるActorRef全てに対してtellする。 **/
                .match(String.class, message -> {
                    if (message.startsWith("User") || message.startsWith("closed")) {
                        return;
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode json = mapper.readTree(message);
                    if (json.isEmpty()) {
                        return;
                    }
                    String messageType = json.get("type").asText();
                    if (messageType.isEmpty()) {
                        return;
                    }

                    switch (messageType) {
                    case "broadcast":
                        String msg = json.get("message").asText();
                        roomManager.broadcastMessage(roomName, new Message(msg, "User"));
                        break;
                    case "timer":
                        String action = json.get("action").asText();
                            switch (action) {
                            case "start":
                                timerManager.start();
                                break;
                            case "stop":
                                timerManager.stop();
                                break;
                            case "reset":
                                timerManager.reset();
                                break;
                            default:
                                throw new IllegalAccessException("Unknown timer action");
                            }
                        break;
                    default:
                        throw new IllegalAccessException("Unknown message type");
                    }

                })
                .build();
    }

    @Override
    public void postStop() {
    }

}
