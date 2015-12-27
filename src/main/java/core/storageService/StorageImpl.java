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
    volatile List<UserImpl> usersOnline = new ArrayList();

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

    public void addToUsersOnline(UserImpl user) {
        usersOnline.add(user);
    }

    public void removeToUsersOnline(UUID sessionId) {
        usersOnline.remove(findBySessionId(sessionId));
    }

    public List<UserImpl> getUsers() {
        return this.users;
    }

    public int getAmountUsersOnline() {
        return usersOnline.size();
    }

    public List<UserImpl> getUsersOnline() {
        return this.usersOnline;
    }

    public UserImpl findBySessionId(UUID sessionId) {
        UserImpl user = null;
        for (UserImpl userOnline : usersOnline) {
            if (userOnline.getCurrentSessionId().equals(sessionId)) {
                user = userOnline;
            }
        }
        return user;
    }

}
