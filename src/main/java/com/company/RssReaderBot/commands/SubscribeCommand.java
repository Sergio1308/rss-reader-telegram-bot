package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.controllers.CallbackQueryConstants;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;

@Component
public class SubscribeCommand implements Command<Long, Integer> {

    private static boolean hasEntered;

    private String getAnswer() {
        return "You have to reply to this message with the URL of an RSS-feed." +
                "\nSend me a valid URL below\uD83D\uDC47\nIf you want an example of an RSS-feed or help, " +
                "use /help";
    }

    public static boolean hasEntered() {
        return hasEntered;
    }

    public static void setEntered(boolean condition) {
        hasEntered = condition;
    }

    @Override
    public BaseRequest<SendMessage, SendResponse> execute(Long chatId, Integer messageId) {
        setEntered(true);
        return new SendMessage(chatId, getAnswer())
                .replyMarkup(new ForceReply(true)
                        .inputFieldPlaceholder(CallbackQueryConstants.SUB_FEED_SAMPLE)
                        .selective(true));
    }
}
