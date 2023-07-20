package com.company.RssReaderBot.commands;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserInputCommandConfiguration {
    @Bean
    @Qualifier("titleCommand")
    public UserInputCommand titleCommand() {
        String answer = "Enter a correct title of the item below (part or full name)\uD83D\uDC47:";
        return new UserInputCommand(answer);
    }

    @Bean
    @Qualifier("dateCommand")
    public UserInputCommand dateCommand() {
        String answer = "Enter a correct date in the format \"dd-MM-yyyy\" " +
                "\nFor example, 10-05-2023 \uD83D\uDC47";
        return new UserInputCommand(answer);
    }
}
