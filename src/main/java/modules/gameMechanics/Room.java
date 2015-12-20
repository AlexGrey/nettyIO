package modules.gameMechanics;

import com.corundumstudio.socketio.SocketIOClient;
import core.accountService.UserImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Zver on 19.12.2015.
 */
public class Room {
    UUID id;
    List<SocketIOClient> clientsId = new ArrayList<SocketIOClient>();
    List<UserImpl> clients = new ArrayList<UserImpl>();
    String winner = null;

    public Room() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void addClient(SocketIOClient client) {
        clientsId.add(client);
    }

    public List<SocketIOClient> getClientsId() {
        return clientsId;
    }

    public List<UserImpl> getClients() {
        return clients;
    }

    public void addClient(UserImpl client) {
        this.clients.add(client);
    }

    public int playersInRoom() {
        return clientsId.size();
    }
}
