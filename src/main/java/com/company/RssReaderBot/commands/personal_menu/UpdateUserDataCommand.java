package com.company.RssReaderBot.commands.personal_menu;

import com.company.RssReaderBot.commands.Command;

public class UpdateUserDataCommand implements Command<Long> {
    /**
     * 1. check if the data has to be updated (which data)
     * 2. if yes, update specific value. Get specific info about user (ex. username) and
     * update it in DB (update ... set ... where ...;)
     * 3. otherwise, return pop-up? notification or do not do anything
     */

    @Override
    public void execute(Long chatId, Long messageId) {
        
    }
}
