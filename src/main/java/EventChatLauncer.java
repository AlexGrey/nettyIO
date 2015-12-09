import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import impl.AccountServiceImpl;
import impl.UserImpl;
import impl.ValidatorImpl;

/**
 * Created by Zver on 06.12.2015.
 */
class EventChatLauncher {
    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(4567);
        final SocketIOServer server = new SocketIOServer(config);


        server.addEventListener("boop2", ChatObject.class, new DataListener<ChatObject>() {

            public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
                String greeting = "hello " + data.getUserName() + "!";
                data.setMessage(greeting);
                server.getBroadcastOperations().sendEvent("boop2", data);
            }
        });

        server.addEventListener("registration", AccountObject.class, new DataListener<AccountObject>() {

            public void onData(SocketIOClient client, AccountObject data, AckRequest ackRequest) {
                ValidatorImpl validator = new ValidatorImpl();
                UserImpl user = new UserImpl(data.getName(), data.getPassword());
                if (!validator.userIsExist(user, AccountServiceImpl.getInstance().getUsers())) {
                    AccountServiceImpl.getInstance().registration(user);
                    data.setAnswer("пользователь с именем: " + user.getName() + " успешно зарегистрирован!");
                } else {
                    data.setAnswer("пользователь с именем: " + user.getName() + " уже существует!");
                }

                server.getBroadcastOperations().sendEvent("registration", data);
            }
        });

        server.start();
        Thread.sleep(Integer.MAX_VALUE);
        server.stop();
    }
}
