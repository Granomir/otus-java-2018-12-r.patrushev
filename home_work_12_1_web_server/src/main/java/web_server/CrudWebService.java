package web_server;

import dbservice.DBService;
import dbservice.impl.DBServiceUserImpl;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.security.Constraint;
import test_entities.Address;
import test_entities.Phone;
import test_entities.User;
import web_server.auth.AuthorizationFilter;
import web_server.filters.SimpleFilter;
import web_server.servlets.CrudServlet;
import web_server.servlets.LoginServlet;
import web_server.view.TemplateProcessor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

public class CrudWebService {
    private final static String STATIC_RESOURCE_PATH = "/static";
    private final ServletContextHandler context;
    private final Server server;

    public void start() throws Exception {
        DBService<User> dbService = new DBServiceUserImpl();
        createSomeUsers(dbService);
        //создается ресурсхэндлер jetty и ему передается путь к папке с ресурсами (html-страницы, картинки и т.п.)
//        Resource resource = Resource.newClassPathResource(STATIC_RESOURCE_PATH);
//        resourceHandler.setBaseResource(resource);
        //создается наш сервлет и передается сервлетхолдеру jetty, который передается в контекст jetty
        //еще передается путь, перейдя по которому (адрес:порт/timer) откроется страница, управлямая нашим сервлетом
        context.addServlet(new ServletHolder(new CrudServlet(dbService, new TemplateProcessor())), "/crud");
        context.addServlet(new ServletHolder(new LoginServlet(dbService)), "/login");
//        context.addFilter(new FilterHolder(new AuthorizationFilter()), "/crud", null); //НЕ НУЖЕН???
        context.addFilter(new FilterHolder(new SimpleFilter()), "/*", null); //просто выводит в консоль ури и имя юзера
        //и передается лист хэндлеров, в который передаются ресурсхендлер и контекст
//        server.setHandler(new HandlerList(resourceHandler, context));
        server.setHandler(new HandlerList(context));
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{createResourceHandler(), createSecurityHandler(context)});
        server.setHandler(handlers);
        server.start();
        //join тут нужен на случай, когда серверу надо некоторое время на запуск (например большое приложение) и поэтому вызывающему потоку лучше его подождать
        server.join();

    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false); //ЧТО ЭТО???
        resourceHandler.setWelcomeFiles(new String[]{"index.html"}); //ЧТО ЭТО???
        URL fileDir = CrudWebService.class.getClassLoader().getResource("static"); //ЭТО ВМЕСТО ПРЕДЫДУЩЕЙ СТРОКИ???
        if (fileDir == null) { //ЕСЛИ ДА, ТО ЧЕМ ЭТО ЛУЧШЕ???
            throw new RuntimeException("File Directory not found");
        }
        resourceHandler.setResourceBase(fileDir.getPath()); //ЕСЛИ ДА, ТО ЧЕМ ЭТО ЛУЧШЕ???
        return resourceHandler;
    }

    public CrudWebService(ServletContextHandler context, Server server) {
        //что-то от jetty, связанное с контекстом и сервлетами
        this.context = context;
        //сервер jetty, ему передается порт - видимо это сам веб-сервер
        this.server = server;
    }

    private void createSomeUsers(DBService<User> dbService) {
        User savingUser1 = new User("Roman", 30, new Address("P"), new Phone("937"));
        User savingUser2 = new User("Tatiana", 28, new Address("M"), new Phone("964"));
        User savingUser3 = new User("Anna", 4, new Address("SD"), new Phone("917"));
        User savingUser4 = new User("Aleksandra", 3, new Address("MD"), new Phone("915"));
        dbService.create(savingUser1);
        dbService.create(savingUser2);
        dbService.create(savingUser3);
        dbService.create(savingUser4);
    }

    private SecurityHandler createSecurityHandler(ServletContextHandler context) throws MalformedURLException {
        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"user", "admin"});

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/crud/*");
        mapping.setConstraint(constraint);

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        //как декодировать стороку с юзером:паролем https://www.base64decode.org/
        security.setAuthenticator(new BasicAuthenticator());

        URL propFile = null;
        File realmFile = new File("./realm.properties");
        if (realmFile.exists()) {
            propFile = realmFile.toURI().toURL();
        }
        if (propFile == null) {
            System.out.println("local realm config not found, looking into Resources");
            propFile = CrudWebService.class.getClassLoader().getResource("realm.properties");
        }

        if (propFile == null) {
            throw new RuntimeException("Realm property file not found");
        }

        security.setLoginService(new HashLoginService("MyRealm", propFile.getPath()));
        security.setHandler(new HandlerList(context));
        security.setConstraintMappings(Collections.singletonList(mapping));

        return security;
    }
}
