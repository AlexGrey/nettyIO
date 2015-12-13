package core.accountService;

import core.storageService.StorageImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zver on 09.12.2015.
 */
public class AccountServiceImpl implements AccountService {

    private static volatile AccountServiceImpl instance;
    volatile List<UserImpl> users = new ArrayList();

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


    public void registration(UserImpl user) {
        StorageImpl.getInstance().addNew(user);
    }

    public void auth(UserImpl user) {
        StorageImpl.getInstance().addToUsersOnline(user);
    }

    public List<UserImpl> getUsers() {
        return StorageImpl.getInstance().getUsers();
    }

    public int getUsersOnline() {
        return StorageImpl.getInstance().getUsersOnline();
    }

    public UserImpl findUserByName(String name) {
        UserImpl seekingUser = null;
        for (UserImpl user : StorageImpl.getInstance().getUsers()) {
            if (user.getName().equals(name)) {
                seekingUser = user;
            }
        }
        return seekingUser;
    }

    public String accountInfo(String user) {
        UserImpl seekingUser = findUserByName(user);
        return seekingUser.getWins() + ":" + seekingUser.getLose();
    }
}
