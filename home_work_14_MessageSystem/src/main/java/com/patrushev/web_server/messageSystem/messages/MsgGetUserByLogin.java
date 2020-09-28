package com.patrushev.web_server.messageSystem.messages;

import com.patrushev.web_server.data.UserDataSet;
import com.patrushev.web_server.dbutils.DBService;
import com.patrushev.web_server.messageSystem.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgGetUserByLogin extends MsgToDB {
    private final Logger logger = LoggerFactory.getLogger(MsgGetUserByLogin.class);
    private final String login;

    public MsgGetUserByLogin(Address from, Address to, String login) {
        super(from, to);
        logger.info("Создается сообщение типа MsgGetUserByLogin");
        this.login = login;
    }

    @Override
    public void exec(DBService dbService) {
        logger.info("начинается отработка сообщения типа MsgGetUserByLogin");
        logger.info("из БД выгружается пользователь с логином " + login);
        UserDataSet checkingUser = dbService.loadUserByName(login);
        dbService.getMS().sendMessage(new MsgGetUserByLoginAnswer(getTo(), getFrom(), checkingUser, getId()));
    }
}
