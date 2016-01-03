import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import core.accountService.AccountServiceImpl;
import core.accountService.UserImpl;
import core.storageService.StorageImpl;
import core.utilities.ValidatorImpl;
import modules.gameMechanics.Game;
import modules.gameMechanics.Room;
import modules.matchMaking.MatchMaking;
import serializableObj.AccountObject;
import serializableObj.ChatObject;
import serializableObj.FindGameObject;
import serializableObj.RoomObject;

import java.util.UUID;

/**
 * Created by Zver on 13.12.2015.
 */
public class MainLauncher {
    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setOrigin("*");
        config.setHostname("54.194.200.72");
        config.setPort(8080);
        final SocketIOServer server = new SocketIOServer(config);
        final ValidatorImpl validator = new ValidatorImpl();

        server.addEventListener("reg", AccountObject.class, new DataListener<AccountObject>() {
            public void onData(SocketIOClient client, AccountObject data, AckRequest ackRequest) {
                UserImpl user = new UserImpl(data.getName(), data.getPassword());
                if (!validator.userIsExist(user)) {
                    AccountServiceImpl.getInstance().registration(user);
                    data.setAnswer("пользователь с именем: " + user.getName() + " успешно зарегистрирован!");
                } else {
                    data.setAnswer("пользователь с именем: " + user.getName() + " уже существует!");
                }
                server.getBroadcastOperations().sendEvent("reg", data);
            }
        });

        server.addEventListener("auth", AccountObject.class, new DataListener<AccountObject>() {
            public void onData(SocketIOClient client, AccountObject data, AckRequest ackRequest) {
                try {
                    UserImpl user = AccountServiceImpl.getInstance().findUserByName(data.getName());
                    user.setCurrentSessionId(client.getSessionId());
                    if (validator.correctUserData(data.getName(), data.getPassword())) {
                        AccountServiceImpl.getInstance().auth(user);
                        data.setAnswer("success");
                    } else {
                        data.setAnswer("failure");
                    }
                } catch (NullPointerException e) {
                    System.out.println(e);
                    data.setAnswer("failure");
                }
                server.getBroadcastOperations().sendEvent("auth", data);
            }
        });

        server.addEventListener("amountOfPlayers", AccountObject.class, new DataListener<AccountObject>() {
            public void onData(SocketIOClient client, AccountObject data, AckRequest ackRequest) {
                try {
                    String toData = String.valueOf(AccountServiceImpl.getInstance().getUsersOnline());
                    data.setAnswer(toData);
                } catch (NullPointerException e) {
                    data.setAnswer("failure");
                }
                server.getBroadcastOperations().sendEvent("amountOfPlayers", data);
            }
        });

        server.addEventListener("userInfo", AccountObject.class, new DataListener<AccountObject>() {
            public void onData(SocketIOClient client, AccountObject data, AckRequest ackRequest) {
                try {
                    UserImpl user = AccountServiceImpl.getInstance().findUserBySessionId(client.getSessionId());
                    data.setName(user.getName());
                    data.setAnswer("Победы: " + user.getWins() + ", Поражения : " + user.getLose());
                    System.out.println(data.getName());
                    System.out.println(data.getAnswer());
                } catch (NullPointerException e) {
                    data.setAnswer("failure");
                }
                server.getClient(client.getSessionId()).sendEvent("userInfo", data);
            }
        });

        server.addEventListener("sendToChat", ChatObject.class, new DataListener<ChatObject>() {
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
                UserImpl user = AccountServiceImpl.getInstance().findUserBySessionId(client.getSessionId());
                data.setName(user.getName());
                server.getBroadcastOperations().sendEvent("msgFromChat", data);
            }
        });

        server.addEventListener("findGame", FindGameObject.class, new DataListener<FindGameObject>() {
            public void onData(SocketIOClient client, FindGameObject data, AckRequest ackRequest) {
                UserImpl user = AccountServiceImpl.getInstance().findUserBySessionId(client.getSessionId());
                try{
                    Room gameRoom = MatchMaking.getInstance().findRoom(1);
                    gameRoom.addClient(user);
                    data.setAnswer("gameIsReady");
                    data.setUuid(gameRoom.getId().toString());
                    for (UserImpl player: gameRoom.getClients()) {
                        server.getClient(player.getCurrentSessionId()).sendEvent("findGame", data);
                    }
                } catch(NullPointerException e){
                    Room gameRoom = new Room();
                    Game game = new Game(server, gameRoom.getClients());
                    gameRoom.setGame(game);
                    gameRoom.addClient(user);
                    MatchMaking.getInstance().addAvailableRoom(gameRoom);
                    data.setAnswer("roomCreate");
                    server.getClient(client.getSessionId()).sendEvent("findGame", data);
                }
            }
        });

        server.addEventListener("roomInfo", RoomObject.class, new DataListener<RoomObject>() {
            public void onData(SocketIOClient client, RoomObject data, AckRequest ackRequest) {
                try {
                    Room room = MatchMaking.getInstance().findRoomById(data.getId());
                    data.setClientName1(room.getClients().get(0).getName());
                    data.setClientName2(room.getClients().get(1).getName());
                    server.getBroadcastOperations().sendEvent("roomInfo", data);
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        });

        server.addEventListener("readyToFight", AccountObject.class, new DataListener<AccountObject>() {
            public void onData(SocketIOClient client, AccountObject data, AckRequest ackRequest) {
                try {
                    Room room = MatchMaking.getInstance().findRoomById(data.getId());
                    room.findUserBySessionId(client.getSessionId()).setReadyToFight(true);

                    if (room.allClientsReadyToFight()){
                        data.setAnswer("allReady");
                        for (UserImpl player: room.getClients()) {
                            server.getClient(player.getCurrentSessionId()).sendEvent("readyToFight", data);
                        }
                        room.getGame().preGame(2);
                    } else {
                        data.setAnswer("notAllReady");
                        server.getClient(client.getSessionId()).sendEvent("readyToFight", data);
                    }
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        });

        server.addEventListener("endGame", RoomObject.class, new DataListener<RoomObject>() {
            public void onData(SocketIOClient client, RoomObject data, AckRequest ackRequest) {
                System.out.println("id:" + data.getId());
                try {
                    Room room = MatchMaking.getInstance().findRoomById(data.getId());
                    UserImpl user = AccountServiceImpl.getInstance().findUserBySessionId(client.getSessionId());
                    user.setClicks(data.getClicks());
                    System.out.println(user.getName() + user.getClicks());
                    user.setGameFinish(true);
                    room.getGame().updateCounterPlayers();
                    for (UserImpl clients : room.getClients()) {
                        System.out.println(user.isGameFinish());
                    }
                    System.out.println(room.getGame().getCounterPlayers());
                    if (room.getGame().getCounterPlayers() == 2){
                        room.getGame().endGame();
                    }
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        });

        server.addDisconnectListener(new DisconnectListener() {
            public void onDisconnect(SocketIOClient client) {
                for (UserImpl user: StorageImpl.getInstance().getUsersOnline()) {
                    if (user.getCurrentSessionId().equals(client.getSessionId())) {
                        AccountServiceImpl.getInstance().refuse(user.getCurrentSessionId());
                        break;
                    }
                }
                AccountObject data = new AccountObject();
                String toData = String.valueOf(AccountServiceImpl.getInstance().getUsersOnline());
                data.setAnswer(toData);
                server.getBroadcastOperations().sendEvent("amountOfPlayers", data);
            }
        });

        server.addConnectListener(new ConnectListener() {
            public void onConnect(SocketIOClient socketIOClient) {
            }
        });

        server.start();
        Thread.sleep(Integer.MAX_VALUE);
        server.stop();
    }
}
