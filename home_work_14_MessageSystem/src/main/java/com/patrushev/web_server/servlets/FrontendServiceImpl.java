package com.patrushev.web_server.servlets;

import com.patrushev.web_server.data.UserDataSet;
import com.patrushev.web_server.messageSystem.Address;
import com.patrushev.web_server.messageSystem.messages.Message;
import com.patrushev.web_server.messageSystem.MessageSystem;
import com.patrushev.web_server.messageSystem.MessageSystemContext;
import com.patrushev.web_server.messageSystem.messages.MsgGetUserCount;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class FrontendServiceImpl implements FrontendService {
    private Address address;
    private MessageSystemContext msContext;
    private Address frontAddress;
    private final Map<String, LinkedBlockingQueue<Message>> messagesMap;

    public FrontendServiceImpl(MessageSystemContext msContext, Address frontAddress) {
        this.msContext = msContext;
        this.frontAddress = frontAddress;
        msContext.getMessageSystem().addAddressee(this);
    }

    @Override
    public UserDataSet findUser(long id) {
        return null;
    }

    @Override
    public int getUserCount() {
        Message message = new MsgGetUserCount(getAddress(), msContext.getAddress("dataBase"));
        msContext.getMessageSystem().sendMessage(message);
        return 0;
    }

    @Override
    public void insertUser(UserDataSet user) {

    }

    @Override
    public Address getAddress() {
        return frontAddress;
    }

    @Override
    public MessageSystem getMS() {
        return msContext.getMessageSystem();
    }
}
