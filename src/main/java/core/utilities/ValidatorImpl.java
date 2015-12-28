package core.utilities;

import core.accountService.AccountServiceImpl;
import core.accountService.UserImpl;
import core.storageService.StorageImpl;

import java.util.List;

/**
 * Created by Zver on 09.12.2015.
 */
public class ValidatorImpl {
    public boolean userIsExist(UserImpl seekingUser) {
        boolean result = StorageImpl.getInstance().userIsExist(seekingUser.getName());
        return result;
    }

    public boolean correctUserData(String userName, String pass) {
        UserImpl user = AccountServiceImpl.getInstance().findUserByName(userName);
        if (user.getPassword().equals(pass)) {
            return true;
        } else {
            return false;
        }

    }
}
