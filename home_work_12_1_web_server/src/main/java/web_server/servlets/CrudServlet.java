package web_server.servlets;

import dbservice.UserDao;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test_entities.Address;
import test_entities.Phone;
import test_entities.User;
import web_server.view.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CrudServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html;charset=utf-8";

    private final Logger logger = LoggerFactory.getLogger(CrudServlet.class);

    private final TemplateProcessor templateProcessor;
    private final UserDao userDao;

    public CrudServlet(UserDao userDao, TemplateProcessor templateProcessor) {
        this.userDao = userDao;
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
            //TODO получение всех юзеров - не охота, т.к. в основном просто надо заморочиться с фронтом и БДсервисом
            if (req.getParameter("action").equals("getAllUsers")) {
                doGetAllUsers(pageVariables);
            }
        }
        resp.setContentType(CONTENT_TYPE);
        resp.getWriter().println(templateProcessor.getPage("crud.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void doGetAllUsers(Map<String, Object> pageVariables) {
        //TODO надо реализовать, но не охота, т.к. в основном просто надо заморочиться с фронтом и БДсервисом
    }

    private void doGetCount(Map<String, Object> pageVariables) {
        logger.info("Пользователь запрашивает количество user в БД");
        pageVariables.put("count", userDao.getAll().size());
    }

    private void doFindUser(HttpServletRequest req, Map<String, Object> pageVariables) {
        final String id = req.getParameter("id");
        logger.info("Пользователь ищет user по id = " + id);
        User foundUser = null;
        try {
            foundUser = userDao.load(Long.parseLong(id));
        } catch (NumberFormatException e) {
            pageVariables.put("foundUser", "необходимо ввести число");
        } catch (ObjectNotFoundException e) {
            pageVariables.put("foundUser", "такой пользователь отсутсвует");
        }
        if (foundUser == null) {
            pageVariables.put("foundUser", "такой пользователь отсутсвует");
        } else {
            pageVariables.put("foundUser", foundUser.getName());
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
        User user = getUser(req);
        userDao.create(user);
        pageVariables.put("created", "Новый пользователь создан!");
    }

    private User getUser(HttpServletRequest request) {
        logger.info("получение данных из форм для создания пользователя");
        String name = getParameter(request, "name");
        String pass = getParameter(request, "pass");
        int age = 0;
        if (!request.getParameter("age").equals("")) {
            age = Integer.parseInt(request.getParameter("age"));
        }
        Address address = null;
        if (!request.getParameter("address").equals("")) {
            address = new Address(request.getParameter("address"));
        }
        Phone[] phonesSet = null;
        if (!request.getParameter("phones").equals("")) {
            String[] phonesArray = request.getParameter("phones").split(",");
            phonesSet = new Phone[phonesArray.length];
            for (int i = 0; i < phonesArray.length; i++) {
                phonesSet[i] = new Phone(phonesArray[i]);
            }
        }
        //TODO по идее надо делать валидацию заполнения полей юзером, но не охота, т.к. это работа фронта
        // либо надо разобраться чтобы эти поля не были NonNull, но это тогда надо разбираться с БДсервисом
        return new User(name, age, address, pass, phonesSet);
    }

    private String getParameter(HttpServletRequest req, String parameter) {
        if (!req.getParameter(parameter).equals("")) {
            return req.getParameter(parameter);
        } else {
            return null;
        }
    }
}
