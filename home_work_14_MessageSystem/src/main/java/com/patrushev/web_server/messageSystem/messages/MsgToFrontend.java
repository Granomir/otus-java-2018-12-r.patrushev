package com.patrushev.web_server.messageSystem.messages;

import com.patrushev.web_server.messageSystem.Address;
import com.patrushev.web_server.messageSystem.Addressee;
import com.patrushev.web_server.servlets.FrontendService;

/**
 * Created by tully.
 */
public abstract class MsgToFrontend extends Message {
    public MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof FrontendService) {
            exec((FrontendService) addressee);
        } else {
            //todo error!
        }
    }

    public abstract void exec(FrontendService frontendService);
}