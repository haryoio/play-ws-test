package services;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import models.websocket.messages.BaseMessageDto;
import models.websocket.messages.Message;
import models.websocket.Room;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private final Map<String, Room> rooms;

    public RoomManager() {
        rooms = new ConcurrentHashMap<>();
    }

    /**
     * ChatRoomにメンバーを追加するメソッド
     * 部屋が存在しない場合は新規作成する。
     * @param roomName 部屋の名前
     * @param member 部屋に追加するアクター
     *
     */
    public void joinRoom(String roomName, ActorRef member) {
        Room targetRoom = rooms.get(roomName);

        /* ルーム一覧に部屋がない場合は新規作成 */
        if (targetRoom == null) {
            targetRoom = new Room();
            targetRoom.setName(roomName);
        }

        /* ルームのメンバーとして送信者を追加 */
        targetRoom.addMember(member);

        /* 部屋リストに再度追加 */
        rooms.put(roomName, targetRoom);
    }

    /**
     * 部屋からアクターを削除する
     * @param roomName 部屋の名前
     * @param member 削除するメンバー
     */
    public void leaveRoom(String roomName, ActorRef member) {
        Room targetRoom = rooms.get(roomName);
        member.tell(PoisonPill.getInstance(), member);
        targetRoom.removeMember(member);
    }

    public void broadcastMessage(String roomName, BaseMessageDto message) {
        Room targetRoom = rooms.get(roomName);
        if (targetRoom == null) {
            System.out.println("Room is Empty");
        }
        List<ActorRef> members = targetRoom.getMembers();
        members.forEach(member -> member.tell(message.toString(), ActorRef.noSender()));
    }

    public Room getRoomByName(String roomName) {

        var foundRoom = this.rooms.get(roomName);
        if (foundRoom == null) {
            Room newRoom = new Room();
            newRoom.setName(roomName);
            return newRoom;
        }
        return foundRoom;
    }
}
