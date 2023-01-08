package com.company.RssReaderBot.db;

import com.company.RssReaderBot.db.config.Config;
import com.company.RssReaderBot.db.models.UsersDB;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseHandler {

    @Getter
    @Autowired
    private Config config;

    Connection connection;

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String connectionString = config.getDbUrl() + "//" + config.getDbHost() + ":" + config.getDbPort()
                + "/" + config.getDbName() + "?" + config.getDbProperty();
        connection = DriverManager.getConnection(connectionString, config.getDbUser(), config.getDbPass());
        System.out.println(connection);
        return connection;
    }

    public void addUser(UsersDB usersDB) {
        // transaction
        String query1 =
                "INSERT INTO " + DatabaseVars.USERS_TABLE + "(" + DatabaseVars.USERS_USERID + "," +
                DatabaseVars.USERS_USERNAME + "," + DatabaseVars.USERS_FIRSTNAME + "," +
                DatabaseVars.USERS_LANGUAGE_CODE + ")VALUES(?,?,?,?);";
        String query2 =
                "INSERT INTO " + DatabaseVars.USERS_PERSONAL_TABLE + "(" + DatabaseVars.USERS_USERID + ")VALUES(?);";
        String query3 =
                "INSERT INTO " + DatabaseVars.USERS_PERSONAL_ITEMS_TABLE + "(" +
                DatabaseVars.ID_USERS_PERSONAL + ") SELECT " + DatabaseVars.ID_USERS_PERSONAL +
                " FROM " + DatabaseVars.USERS_PERSONAL_TABLE + " WHERE " + DatabaseVars.USERS_USERID + "=?";
        try {
            PreparedStatement preparedStatement1 = getConnection().prepareStatement(query1);
            PreparedStatement preparedStatement2 = getConnection().prepareStatement(query2);
            PreparedStatement preparedStatement3 = getConnection().prepareStatement(query3);
            preparedStatement1.setLong(1, usersDB.getUserId());
            preparedStatement1.setString(2, usersDB.getUsername());
            preparedStatement1.setString(3, usersDB.getFirstName());
            preparedStatement1.setString(4, usersDB.getLanguageCode());
            preparedStatement2.setLong(1, usersDB.getUserId());
            preparedStatement3.setLong(1, usersDB.getUserId());
            preparedStatement1.executeUpdate();
            preparedStatement2.executeUpdate();
            preparedStatement3.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateUserData(long userId) {
        // TODO
        String query = "UPDATE " + DatabaseVars.USERS_TABLE + " SET " + DatabaseVars.USERS_USERNAME + "=?," +
                DatabaseVars.USERS_FIRSTNAME + "=?," + DatabaseVars.USERS_LANGUAGE_CODE + "=?";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateRssUrl(long userId, String url) {
        String query =
                "UPDATE " + DatabaseVars.USERS_PERSONAL_TABLE +
                        " SET " + DatabaseVars.RSS_URL + "=?" +
                        " WHERE " + DatabaseVars.USERS_USERID + "=?;";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, url);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
            System.out.println(userId + " user with url=" + url + " added to table");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        String createUsers =
                "CREATE TABLE IF NOT EXISTS users" +
                "(" +
                    "userid INT NOT NULL," +
                    "username VARCHAR(32) NULL," +
                    "firstname VARCHAR(45) NULL," +
                    "language_code VARCHAR(5) NOT NULL," +
                    "PRIMARY KEY (userid)," +
                    "UNIQUE KEY userid_UNIQUE (userid)," +
                    "UNIQUE KEY username_UNIQUE (username)" +
                ") DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
        String createUsersPersonal =
                "CREATE TABLE IF NOT EXISTS users_personal " +
                "(" +
                    "id_users_personal INT NOT NULL AUTO_INCREMENT," +
                    "userid INT NOT NULL," +
                    "rss_url VARCHAR(255) NULL," +
                    "query_item_title VARCHAR(64) NULL," +
                    "PRIMARY KEY (id_users_personal)," +
                    "UNIQUE KEY userid_UNIQUE (userid)," +
                    "UNIQUE KEY id_users_personal_UNIQUE (id_users_personal)," +
                    "CONSTRAINT userid FOREIGN KEY (userid) REFERENCES users (userid) ON DELETE CASCADE" +
                ") DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
        String createUsersPersonalItems =
                "CREATE TABLE IF NOT EXISTS users_personal_items " +
                "(" +
                    "id_users_personal INT NOT NULL," +
                    "item_title VARCHAR(128) NULL," +
                    "item_description VARCHAR(10000) NULL," +
                    "item_url VARCHAR(255) NULL," +
                    "item_date timestamp NULL," +
                    "PRIMARY KEY (id_users_personal)," +
                    "UNIQUE KEY id_users_personal_UNIQUE (id_users_personal)," +
                    "UNIQUE KEY item_title_UNIQUE (item_title)," +
                    "CONSTRAINT id_users_personal FOREIGN KEY (id_users_personal)" +
                    "REFERENCES users_personal (id_users_personal) ON DELETE CASCADE" +
                ") DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
        try {
//            PreparedStatement preparedStatement = getConnection().prepareStatement(createUsers);
            getConnection().prepareStatement(createUsers).executeUpdate();
            getConnection().prepareStatement(createUsersPersonal).executeUpdate();
            getConnection().prepareStatement(createUsersPersonalItems).executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getUser(UsersDB usersDB) {
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + DatabaseVars.USERS_TABLE + " WHERE " +
                DatabaseVars.USERS_USERID + "=?";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setLong(1, usersDB.getUserId());
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public List<UsersDB> getUsers() {
        ResultSet resultSet = null;
        List<UsersDB> users = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseVars.USERS_TABLE;
        try {
            Statement statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                long userid = resultSet.getInt(DatabaseVars.USERS_USERID);
                String language_code = resultSet.getString(DatabaseVars.USERS_LANGUAGE_CODE);
                UsersDB usersDB = new UsersDB(userid, language_code);
                usersDB.setUsername(resultSet.getString(DatabaseVars.USERS_USERNAME));
                usersDB.setFirstName(resultSet.getString(DatabaseVars.USERS_FIRSTNAME));
//                setValue(usersDB::setUsername, resultSet.getString(DatabaseVars.USERS_USERNAME));
//                setValue(usersDB::setFirstName, resultSet.getString(DatabaseVars.USERS_FIRSTNAME));
                users.add(usersDB);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    public ResultSet getUserRssUrl(long userId) {
        ResultSet resultSet = null;
        String query =
                "SELECT " + DatabaseVars.RSS_URL + " FROM " + DatabaseVars.USERS_PERSONAL_TABLE +
                " WHERE " + DatabaseVars.USERS_USERID + "=?";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setLong(1, userId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                String columnName = metaData.getColumnName(1);
                System.out.println(resultSet.getString(columnName));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
