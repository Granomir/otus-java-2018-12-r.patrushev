package com.patrushev.web_server.servlets;

import com.patrushev.web_server.data.AddressDataSet;
import com.patrushev.web_server.data.PhoneDataSet;
import com.patrushev.web_server.data.UserDataSet;
import com.patrushev.web_server.dbutils.DBService;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CrudServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(CrudServlet.class);
    private final TemplateProcessor templateProcessor;
    private final DBService dbService;

    public CrudServlet(DBService dbService) {
        this.dbService = dbService;
        this.templateProcessor = new TemplateProcessor();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("отправлен метод GET");
        Map<String, Object> pageVariables = new HashMap<>();
        if (req.getParameter("action") != null) {
            //получение имени юзера по id
            if (req.getParameter("action").equals("findUser")) {
                final String id = req.getParameter("id");
                logger.info("Пользователь ищет user по id = " + id);
                final UserDataSet foundUser;
                try {
                    foundUser = dbService.load(Long.parseLong(id), UserDataSet.class);
                    pageVariables.put("foundUser", foundUser.getUser_name());
                } catch (ObjectNotFoundException e) {
                    pageVariables.put("foundUser", "такой пользователь отсутсвует");
                }
            }
            //получение количества юзеров в БД
            if (req.getParameter("action").equals("getCount")) {
                logger.info("Пользователь запрашивает количество user в БД");
                pageVariables.put("count", dbService.getAllUsers().size());
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
            //создание нового юзера в БД
            if (req.getParameter("action").equals("insertUser")) {
                logger.info("Пользователь добавляет user в БД");
                UserDataSet userDataSet = getUserDataSet(req);
                dbService.save(userDataSet);
                pageVariables.put("created", "Новый пользователь создан!");
            }
        }
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(templateProcessor.getPage("crud.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private UserDataSet getUserDataSet(HttpServletRequest req) {
        logger.info("получение данных из форм для создания пользователя");
        String name = null;
        if (!req.getParameter("name").equals("")) {
            name = req.getParameter("name");
        }
        String pass = null;
        if (!req.getParameter("pass").equals("")) {
            pass = req.getParameter("pass");
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
        return new UserDataSet(name, pass, age, addressDataSet, phonesSet);
    }
}
