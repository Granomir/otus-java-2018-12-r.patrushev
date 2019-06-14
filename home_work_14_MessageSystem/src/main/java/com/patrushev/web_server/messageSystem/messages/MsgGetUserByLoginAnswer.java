package com.patrushev.web_server.messageSystem.messages;

import com.patrushev.web_server.data.UserDataSet;
import com.patrushev.web_server.messageSystem.Address;
import com.patrushev.web_server.servlets.FrontendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgGetUserByLoginAnswer extends MsgToFrontend {
    private final Logger logger = LoggerFactory.getLogger(MsgGetUserByLoginAnswer.class);
    private UserDataSet checkingUser;
    private String queryId;

    public MsgGetUserByLoginAnswer(Address from, Address to, UserDataSet checkingUser, String id) {
        super(from, to);
        queryId = id;
        logger.info("создается сообщение типа MsgGetUserByLoginAnswer");
        this.checkingUser = checkingUser;
    }

    @Override
    public void exec(FrontendService frontendService) {
        logger.info("начинается отработка сообщения типа MsgGetUserByLoginAnswer");
        frontendService.putAnswer(queryId, this);
    }

    public UserDataSet getCheckingUser() {
        return checkingUser;
    }
}
