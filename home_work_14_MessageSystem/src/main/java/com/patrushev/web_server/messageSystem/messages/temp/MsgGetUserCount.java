package com.patrushev.web_server.messageSystem.messages.temp;

import com.patrushev.web_server.dbutils.DBService;
import com.patrushev.web_server.messageSystem.Address;
import com.patrushev.web_server.messageSystem.messages.MsgToDB;

public class MsgGetUserCount extends MsgToDB {
    public MsgGetUserCount(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(DBService dbService) {
        int count = dbService.getAllUsers().size();
        dbService.getMS().sendMessage(new MsgGetUserCountAnswer(getTo(), getFrom(), count));
    }
}
