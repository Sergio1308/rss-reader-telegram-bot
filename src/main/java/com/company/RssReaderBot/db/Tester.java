package com.company.RssReaderBot.db;

import com.company.RssReaderBot.db.models.UsersDB;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Tester {
    public static void main(String[] args) {
        Tester tester = new Tester();
//        tester.addNewUser(132, "ua");
        tester.loginUser(131);

    }

    private void loginUser(int userId) {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        UsersDB usersDB = new UsersDB(userId);
        ResultSet resultSet = databaseHandler.getUser(usersDB);
        int counter = 0;
//        databaseHandler.createTables();
        /*
        usernames_list = [user[0] for user in database.get_all_users()]
        if username not in usernames_list:
            database.add_new_user(username)
         */
        try {
            if (resultSet.next()) {
                System.out.println("Found userId " + userId);
                ResultSetMetaData metaData = resultSet.getMetaData();
                int count = metaData.getColumnCount();
                for (int i = 1; i < count; i++) {
                    // int type = metaData.getColumnType(i);
                    // if (type == Types.VARCHAR || type == Types.CHAR) Types.INTEGER
                    System.out.println(metaData.getColumnTypeName(i));
                    String columnName = metaData.getColumnName(i);
                    System.out.println("column name: " + columnName);
                    System.out.println("name: " + resultSet.getString(columnName));
                }
            } else {
                System.out.println("Not found");
                addNewUser(userId, "null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addNewUser(int id, String langCode) {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        UsersDB usersDB = new UsersDB(id, langCode);
        usersDB.setUsername("lolkekhaha");
        usersDB.setFirstName("somethingbruh");
        databaseHandler.addUser(usersDB);
    }
}
