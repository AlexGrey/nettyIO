package core.storageService;

import com.corundumstudio.socketio.SocketIOClient;
import core.accountService.UserImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.*;

/**
 * Created by Zver on 13.12.2015.
 */
public class StorageImpl {
    private static volatile StorageImpl instance;
    volatile List<UserImpl> users = new ArrayList();
    volatile List<UserImpl> usersOnline = new ArrayList();

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    String url = "jdbc:postgresql://127.0.0.1:5432/clickMaster";

    //Имя пользователя БД
    String name = "postgres";
    //Пароль
    String password = "481516";

    private StorageImpl() {
        try {
            //Загружаем драйвер
            Class.forName("org.postgresql.Driver");
            System.out.println("Драйвер подключен");
            //Создаём соединение
            connection = DriverManager.getConnection(url, name, password);
            System.out.println("Соединение установлено");



            /*preparedStatement = connection.prepareStatement("SELECT * FROM users");
            //Выполним запрос
            ResultSet result1 = statement.executeQuery(
                    "SELECT * FROM users");
            //result это указатель на первую строку с выборки
            //чтобы вывести данные мы будем использовать
            //метод next() , с помощью которого переходим к следующему элементу
            System.out.println("Выводим statement");
            while (result1.next()) {
                System.out.println("Номер в выборке #" + result1.getRow()
                        + "\t Номер в базе #" + result1.getString("name")
                        + "\t" + result1.getString("password"));
            }*/

        } catch (Exception e){
            System.out.println(e);
        }
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
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO users(name, password) values(?, ?)");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            //метод принимает значение без параметров
            //темже способом можно сделать и UPDATE
            preparedStatement.executeUpdate();
        } catch (Exception e){
            System.out.println(e);
        }

        users.add(user);
    }

    public void updateWins(String name) {
        int currentWins = 0;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE name = ?");
            preparedStatement.setString(1, name);
            //метод принимает значение без параметров
            //темже способом можно сделать и UPDATE
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                currentWins = result.getInt("wins");
            }

            currentWins++;

            preparedStatement = connection.prepareStatement(
                    "UPDATE users SET wins = ? WHERE name = ?");
            preparedStatement.setInt(1, currentWins);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public int getWins(String name) {
        int wins = 0;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE name = ?");
            preparedStatement.setString(1, name);
            //метод принимает значение без параметров
            //темже способом можно сделать и UPDATE
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                wins = result.getInt("wins");
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return wins;
    }

    public void updateLose(String name) {
        int currentLose = 0;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE name = ?");
            preparedStatement.setString(1, name);
            //метод принимает значение без параметров
            //темже способом можно сделать и UPDATE
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                currentLose = result.getInt("lose");
            }

            currentLose++;

            preparedStatement = connection.prepareStatement(
                    "UPDATE users SET lose = ? WHERE name = ?");
            preparedStatement.setInt(1, currentLose);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public int getLose(String name) {
        int lose = 0;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE name = ?");
            preparedStatement.setString(1, name);
            //метод принимает значение без параметров
            //темже способом можно сделать и UPDATE
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                lose = result.getInt("lose");
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return lose;
    }

    public void addToUsersOnline(UserImpl user) {
        usersOnline.add(user);
    }

    public void removeToUsersOnline(UUID sessionId) {
        usersOnline.remove(findBySessionId(sessionId));
    }

    public List<UserImpl> getUsers() {
        return this.users;
    }

    public int getAmountUsersOnline() {
        return usersOnline.size();
    }

    public List<UserImpl> getUsersOnline() {
        return this.usersOnline;
    }

    public UserImpl findBySessionId(UUID sessionId) {
        UserImpl user = null;
        for (UserImpl userOnline : usersOnline) {
            if (userOnline.getCurrentSessionId().equals(sessionId)) {
                user = userOnline;
            }
        }
        return user;
    }

    public UserImpl finUserByName(String userName){
        String name = null;
        String password = null;
        UserImpl user = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users where name = ?");
            preparedStatement.setString(1, userName);
            //метод принимает значение без параметров
            //темже способом можно сделать и UPDATE
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                name = result.getString("name");
                password = result.getString("password");
            }
        } catch (Exception e){
            System.out.println(e);
        }


        if (name != null && password != null){
            user = new UserImpl(name, password);
        }

        return user;
    }

    public boolean userIsExist(String userName){
        boolean isExist = true;
        String findName = "";
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users where name = ?");
            preparedStatement.setString(1, userName);
            //метод принимает значение без параметров
            //темже способом можно сделать и UPDATE
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                findName = result.getString("name");
            }
        } catch (Exception e){
            System.out.println(e);
        }
        if (findName.equals("")){
            isExist = false;
        }
        return isExist;
    }

}
