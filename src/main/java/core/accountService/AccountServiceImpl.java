package core.accountService;

import com.corundumstudio.socketio.SocketIOClient;
import core.storageService.StorageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Zver on 09.12.2015.
 */
public class AccountServiceImpl implements AccountService {

    private static volatile AccountServiceImpl instance;
    volatile List<UserImpl> users = new ArrayList();
    volatile long lastUserId = 1;

    private AccountServiceImpl() {

    }

    public static AccountServiceImpl getInstance() {
        if (instance == null) {
            synchronized (AccountServiceImpl.class) {
                if (instance == null) instance = new AccountServiceImpl();
            }
        }
        return instance;
    }

    public long getLastUserId() {
        return lastUserId;
    }

    public void setLastUserId(long lastUserId) {
        this.lastUserId = lastUserId;
    }

    public void registration(UserImpl user) {
        StorageImpl.getInstance().addNew(user);
    }

    public void auth(UserImpl user) {
        StorageImpl.getInstance().addToUsersOnline(user);
    }

    public void refuse(UUID id) {
        StorageImpl.getInstance().removeToUsersOnline(id);
    }

    public List<UserImpl> getUsers() {
        return StorageImpl.getInstance().getUsers();
    }

    public int getUsersOnline() {
        return StorageImpl.getInstance().getAmountUsersOnline();
    }

    public UserImpl findUserByName(String name) {
        UserImpl seekingUser = StorageImpl.getInstance().finUserByName(name);
        return seekingUser;
    }

    public UserImpl findUserBySessionId(UUID id) {
        UserImpl seekingUser =  StorageImpl.getInstance().findBySessionId(id);
        return seekingUser;
    }

    public String accountInfo(String user) {
        UserImpl seekingUser = findUserByName(user);
        return seekingUser.getWins() + ":" + seekingUser.getLose();
    }
}
