package services;

import akka.actor.ActorRef;
import models.websocket.Room;
import models.websocket.messages.TimerElapsedMessageDto;
import utils.JsonUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class TimerManager {
    private Timer timer;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private long startTime = 0L;
    private long elapsedTime = 0L;
    private final Room currentRoom;

    public TimerManager(Room room){
        this.currentRoom = room;
    }

    public void start() {
        if (timer == null) {
            timer = new Timer();
        }
        if (!isRunning.get()){
            startTime = System.currentTimeMillis() - elapsedTime;
            isRunning.set(true);
            try {

                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (isRunning.get()) {
                            elapsedTime = System.currentTimeMillis() - startTime;
                            broadcastElapsedTime();
                        }
                    }
                }, 0L, 100L);
            }catch (Exception e) {
                e.printStackTrace();
                System.out.println("error in timer startt");
            }
        }
    }

    public void stop() {
        if (isRunning.get()) {
            isRunning.set(false);
        }
        broadcastElapsedTime();
    }

    public void reset() {
        if (!isRunning.get()) {
            startTime = 0L;
            elapsedTime = 0L;
            timer.purge();
        }
        broadcastElapsedTime();
    }

    private void broadcastElapsedTime() {
        var members = currentRoom.getMembers();
        members.forEach(member -> {
            member.tell(toJson(), ActorRef.noSender());
        });
    }

    public String toJson() {
        return JsonUtils.convert(new TimerElapsedMessageDto(elapsedTime));
    }
}
