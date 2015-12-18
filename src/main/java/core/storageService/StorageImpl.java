package core.storageService;

import com.corundumstudio.socketio.SocketIOClient;
import core.accountService.UserImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Zver on 13.12.2015.
 */
public class StorageImpl {
    private static volatile StorageImpl instance;
    volatile List<UserImpl> users = new ArrayList();
    volatile List<UUID> usersOnline = new ArrayList();

    private StorageImpl() {

    }

    public static StorageImpl getInstance() {
        if (instance == null) {
            synchronized (StorageImpl.class) {
                if (instance == null) instance = new StorageImpl();
            }
        }
        return instance;
    }

    public void addNew(UserImpl user) {
        users.add(user);
    }

    public void addToUsersOnline(SocketIOClient user) {
        usersOnline.add(user.getSessionId());
    }

    public void removeToUsersOnline(UUID user) {
        usersOnline.remove(user);
    }

    public List<UserImpl> getUsers() {
        return this.users;
    }

    public int getAmountUsersOnline() {
        return usersOnline.size();
    }

    public List<UUID> getUsersOnline() {
        return this.usersOnline;
    }

}
