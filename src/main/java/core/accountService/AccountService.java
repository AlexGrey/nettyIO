package core.accountService;

import core.accountService.UserImpl;

import java.util.List;

/**
 * Created by Zver on 07.12.2015.
 */
public interface AccountService {
    void registration(UserImpl user);
    void auth(UserImpl user);
    List<UserImpl> getUsers();
}
