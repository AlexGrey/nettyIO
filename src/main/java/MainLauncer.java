import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import core.accountService.AccountServiceImpl;
import core.accountService.UserImpl;
import core.utilities.ValidatorImpl;
import serializableObj.AccountObject;

/**
 * Created by Zver on 13.12.2015.
 */
public class MainLauncer {
    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(4567);
        final SocketIOServer server = new SocketIOServer(config);
        final ValidatorImpl validator = new ValidatorImpl();

        server.addEventListener("reg", AccountObject.class, new DataListener<AccountObject>() {
            public void onData(SocketIOClient client, AccountObject data, AckRequest ackRequest) {
                UserImpl user = new UserImpl(data.getName(), data.getPassword());
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
                        AccountServiceImpl.getInstance().auth(user);
                        data.setAnswer("success");
                    } else {
                        data.setAnswer("failure");
                    }
                } catch (NullPointerException e) {
                    data.setAnswer("failure");
                }
                server.getBroadcastOperations().sendEvent("auth", data);
            }
        });

        server.addDisconnectListener(new DisconnectListener() {
            public void onDisconnect(SocketIOClient client) {

            }
        });

        server.start();
        Thread.sleep(Integer.MAX_VALUE);
        server.stop();
    }
}
