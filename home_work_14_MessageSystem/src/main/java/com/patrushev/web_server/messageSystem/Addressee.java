package com.patrushev.web_server.messageSystem;

public interface Addressee {
    //получить адрес адресата
    Address getAddress();
    //получить используемую систему сообщений???
    MessageSystem getMS();
}
