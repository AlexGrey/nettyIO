package modules.gameMechanics;

import com.corundumstudio.socketio.SocketIOServer;
import core.accountService.AccountServiceImpl;
import core.accountService.UserImpl;
import serializableObj.AccountObject;

import java.util.List;

/**
 * Created by Zver on 19.12.2015.
 */
public class Game {
    SocketIOServer server;
    volatile int preTimer;
    volatile int gameTimer;
    volatile int counterPlayers = 0;
    List<UserImpl> users;
    UserImpl winner;
    UserImpl looser;

    public Game(SocketIOServer server, List<UserImpl> users) {
        this.server = server;
        this.users = users;
    }

    public void preGame(int seconds) {
        preTimer = seconds;
        AccountObject obj = new AccountObject();
        for (int i = 0; i < seconds; i++) {
            obj.setAnswer(String.valueOf(preTimer));
            for (UserImpl user : users) {
                server.getClient(user.getCurrentSessionId()).sendEvent("preGame", obj);
            }
            preTimer--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        obj.setAnswer("startGame");
        for (UserImpl user : users) {
            server.getClient(user.getCurrentSessionId()).sendEvent("startGame", obj);
        }
        game(3);
    }

    public void game(int seconds) {
        gameTimer = seconds;
        AccountObject obj = new AccountObject();
        for (int i = 0; i < seconds; i++) {
            obj.setAnswer(String.valueOf(gameTimer));
            for (UserImpl user : users) {
                server.getClient(user.getCurrentSessionId()).sendEvent("game", obj);
            }
            gameTimer--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        obj.setAnswer("gameOver");
        for (UserImpl user : users) {
            server.getClient(user.getCurrentSessionId()).sendEvent("endGame", obj);
        }
    }

    public synchronized void endGame() {
        AccountObject obj = new AccountObject();
        UserImpl user1 = users.get(0);
        UserImpl user2 = users.get(1);;
        System.out.println("клики игроков:");
        if (user1.getClicks() > user2.getClicks()) {
            winner = user1;
            looser = user2;
            user1.setWins();
            user2.setLose();
            obj.setName(winner.getName());

            obj.setAnswer("Вы проиграли!");
            server.getClient(user2.getCurrentSessionId()).sendEvent("resultGame", obj);

            obj.setAnswer("Вы выиграли!");
            server.getClient(user1.getCurrentSessionId()).sendEvent("resultGame", obj);

        } else {
            winner = user2;
            looser = user1;
            user2.setWins();
            user1.setLose();

            obj.setName(winner.getName());

            obj.setAnswer("Вы проиграли!");
            server.getClient(user1.getCurrentSessionId()).sendEvent("resultGame", obj);

            obj.setAnswer("Вы выиграли!");
            server.getClient(user2.getCurrentSessionId()).sendEvent("resultGame", obj);
        }
        for (UserImpl user : AccountServiceImpl.getInstance().getUsers()) {
            user.setClicks(0);
            user.setReadyToFight(false);
            user.setGameFinish(false);
        }
    }

    public int getCounterPlayers() {
        return counterPlayers;
    }

    public void updateCounterPlayers() {
        this.counterPlayers++;
    }

    public UserImpl getWinner() {
        return winner;
    }

    public void setWinner(UserImpl winner) {
        this.winner = winner;
    }
}
