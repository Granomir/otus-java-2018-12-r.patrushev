package com.patrushev.web_server.messageSystem.messages;

import com.patrushev.web_server.messageSystem.Address;
import com.patrushev.web_server.messageSystem.Addressee;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Message {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
    private final String id;
    private final Address from;
    private final Address to;

    public Message(Address from, Address to) {
        this.from = from;
        this.to = to;
        id = String.valueOf(ID_GENERATOR.getAndIncrement());
    }

    public Address getFrom() {
        return from;
    }

    public Address getTo() {
        return to;
    }

    public abstract void exec(Addressee addressee);

    public String getId() {
        return id;
    }
}
