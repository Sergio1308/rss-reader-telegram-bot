package com.company.RssReaderBot.db.services;

import com.company.RssReaderBot.db.entities.UserDB;
import com.company.RssReaderBot.db.entities.UserSettings;
import com.company.RssReaderBot.db.repositories.UserRepository;
import com.pengrad.telegrambot.model.User;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Getter
    private UserDB userDB;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User telegramUser) {
        userDB = new UserDB(telegramUser.id(), telegramUser.languageCode());
        UserSettings userSettings = new UserSettings(userDB);
        setValue(userDB::setUsername, telegramUser.username());
        setValue(userDB::setFirstName, telegramUser.firstName());
        if (!userRepository.existsById(userDB.getUserid())) {
            setValue(userDB::setUserSettings, userSettings);
        }
        userRepository.save(userDB);
    }

    public UserDB findUser(Long userid) {
        return userRepository.findById(userid).orElseThrow();
    }

    /**
     * Set values via setter method if it is not null, used to modify an object via setter
     * @param setter method that changes the state of an object
     * @param value to set
     * @param <T> generic
     */
    public <T> void setValue(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
