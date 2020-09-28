package com.patrushev.web_server.servlets;

import com.patrushev.web_server.data.UserDataSet;
import com.patrushev.web_server.messageSystem.Address;
import com.patrushev.web_server.messageSystem.messages.Message;
import com.patrushev.web_server.messageSystem.MessageSystem;
import com.patrushev.web_server.messageSystem.MessageSystemContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class FrontendServiceImpl implements FrontendService {
    private final Logger logger = LoggerFactory.getLogger(CrudServlet.class);
    private Address address;
    private final MessageSystemContext msContext;
    private final Address frontAddress;
    private final Map<String, LinkedBlockingQueue<Message>> answerMessageAwaitingQueues;

    public FrontendServiceImpl(MessageSystemContext msContext, Address frontAddress) {
        this.msContext = msContext;
        this.frontAddress = frontAddress;
        msContext.getMessageSystem().addAddressee(this);
        answerMessageAwaitingQueues = new HashMap<>();
    }

    @Override
    public UserDataSet findUser(long id) {
        return null;
    }

    @Override
    public int getUserCount() {
//        Message message = new MsgGetUserCount(getAddress(), msContext.getAddress("dataBase"));
//        msContext.getMessageSystem().sendMessage(message);
        return 0;
    }

    @Override
    public void insertUser(UserDataSet user) {

    }

    @Override
    public Address getDbAddress() {
        return msContext.getDbAddress();
    }

    @Override
    public void sendMessage(Message message) {
        logger.info("Отправляется сообщение фронтом типа " + message.getClass().getSimpleName());
        answerMessageAwaitingQueues.put(message.getId(), new LinkedBlockingQueue<>());
        msContext.getMessageSystem().sendMessage(message);
    }

    @Override
    public Message getAnswer(String id) {
        try {
            return answerMessageAwaitingQueues.get(id).take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void putAnswer(String queryId, Message message) {
        logger.info("в очередь фронта возвращается ответное сообщение типа " + message.getClass().getSimpleName());
        answerMessageAwaitingQueues.get(queryId).add(message);
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
