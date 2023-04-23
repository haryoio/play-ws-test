package models.websocket;

import akka.actor.ActorRef;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Room {
    /** 部屋の名前 **/
    private String name;
    /** 部屋のメンバーを保持 **/
    private List<ActorRef> members = new ArrayList<>();

    /** 部屋のメンバーより指定された一人を削除 **/
    public void removeMember(ActorRef member) {
        members.remove(member);
    }

    /** メンバーをリスとに櫃取追加 **/
    public void addMember(ActorRef member) {
        members.add(member);
    }

}
