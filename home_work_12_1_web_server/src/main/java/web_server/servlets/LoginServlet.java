package web_server.servlets;

import dbservice.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test_entities.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static web_server.CrudWebService.CRUD_PATH;

public class LoginServlet extends HttpServlet {
    private final static int MAX_INACTIVE_INTERVAL = 300;

    private final Logger logger = LoggerFactory.getLogger(CrudServlet.class);

    private final UserDao userDao;

    public LoginServlet(UserDao userDao) {
        this.userDao = userDao;
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
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            session.setAttribute("currentUser", login);
            logger.info("Создана сессия");
            resp.sendRedirect(CRUD_PATH);
        } else {
            logger.info("Пользователь ввел неправильные данные");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean authenticateUser(String login, String pass) {
        logger.info("Начинается аутентификация введенных пользователем данных");
        User checkingUser = userDao.loadByName(login);
        if (checkingUser != null) {
            return pass.equals(checkingUser.getPass());
        } else {
            logger.info("Пользователя с таким именем не существует");
            return false;
        }
    }
}
