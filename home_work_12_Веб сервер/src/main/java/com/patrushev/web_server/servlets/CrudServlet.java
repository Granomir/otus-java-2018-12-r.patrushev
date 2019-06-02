package com.patrushev.web_server.servlets;

import com.patrushev.web_server.data.AddressDataSet;
import com.patrushev.web_server.data.PhoneDataSet;
import com.patrushev.web_server.data.UserDataSet;
import com.patrushev.web_server.dbutils.DBService;
import com.patrushev.web_server.view.TemplateProcessor;
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

    public CrudServlet(DBService dbService, TemplateProcessor templateProcessor) {
        this.dbService = dbService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("отправлен метод GET");
        Map<String, Object> pageVariables = new HashMap<>();
        if (req.getParameter("action") != null) {
            //получение имени юзера по id
            if (req.getParameter("action").equals("findUser")) {
                doFindUser(req, pageVariables);
            }
            //получение количества юзеров в БД
            if (req.getParameter("action").equals("getCount")) {
                doGetCount(pageVariables);
            }
        }
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(templateProcessor.getPage("crud.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void doGetCount(Map<String, Object> pageVariables) {
        logger.info("Пользователь запрашивает количество user в БД");
        pageVariables.put("count", dbService.getAllUsers().size());
    }

    private void doFindUser(HttpServletRequest req, Map<String, Object> pageVariables) {
        final String id = req.getParameter("id");
        logger.info("Пользователь ищет user по id = " + id);
        final UserDataSet foundUser;
        try {
            foundUser = dbService.load(Long.parseLong(id), UserDataSet.class);
            pageVariables.put("foundUser", foundUser.getUser_name());
        } catch (NumberFormatException e) {
            pageVariables.put("foundUser", "необходимо ввести число");
        } catch (ObjectNotFoundException e) {
            pageVariables.put("foundUser", "такой пользователь отсутсвует");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("отправлен метод POST");
        Map<String, Object> pageVariables = new HashMap<>();
        if (req.getParameter("action") != null) {
            //создание нового юзера в БД
            if (req.getParameter("action").equals("insertUser")) {
                doInsertUser(req, pageVariables);
            }
        }
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(templateProcessor.getPage("crud.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void doInsertUser(HttpServletRequest req, Map<String, Object> pageVariables) {
        logger.info("Пользователь добавляет user в БД");
        UserDataSet userDataSet = getUserDataSet(req);
        dbService.save(userDataSet);
        pageVariables.put("created", "Новый пользователь создан!");
    }

    private UserDataSet getUserDataSet(HttpServletRequest req) {
        logger.info("получение данных из форм для создания пользователя");
        String name = getParameter(req, "name");
        String pass = getParameter(req, "pass");
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

    private String getParameter(HttpServletRequest req, String parameter) {
        if (!req.getParameter(parameter).equals("")) {
            return req.getParameter(parameter);
        } else {
            return null;
        }
    }
}
