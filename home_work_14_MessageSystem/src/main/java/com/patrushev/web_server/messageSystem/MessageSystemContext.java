package com.patrushev.web_server.messageSystem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tully.
 */
public class MessageSystemContext {
    private final MessageSystem messageSystem;

    private Map<String, Address> addresses;

//    private Address frontAddress;
//    private Address dbAddress;

    public MessageSystemContext(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        addresses = new HashMap<>();
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

//    public Address getFrontAddress() {
//        return frontAddress;
//    }

    public void addAddress(String addressName, Address address) {
        addresses.put(addressName, address);
    }

    public Address getAddress(String addressName) {
        return addresses.get(addressName);
    }
//
//    public void setDbAddress(Address dbAddress) {
//        this.dbAddress = dbAddress;
//    }
}
