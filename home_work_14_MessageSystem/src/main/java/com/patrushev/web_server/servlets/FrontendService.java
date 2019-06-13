package com.patrushev.web_server.servlets;

import com.patrushev.web_server.data.UserDataSet;
import com.patrushev.web_server.messageSystem.Addressee;

public interface FrontendService extends Addressee {
    UserDataSet findUser(long id);

    int getUserCount();

    void insertUser(UserDataSet user);
}
