package core.storageService;

import core.accountService.UserImpl;

import java.util.ArrayList;
import java.util.List;

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

    public void removeToUsersOnline(UserImpl user) {
        usersOnline.remove(user);
    }

    public List<UserImpl> getUsers() {
        return this.users;
    }

    public int getUsersOnline() {
        return usersOnline.size();
    }
}
