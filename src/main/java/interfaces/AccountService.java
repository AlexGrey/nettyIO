package interfaces;

import com.corundumstudio.socketio.SocketIOClient;
import impl.UserImpl;

import java.util.List;

/**
 * Created by Zver on 07.12.2015.
 */
public interface AccountService {
    void registration(UserImpl user);
    void auth();
    List<UserImpl> getUsers();
}
