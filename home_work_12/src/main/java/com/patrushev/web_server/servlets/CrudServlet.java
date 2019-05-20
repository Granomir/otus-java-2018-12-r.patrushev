package com.patrushev.web_server.servlets;

import com.patrushev.web_server.data.AddressDataSet;
import com.patrushev.web_server.data.AdminDataSet;
import com.patrushev.web_server.data.PhoneDataSet;
import com.patrushev.web_server.data.UserDataSet;
import com.patrushev.web_server.dbutils.DBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class CrudServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(CrudServlet.class);
    private final TemplateProcessor templateProcessor;
    private final DBService dbService;

    public CrudServlet(DBService dbService) throws IOException {
        this.dbService = dbService;
        this.templateProcessor = new TemplateProcessor();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("отправлен метод GET");
        Map<String, Object> pageVariables = new HashMap<>();
        if (req.getParameter("action") != null) {
            if (req.getParameter("action").equals("findUser")) {
                logger.info("Пользователь ищет user по id = " + req.getParameter("id"));
                //TODO получить user из БД и записать в шаблон
                pageVariables.put("foundUser", new UserDataSet("Roman", 0, new AddressDataSet()));
            }
            if (req.getParameter("action").equals("getCount")) {
                logger.info("Пользователь запрашивает количество user в БД");
                //TODO получить количество user из БД и записать в шаблон
            }
        }
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(templateProcessor.getPage("crud.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("отправлен метод POST");
        Map<String, Object> pageVariables = new HashMap<>();
        if (req.getParameter("action") != null) {
            if (req.getParameter("action").equals("login")) {
                logger.info("Пользователь начинает авторизацию");
                String login = req.getParameter("login");
                logger.info("Пользователь ввел логин: " + login);
                String pass = req.getParameter("pass");
                logger.info("Пользователь ввел пароль: " + pass);
                //TODO проверка наличия такого админа
                
                //TODO если нет такого админа - возврат ошибки???
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                //TODO если есть такой админ - создание сессии

                //TODO совершить авторизацию
            }
            if (req.getParameter("action").equals("insertUser")) {
                logger.info("Пользователь добавляет user в БД");
                UserDataSet userDataSet = getUserDataSet(req);
                dbService.save(userDataSet);
                //TODO получить данные user из req и записать в БД
            }
        }
        resp.setContentType("text/html;charset=utf-8");
//        resp.getWriter().println(templateProcessor.getPage("crud.html", pageVariables));
//        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private UserDataSet getUserDataSet(HttpServletRequest req) {
        String name = null;
        if (!req.getParameter("name").equals("")) {
            name = req.getParameter("name");
        }
        int age = 0;
        if (!req.getParameter("age").equals("")) {
            age = Integer.parseInt(req.getParameter("age"));
        }
        AddressDataSet addressDataSet = null;
        if (!req.getParameter("address").equals("")) {
            addressDataSet = new AddressDataSet(req.getParameter("address"));
        }
        PhoneDataSet[] phonesSet = null;
        if (!req.getParameter("phones").equals("")) {
            String[] phonesArray = req.getParameter("phones").split(",");
            phonesSet = new PhoneDataSet[phonesArray.length];
            for (int i = 0; i < phonesArray.length; i++) {
                phonesSet[i] = new PhoneDataSet(phonesArray[i]);
            }
        }
        return new UserDataSet(name, age, addressDataSet, phonesSet);
    }
}
