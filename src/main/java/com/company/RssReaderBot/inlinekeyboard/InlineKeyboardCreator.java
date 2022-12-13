package com.company.RssReaderBot.inlinekeyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface InlineKeyboardCreator {

    // generic
    InlineKeyboardMarkup createInlineKeyboard();

    default InlineKeyboardButton createInlineKeyboardButton(
                String buttonText,
                String buttonCallbackData
    ) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonText);
        button.setCallbackData(buttonCallbackData);
        return button;
    }

    default List<InlineKeyboardButton> createInlineKeyboardButtonRowInline(
            InlineKeyboardButton... buttons
    ) {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        Collections.addAll(rowInline, buttons);
        return rowInline;
    }

    default List<List<InlineKeyboardButton>> createInlineKeyboardButtonRowList(
            List<InlineKeyboardButton> rowInline
    ) {
        return new ArrayList<>(Collections.singletonList(rowInline));
    }

    /*
		System.out.println("--------------------\n" + "creating inline with: " + buttonsNumber + " buttons, " +
				buttonText.size() + " buttons text, " + callbackData.size() + " callbacksData.\n-----------------");
		System.out.println("Created buttons: " + rowInline.size());
		for (InlineKeyboardButton b : rowInline) {
			System.out.println("\nbutton text: " + b.getText());
			System.out.println("callback: " + b.getCallbackData());
		}
		*/
}
