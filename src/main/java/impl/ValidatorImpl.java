package impl;

import java.util.List;

/**
 * Created by Zver on 09.12.2015.
 */
public class ValidatorImpl {
    public boolean userIsExist(UserImpl seekingUser, List<UserImpl> users) {
        for (UserImpl userFromList : users) {
            if (userFromList.getName().equals(seekingUser.getName())) {
                return true;
            }
        }
        return false;
    }
}
