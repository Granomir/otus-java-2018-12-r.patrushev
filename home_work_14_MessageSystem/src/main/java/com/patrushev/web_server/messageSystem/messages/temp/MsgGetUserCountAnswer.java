package com.patrushev.web_server.messageSystem.messages.temp;

import com.patrushev.web_server.messageSystem.Address;
import com.patrushev.web_server.messageSystem.messages.MsgToFrontend;
import com.patrushev.web_server.servlets.FrontendService;

public class MsgGetUserCountAnswer extends MsgToFrontend {
    private final int count;

    public MsgGetUserCountAnswer(Address to, Address from, int count) {
        super(from, to);
        this.count = count;
    }

    @Override
    public void exec(FrontendService frontendService) {
//        frontendService.addMessage(this);
    }
}
