package com.patrushev.web_server.messageSystem.messages;

import com.patrushev.web_server.dbutils.DBService;
import com.patrushev.web_server.messageSystem.Address;
import com.patrushev.web_server.messageSystem.Addressee;

/**
 * Created by tully.
 */
public abstract class MsgToDB extends Message {
    public MsgToDB(Address from, Address to) {
        super(from, to);
    }

    //здесь проверяется что переданный адресат действительно является тем, кем нужно (сервисом БД)
    //и дальше передается в следующий метод
    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DBService) {
            exec((DBService) addressee);
        }
    }

    public abstract void exec(DBService dbService);
}
