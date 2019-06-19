package com.patrushev.web_server.messageSystem.messages;

import com.patrushev.web_server.data.UserDataSet;
import com.patrushev.web_server.dbutils.DBService;
import com.patrushev.web_server.messageSystem.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgAddUser extends MsgToDB {
    private final Logger logger = LoggerFactory.getLogger(MsgAddUser.class);
    private UserDataSet userDataSet;

    public MsgAddUser(Address from, Address to, UserDataSet userDataSet) {
        super(from, to);
        this.userDataSet = userDataSet;
        logger.info("Создается сообщение типа MsgAddUser");
    }

    @Override
    public void exec(DBService dbService) {
        logger.info("начинается отработка сообщения типа MsgAddUser");
        logger.info("в БД добавляется новый пользователь с именем " + userDataSet.getUser_name());
//        UserDataSet checkingUser = dbService.loadUserByName(login);
        dbService.save(userDataSet);
        dbService.getMS().sendMessage(new MsgAddUserAnswer(getTo(), getFrom(), getId()));
    }
}
