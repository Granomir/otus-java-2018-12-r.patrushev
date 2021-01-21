package web_server;

import dbservice.DBService;
import dbservice.impl.DBServiceUserImpl;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import test_entities.Address;
import test_entities.Phone;
import test_entities.User;
import web_server.auth.AuthorizationFilter;
import web_server.filters.SimpleFilter;
import web_server.servlets.CrudServlet;
import web_server.servlets.LoginServlet;
import web_server.view.TemplateProcessor;

public class CrudWebService {
    private final static String STATIC_RESOURCE_PATH = "/static";
    public final static String CRUD_PATH = "/crud";
    private final static String LOGIN_PATH = "/login";
    private final static String ALL_PATH = "/*";

    private final ServletContextHandler context;
    private final Server server;

    public Server prepareServer() {
        DBService<User> dbService = new DBServiceUserImpl();
        createSomeUsers(dbService);
        //создается наш сервлет и передается сервлетхолдеру jetty, который передается в контекст jetty
        //еще передается путь, перейдя по которому (адрес:порт/timer) откроется страница, управлямая нашим сервлетом
        context.addServlet(new ServletHolder(new CrudServlet(dbService, new TemplateProcessor())), CRUD_PATH);
        context.addServlet(new ServletHolder(new LoginServlet(dbService)), LOGIN_PATH);
        context.addFilter(new FilterHolder(new SimpleFilter()), ALL_PATH, null); //просто выводит в консоль ури и имя юзера
        context.addFilter(new FilterHolder(new AuthorizationFilter()), CRUD_PATH, null); //фильтр авторизации, не пускает по указанному пути, если нет сессии и перенаправляет на строницу авторизации
        //и передается лист хэндлеров, в который передаются ресурсхендлер и контекст
        server.setHandler(new HandlerList(createResourceHandler(), context));
        return server;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        Resource resource = Resource.newClassPathResource(STATIC_RESOURCE_PATH);
        resourceHandler.setBaseResource(resource);
        return resourceHandler;
    }

    public CrudWebService(ServletContextHandler context, Server server) {
        //что-то от jetty, связанное с контекстом и сервлетами
        this.context = context;
        //сервер jetty, ему передается порт - видимо это сам веб-сервер
        this.server = server;
    }

    private void createSomeUsers(DBService<User> dbService) {
        User savingUser1 = new User("Roman", 30, new Address("P"), "verda", new Phone("937"));
        User savingUser2 = new User("Tatiana", 28, new Address("M"), "verda", new Phone("964"));
        User savingUser3 = new User("Anna", 4, new Address("SD"), "verda", new Phone("917"));
        User savingUser4 = new User("Aleksandra", 3, new Address("MD"), "verda", new Phone("915"));
        dbService.create(savingUser1);
        dbService.create(savingUser2);
        dbService.create(savingUser3);
        dbService.create(savingUser4);
    }

}
