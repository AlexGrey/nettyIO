import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import core.accountService.AccountServiceImpl;
import core.accountService.UserImpl;
import core.storageService.StorageImpl;
import core.utilities.ValidatorImpl;
import serializableObj.AccountObject;
import serializableObj.ChatObject;

import java.util.UUID;

/**
 * Created by Zver on 13.12.2015.
 */
public class MainLauncher {
    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(4567);
        final SocketIOServer server = new SocketIOServer(config);
        final ValidatorImpl validator = new ValidatorImpl();

        server.addEventListener("reg", AccountObject.class, new DataListener<AccountObject>() {
            public void onData(SocketIOClient client, AccountObject data, AckRequest ackRequest) {
                UserImpl user = new UserImpl(client.getSessionId(), data.getName(), data.getPassword());
                System.out.println(data.getName());
                if (!validator.userIsExist(user, AccountServiceImpl.getInstance().getUsers())) {
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
                    if (validator.correctUserData(data.getName(), data.getPassword())) {
                        AccountServiceImpl.getInstance().auth(client);
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
                    System.out.println(data);
                    UserImpl user = AccountServiceImpl.getInstance().findUserByName(data.getName());
                    data.setAnswer(user.getName() + ":" + user.getWins() + ":" + user.getLose());
                } catch (NullPointerException e) {
                    data.setAnswer("failure");
                }
                server.getClient(client.getSessionId()).sendEvent("userInfo", data);
            }
        });

        server.addEventListener("sendToChat", ChatObject.class, new DataListener<ChatObject>() {
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
                System.out.println(data.getName() + ":" + data.getMessage());
                server.getBroadcastOperations().sendEvent("msgFromChat", data);
            }
        });

        server.addDisconnectListener(new DisconnectListener() {
            public void onDisconnect(SocketIOClient client) {
                for (UUID id: StorageImpl.getInstance().getUsersOnline()) {
                    if (id.equals(client.getSessionId())) {
                        AccountServiceImpl.getInstance().refuse(id);
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
                //server.getBroadcastOperations().sendEvent();
            }
        });

        server.start();
        Thread.sleep(Integer.MAX_VALUE);
        server.stop();
    }
}
