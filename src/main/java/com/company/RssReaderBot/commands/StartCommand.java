package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.db.DatabaseHandler;
import com.company.RssReaderBot.db.DatabaseVars;
import com.company.RssReaderBot.db.models.UsersDB;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.StartMenuInlineKeyboard;
import com.company.RssReaderBot.parser.RssParser;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class StartCommand implements Command<Long> {

    private String getAnswer() {
        return "\t<Greetings text>\n<info>\nLet's start. Send me RSS URL below\uD83D\uDC47";
    }

    private static boolean hasEntered;

    public static boolean hasEntered() {
        return hasEntered;
    }

    public static void setEntered(boolean condition) {
        hasEntered = condition;
    }

    @Override
    public void execute(Long chatId, Long messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new StartMenuInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        messageSender.sendEditMessageText(chatId, messageId, "Send me RSS URL below\uD83D\uDC47",  markupInline);
        setEntered(true);
    }

    public void execute(Update update) {
        sendStartMessage(update);
    }

    public void sendStartMessage(Update update) {
        loginUser(update.getMessage().getFrom());
        System.out.println(RssParser.getRssUrl());

        InlineKeyboardCreator inlineKeyboardCreator = new StartMenuInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        messageSender.sendText(update.getMessage().getChatId(), getAnswer(),  markupInline);
        setEntered(true);
    }

    private void loginUser(User user) {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        long userId = user.getId();
        UsersDB usersDB = new UsersDB(userId, user.getLanguageCode());
        System.out.println("Trying to login user " + userId + " lang=" + user.getLanguageCode());
        ResultSet resultSet = databaseHandler.getUser(usersDB);
//        databaseHandler.createTables();
        /*
        usernames_list = [user[0] for user in database.get_all_users()]
        if username not in usernames_list:
            database.add_new_user(username)
         */
        try {
            if (resultSet.next()) {
                long usersUserid = resultSet.getInt(DatabaseVars.USERS_USERID);
                String usersUsername = resultSet.getString(DatabaseVars.USERS_USERNAME);
                String usersFirstName = resultSet.getString(DatabaseVars.USERS_FIRSTNAME);
                String usersLanguageCode = resultSet.getString(DatabaseVars.USERS_LANGUAGE_CODE);
                System.out.println("Found userId from DB:\n" + usersUserid + "\n" + usersUsername + "\n" +
                        usersFirstName + "\n" + usersLanguageCode);
                setValue(usersDB::setUsername, usersUsername);
                setValue(usersDB::setFirstName, usersFirstName);
                setValue(usersDB::setFirstName, usersLanguageCode);
            } else {
                System.out.println("Not found. Adding to DB...");
                setValue(usersDB::setUsername, user.getUserName());
                setValue(usersDB::setFirstName, user.getFirstName());
                addNewUser(usersDB);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // return usersDB
    }

    private void addNewUser(UsersDB usersDB) {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
//        setValue(usersDB::setUsername, user.getUserName());
//        setValue(usersDB::setFirstName, user.getFirstName());
        databaseHandler.addUser(usersDB);
        System.out.println("successfully added to db: " + usersDB);
    }

    /**
     * Set values via setter method if it is not null. Useful for partial updates
     * @param setter method
     * @param value to set
     * @param <T> generic
     */
    private <T> void setValue(Consumer<T> setter, T value) {
        if (value != null) {
            System.out.println("successfully set value " + value + " with setter" + setter);
            setter.accept(value);
        }
    }
}
