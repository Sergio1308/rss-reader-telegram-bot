package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.db.DatabaseHandler;
import com.company.RssReaderBot.db.DatabaseVars;
import com.company.RssReaderBot.db.config.Config;
import com.company.RssReaderBot.db.models.UsersDB;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.StartMenuInlineKeyboard;
import com.company.RssReaderBot.parser.RssParser;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

@Component
public class StartCommand implements Command<Long, Integer> {

    @Autowired
    private DatabaseHandler databaseHandler;

    private static boolean hasEntered;

    private String getAnswer() {
        return "\t<Greetings text>\n<info>\nLet's start. Send me RSS URL below\uD83D\uDC47";
    }

    public static boolean hasEntered() {
        return hasEntered;
    }

    public static void setEntered(boolean condition) {
        hasEntered = condition;
    }

    @Override
    public BaseRequest<EditMessageText, BaseResponse> execute(Long chatId, Integer messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new StartMenuInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        setEntered(true);
        return new EditMessageText(chatId, messageId, "Send me RSS URL below\uD83D\uDC47")
                .replyMarkup(markupInline);
    }

    public BaseRequest<SendMessage, SendResponse> execute(Update update) {
        loginUser(update.message().from());
        System.out.println(RssParser.getRssUrl());

        InlineKeyboardCreator inlineKeyboardCreator = new StartMenuInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
        setEntered(true);
        return new SendMessage(update.message().chat().id(), getAnswer()).replyMarkup(markupInline);
    }

    private void loginUser(User user) {
        long userId = user.id();
        UsersDB usersDB = new UsersDB(userId, user.languageCode());
        System.out.println("Trying to login user " + userId + " lang=" + user.languageCode());
        System.out.println(databaseHandler);
        ResultSet resultSet = databaseHandler.getUser(usersDB);
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
                setValue(usersDB::setUsername, user.username());
                setValue(usersDB::setFirstName, user.firstName());
                addNewUser(usersDB);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addNewUser(UsersDB usersDB) {
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
