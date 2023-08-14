package com.company.RssReaderBot.commands.config;

import com.company.RssReaderBot.commands.UserInputCommand;
import com.company.RssReaderBot.controllers.core.BotState;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that defines Spring beans for different UserInputCommand instances.
 * These beans are used to create instances of UserInputCommand with specific states and answers.
 */
@Configuration
public class CommandConfiguration {
    @Bean
    @Qualifier("titleCommand")
    public UserInputCommand titleCommand() {
        String answer = "Enter a correct title of the item below (part or full name)\uD83D\uDC47:";
        return new UserInputCommand(BotState.ENTERING_TITLE, answer);
    }

    @Bean
    @Qualifier("dateCommand")
    public UserInputCommand dateCommand() {
        String answer = "Enter a correct date in the format \"dd-MM-yyyy\" " +
                "\nFor example, 10-05-2023 \uD83D\uDC47";
        return new UserInputCommand(BotState.ENTERING_DATE, answer);
    }

    @Bean
    @Qualifier("subscribeCommand")
    public UserInputCommand subscribeCommand() {
        String answer = "Send me a valid URL below\uD83D\uDC47\nIf you want an example of an RSS-feed or help, " +
                "use /help";
        return new UserInputCommand(BotState.SUBSCRIBE, answer);
    }

    @Bean
    @Qualifier("getItemsWithSpecifiedUrlCommand")
    public UserInputCommand getItemsWithSpecifiedUrlCommand() {
        String answer = "Send me the URL of the feed you want to get the items from\uD83D\uDC47\n" +
                "If you want an example of an RSS-feed or help, use /help";
        return new UserInputCommand(BotState.ENTERING_SPECIFIC_URL, answer);
    }
}
