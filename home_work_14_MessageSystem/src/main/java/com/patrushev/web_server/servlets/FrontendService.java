package com.patrushev.web_server.servlets;

import com.patrushev.web_server.data.UserDataSet;
import com.patrushev.web_server.messageSystem.Address;
import com.patrushev.web_server.messageSystem.Addressee;
import com.patrushev.web_server.messageSystem.messages.Message;

public interface FrontendService extends Addressee {
    UserDataSet findUser(long id);

    int getUserCount();

    void insertUser(UserDataSet user);

    Address getDbAddress();

    void sendMessage(Message getUserByLoginMessage);

    Message getAnswer(String id);

    void putAnswer(String queryId, Message message);
}
