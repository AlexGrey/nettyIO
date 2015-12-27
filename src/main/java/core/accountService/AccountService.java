package core.accountService;

import com.corundumstudio.socketio.SocketIOClient;
import core.accountService.UserImpl;

import java.util.List;
import java.util.UUID;

/**
 * Created by Zver on 07.12.2015.
 */
public interface AccountService {
    void registration(UserImpl user);
    void auth(UserImpl user);
    List<UserImpl> getUsers();
}
