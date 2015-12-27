package modules.gameMechanics;

import com.corundumstudio.socketio.SocketIOClient;
import core.accountService.UserImpl;
import core.storageService.StorageImpl;

import java.util.*;

/**
 * Created by Zver on 19.12.2015.
 */
public class Room {
    UUID id;
    Game game;
    List<UserImpl> clientsInTheRoom = new ArrayList<UserImpl>();
    String winner = null;

    public Room() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public List<UserImpl> getClients() {
        return clientsInTheRoom;
    }

    public void addClient(UserImpl client) {
        this.clientsInTheRoom.add(client);
    }

    public int playersInRoom() {
        return clientsInTheRoom.size();
    }

    public UserImpl findUserByName(String name) {
        UserImpl user = null;
        for (UserImpl client: clientsInTheRoom) {
            if (client.getName().equals(name)){
                user = client;
            }
        }
        return user;
    }

    public UserImpl findUserBySessionId(UUID id) {
        UserImpl user = null;
        for (UserImpl client: clientsInTheRoom) {
            if (client.getCurrentSessionId().equals(id)) {
                user = client;
            }
        }
        return user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean allClientsReadyToFight() {
        boolean clientsReady = true;
        for (UserImpl client: clientsInTheRoom) {
            if (!client.isReadyToFight()){
                clientsReady = false;
            }
        }
        return clientsReady;
    }
}
