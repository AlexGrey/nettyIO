package core.accountService;

import java.util.UUID;

/**
 * Created by Zver on 09.12.2015.
 */
public class UserImpl {
    long id;
    String name;
    String password;
    boolean isAuth;
    int wins = 0;
    int lose = 0;

    public UserImpl(String name, String password) {
        this.id = AccountServiceImpl.getInstance().getLastUserId();
        AccountServiceImpl.getInstance().setLastUserId(this.id + 1);
        this.name = name;
        this.password = password;
    }

    public long getUuid() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }
}
