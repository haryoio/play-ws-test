package controllers;

import actors.ChatActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import javax.inject.Inject;

import models.websocket.messages.Message;
import play.mvc.Result;
import play.mvc.WebSocket;
import services.RoomManager;

public class ChatController extends BaseController{

    private final ActorSystem actorSystem;
    private final Materializer materializer;
    private final RoomManager roomManager;

    @Inject
    public ChatController(ActorSystem actorSystem, Materializer materializer, RoomManager roomManager) {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
        this.roomManager = roomManager;
    }

    /**
     * ChatRoomのビューを返すメソッド
     * @param roomName 部屋名 /chat/:roomName
     * @return Result　ビューテンプレートより生成されたhtmlを返す
     */
    public Result chatRoom(String roomName) {
        logger.debug(roomName + " opened");
        return ok(views.html.chat.render(roomName));
    }

    /**
     * ChatRoomのWebSocket接続を提供するメソッド
     * ws://host/chatより接続
     * @param roomName 部屋名 ws://host/chat/:roomName
     * @return WebSocket接続を返す
     */
    public WebSocket chatSocket(String roomName) {
        ActorRef chatActor = actorSystem.actorOf(ChatActor.props(roomManager, roomName));

        return WebSocket.Text.accept(request -> {
            Sink<String, ?> in = Sink.actorRef(actorSystem.actorOf(ChatActor.props(roomManager, roomName)), "closed");

            // TODO: Source.actorRefが非推奨のため、別の実装に変更
            Source<String, ?> out = Source.<Object>actorRef(256, OverflowStrategy.dropHead())
                    .map(Object::toString)
                    .mapMaterializedValue(sourceActorRef -> {
                        roomManager.joinRoom(roomName, sourceActorRef);
                        roomManager.broadcastMessage(roomName, new Message("User connected", "System"));
                        return chatActor;
                    });

            return Flow.fromSinkAndSource(in, out);
        });
    }}

