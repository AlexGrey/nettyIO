package modules.matchMaking;

import core.accountService.UserImpl;
import modules.gameMechanics.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Zver on 19.12.2015.
 */
public class MatchMaking {
    private static volatile MatchMaking instance;
    List<Room> availableRooms = new ArrayList<Room>();
    private MatchMaking() {

    }

    public static MatchMaking getInstance() {
        if (instance == null) {
            synchronized (MatchMaking.class) {
                if (instance == null) instance = new MatchMaking();
            }
        }
        return instance;
    }

    public Room findRoom(int countOfUsers){
        Room room = null;
        for (Room availableRoom: availableRooms) {
            if (availableRoom.playersInRoom() == countOfUsers) {
                room = availableRoom;
            }
        }
        return room;
    }

    public Room findRoomById(UUID id){
        Room room = null;
        for (Room availableRoom: availableRooms) {
            if (availableRoom.getId().equals(id)) {
                room = availableRoom;
            }
        }
        return room;
    }


    public void addAvailableRoom(Room room) {
        availableRooms.add(room);
    }

    public void removeAvailableRoom(Room room) {
        availableRooms.remove(room);
    }
}
