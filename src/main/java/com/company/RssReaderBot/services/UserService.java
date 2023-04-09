package com.company.RssReaderBot.services;

import com.company.RssReaderBot.db.models.UserDB;
import com.company.RssReaderBot.db.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void addUser(UserDB userDB) {
        userRepository.save(userDB);
    }

    public UserDB findUser(Long userid) {
        return userRepository.findById(userid).orElseThrow();
    }
}
