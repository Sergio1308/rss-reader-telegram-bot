package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class SubscribeCommand implements Command<Message> {

    private static boolean hasEntered;

    private String getAnswer() {
        return "Send me a valid URL below\uD83D\uDC47\nIf you want an example of an RSS-feed or help, " +
                "use /help";
    }

    public static boolean hasEntered() {
        return hasEntered;
    }

    public static void setEntered(boolean condition) {
        hasEntered = condition;
    }

    @Override
    public BaseRequest<?, ?> execute(Message message) {
        setEntered(true);
        return new SendMessage(message.chat().id(), getAnswer())
                .replyMarkup(new ForceReply(true)
                        .inputFieldPlaceholder(CallbackDataConstants.SUB_FEED_SAMPLE)
                        .selective(true));
    }
}
