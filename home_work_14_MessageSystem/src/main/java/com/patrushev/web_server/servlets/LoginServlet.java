package com.patrushev.web_server.servlets;

import com.patrushev.web_server.data.UserDataSet;
import com.patrushev.web_server.dbutils.DBService;
import com.patrushev.web_server.messageSystem.Address;
import com.patrushev.web_server.messageSystem.Addressee;
import com.patrushev.web_server.messageSystem.MessageSystem;
import com.patrushev.web_server.messageSystem.MessageSystemContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet implements Addressee {
    private final Logger logger = LoggerFactory.getLogger(CrudServlet.class);
    private final DBService dbService;
    private MessageSystemContext msContext;
    private Address address;

    public LoginServlet(DBService dbService, MessageSystemContext msContext, Address address) {
        this.dbService = dbService;
        this.msContext = msContext;
        this.address = address;
        msContext.getMessageSystem().addAddressee(this);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("Пользователь начинает аутентификацию");
        String login = req.getParameter("login");
        logger.info("Пользователь ввел логин: " + login);
        String pass = req.getParameter("pass");
        logger.info("Пользователь ввел пароль: " + pass);
        //проверка наличия такого юзера
        if (authenticateUser(login, pass)) {
            logger.info("Пользователь ввел правильный пароль, создается сессия");
            HttpSession session = req.getSession();
            session.setMaxInactiveInterval(300);
            logger.info("Создана сессия");
            resp.sendRedirect("/crud");
        } else {
            logger.info("Пользователь ввел неправильные данные");
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private boolean authenticateUser(String login, String pass) {
        logger.info("Начинается аутентификация введенных пользователем данных");
        UserDataSet checkingUser = dbService.loadUserByName(login);
        if (checkingUser != null) {
            return pass.equals(checkingUser.getPass());
        } else {
            logger.info("Пользователя с таким именем не существует");
            return false;
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return msContext.getMessageSystem();
    }
}
