package core.accountService;

import java.util.UUID;

/**
 * Created by Zver on 09.12.2015.
 */
public class UserImpl {
    long id;
    UUID currentSessionId;
    String name;
    String password;
    boolean isAuth;
    boolean looser = true;
    int wins = 0;
    int lose = 0;
    boolean readyToFight = false;
    boolean gameFinish = false;
    int clicks;

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

    public void setWins() {
        this.wins++;
    }

    public int getLose() {
        return lose;
    }

    public void setLose() {
        this.lose++;
    }

    public boolean isReadyToFight() {
        return readyToFight;
    }

    public UUID getCurrentSessionId() {
        return currentSessionId;
    }

    public void setCurrentSessionId(UUID currentSessionId) {
        this.currentSessionId = currentSessionId;
    }

    public void setReadyToFight(boolean readyToFight) {
        this.readyToFight = readyToFight;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public boolean isLooser() {
        return looser;
    }

    public void setLooser(boolean looser) {
        this.looser = looser;
    }

    public boolean isGameFinish() {
        return gameFinish;
    }

    public void setGameFinish(boolean gameFinish) {
        this.gameFinish = gameFinish;
    }
}
