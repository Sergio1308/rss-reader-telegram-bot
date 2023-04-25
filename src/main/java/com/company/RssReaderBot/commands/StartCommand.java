package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.db.models.UserDB;
import com.company.RssReaderBot.db.models.UserSettings;
import com.company.RssReaderBot.db.repositories.UserRepository;
import com.company.RssReaderBot.db.repositories.UserSettingsRepository;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.company.RssReaderBot.inlinekeyboard.StartMenuInlineKeyboard;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class StartCommand implements Command<Message> {

    private final UserRepository userRepository;
    private final UserSettingsRepository userSettingsRepository;

    public StartCommand(UserRepository userRepository, UserSettingsRepository userSettingsRepository) {
        this.userRepository = userRepository;
        this.userSettingsRepository = userSettingsRepository;
    }

    public String getAnswer() {
        return "RSS Reader Bot\n\n" +
                "Using the buttons below, you can subscribe\nto the RSS feeds you want,\n" +
                "or display individual feed items\nand save them to your favorites.";
    }

    @Override
    public BaseRequest<SendMessage, SendResponse> execute(Message message) {
        InlineKeyboardCreator inlineKeyboardCreator = new StartMenuInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();

        User tgUser = message.from();
        UserDB userDB = new UserDB(tgUser.id(), tgUser.languageCode());
        setValue(userDB::setUsername, tgUser.username());
        setValue(userDB::setFirstName, tgUser.firstName());
        UserSettings userSettings = new UserSettings(userDB);
        addUser(userDB, userSettings);

        return new SendMessage(message.chat().id(), getAnswer()).replyMarkup(markupInline);
    }

    public BaseRequest<EditMessageText, BaseResponse> displayStartMenu(Long chatId, Integer messageId) {
        InlineKeyboardCreator inlineKeyboardCreator = new StartMenuInlineKeyboard();
        InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();

        return new EditMessageText(chatId, messageId, getAnswer()).replyMarkup(markupInline);
    }

    private void addUser(UserDB user, UserSettings userSettings) {
        if (userRepository.existsById(user.getUserid())) {
            userRepository.save(user);
        } else {
            userRepository.save(user);
            userSettingsRepository.save(userSettings);
        }
    }

    /**
     * Set values via setter method if it is not null, used to modify an object via setter
     * @param setter method that changes the state of an object
     * @param value to set
     * @param <T> generic
     */
    private <T> void setValue(Consumer<T> setter, T value) {
        if (value != null) {
            System.out.println("successfully set value " + value + " with setter" + setter); // todo delete
            setter.accept(value);
        }
    }
}
