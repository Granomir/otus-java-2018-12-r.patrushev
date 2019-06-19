package com.patrushev.web_server.messageSystem.messages;

import com.patrushev.web_server.messageSystem.Address;
import com.patrushev.web_server.servlets.FrontendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgAddUserAnswer extends MsgToFrontend {
    private final Logger logger = LoggerFactory.getLogger(MsgAddUserAnswer.class);
    private String queryId;

    public MsgAddUserAnswer(Address from, Address to, String id) {
        super(from, to);
    }

    @Override
    public void exec(FrontendService frontendService) {

    }
}
