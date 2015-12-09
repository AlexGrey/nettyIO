package impl;

import com.corundumstudio.socketio.SocketIOClient;
import interfaces.AccountService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zver on 09.12.2015.
 */
public class AccountServiceImpl implements AccountService {

    private static volatile AccountServiceImpl instance;

    private AccountServiceImpl(){

    }

    public static AccountServiceImpl getInstance() {
        if (instance == null) {
            synchronized (AccountServiceImpl.class) {
                if (instance == null) instance = new AccountServiceImpl();
            }
        }
        return instance;
    }


        List<UserImpl> users = new ArrayList();

    public void registration(UserImpl user) {
        users.add(user);
    }

    public void auth() {

    }

    public List<UserImpl> getUsers() {
        return this.users;
    }

    public void remove() {

    }
}
