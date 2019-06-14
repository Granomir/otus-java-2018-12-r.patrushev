package com.patrushev.web_server.messageSystem;

import com.patrushev.web_server.messageSystem.messages.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("LoopStatementThatDoesntLoop")
public final class MessageSystem {
    private final static Logger logger = Logger.getLogger(MessageSystem.class.getName());

    //рабочие потоки???
    private final List<Thread> workers;
    //соотношения "адрес - перечень сообщений для него"??
    private final Map<Address, LinkedBlockingQueue<Message>> messagesMap;
    //соотношения "адрес - адресат"
    private final Map<Address, Addressee> addresseeMap;

    public MessageSystem() {
        //создаются коллекции для полей
        workers = new ArrayList<>();
        messagesMap = new HashMap<>();
        addresseeMap = new HashMap<>();
    }

    public void addAddressee(Addressee addressee) {
        addresseeMap.put(addressee.getAddress(), addressee);
        messagesMap.put(addressee.getAddress(), new LinkedBlockingQueue<>());
    }

    public void sendMessage(Message message) {
        logger.info("система сообщений получает сообщение типа " + message.getClass().getSimpleName());
        messagesMap.get(message.getTo()).add(message);
    }


    public void start() {
        //пробегаемся по коллекции адресатов
        for (Map.Entry<Address, Addressee> entry : addresseeMap.entrySet()) {
            //подготавливаем имя для потока
            String name = "MS-worker-" + entry.getKey().getId();
            Thread thread = new Thread(() -> {
                while (true) {
                    //вытаскиваем очередь, подготовленную для адресата
                    LinkedBlockingQueue<Message> queue = messagesMap.get(entry.getKey());
                    while (true) {
                        try {
                            //вытаскиваем сообщение из очереди (если есть, а если нет, то поток засыпает)
                            Message message = queue.take(); //Blocks
                            //говорим сообщению, чтобы оно отработало
                            message.exec(entry.getValue());
                        } catch (InterruptedException e) {
                            logger.log(Level.INFO, "Thread interrupted. Finishing: " + name);
                            return;
                        }
                    }
                }
            });
            thread.setName(name);
            thread.start();
            workers.add(thread);
        }
    }

    public void dispose() {
        workers.forEach(Thread::interrupt);
    }
}
