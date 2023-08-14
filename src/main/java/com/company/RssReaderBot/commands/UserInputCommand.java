package com.company.RssReaderBot.commands;

import com.company.RssReaderBot.controllers.CallbackDataConstants;
import com.company.RssReaderBot.controllers.MessageController;
import com.company.RssReaderBot.controllers.core.BotState;
import com.company.RssReaderBot.inlinekeyboard.EnteringItemTitleInlineKeyboard;
import com.company.RssReaderBot.inlinekeyboard.InlineKeyboardCreator;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;

/**
 * Implementation of the Command interface that handles user input for various bot states.
 * This class creates appropriate responses based on the current state and user input requirements.
 */
public class UserInputCommand implements Command<Message> {

    @Getter
    private final String answer;

    @Getter
    private final BotState currentState;

    /**
     * Creates a UserInputCommand instance with the specified current state and response answer.
     * @param currentState  The current bot state associated with the command.
     * @param answer        The response answer to be displayed to the user.
     */
    public UserInputCommand(BotState currentState, String answer) {
        this.currentState = currentState;
        this.answer = answer;
    }

    @Override
    public BaseRequest<?, ?> execute(Message message) {
        MessageController.getUserStates().put(message.chat().id(), currentState);
        if (currentState.equals(BotState.ENTERING_TITLE) || currentState.equals(BotState.ENTERING_DATE)) {
            InlineKeyboardCreator inlineKeyboardCreator = new EnteringItemTitleInlineKeyboard();
            InlineKeyboardMarkup markupInline = inlineKeyboardCreator.createInlineKeyboard();
            return new EditMessageText(message.chat().id(), message.messageId(), answer).replyMarkup(markupInline);
        }
        return new SendMessage(message.chat().id(), getAnswer())
                .replyMarkup(new ForceReply(true)
                        .inputFieldPlaceholder(CallbackDataConstants.SUB_FEED_SAMPLE)
                        .selective(true));
    }
}
