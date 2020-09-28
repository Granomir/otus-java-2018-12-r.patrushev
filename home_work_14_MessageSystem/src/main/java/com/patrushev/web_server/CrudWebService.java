package com.patrushev.web_server;

import com.patrushev.web_server.auth.AuthorizationFilter;
import com.patrushev.web_server.data.AddressDataSet;
import com.patrushev.web_server.data.PhoneDataSet;
import com.patrushev.web_server.data.UserDataSet;
import com.patrushev.web_server.data.UserDataSetDAO;
import com.patrushev.web_server.dbutils.DBService;
import com.patrushev.web_server.dbutils.DBServiceHibernateImpl;
import com.patrushev.web_server.messageSystem.Address;
import com.patrushev.web_server.messageSystem.MessageSystem;
import com.patrushev.web_server.messageSystem.MessageSystemContext;
import com.patrushev.web_server.servlets.CrudServlet;
import com.patrushev.web_server.servlets.FrontendService;
import com.patrushev.web_server.servlets.FrontendServiceImpl;
import com.patrushev.web_server.servlets.LoginServlet;
import com.patrushev.web_server.view.TemplateProcessor;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.hibernate.cfg.Configuration;

public class CrudWebService {
    private final String STATIC = "/static";
    private final ServletContextHandler context;
    private final Server server;
    private final MessageSystem messageSystem;

    public void start() throws Exception {
        Configuration configuration = new Configuration();
        configuration.configure();
        //настройка message system
        //создаем контекст
        MessageSystemContext msContext = new MessageSystemContext(messageSystem);
        //создаем адреса
        Address frontAddress = new Address("frontEnd");
        Address databaseAddress = new Address("dataBase");
        //передаем адреса в контекст
        msContext.setFrontAddress(frontAddress);
        msContext.setDbAddress(databaseAddress);
        FrontendService frontendService = new FrontendServiceImpl(msContext, frontAddress);
        try (DBService dbService = new DBServiceHibernateImpl(configuration, new UserDataSetDAO(), msContext, databaseAddress)) {
            createSomeUsers(dbService);
            //создается ресурсхэндлер jetty и ему передается путь к папке с ресурсами (html-страницы, картинки и т.п.)
            ResourceHandler resourceHandler = new ResourceHandler();
            Resource resource = Resource.newClassPathResource(STATIC);
            resourceHandler.setBaseResource(resource);
            //создается наш сервлет и передается сервлетхолдеру jetty, который передается в контекст jetty
            //еще передается путь, перейдя по которому (адрес:порт/) откроется страница, управлямая нашим сервлетом
            context.addServlet(new ServletHolder(new CrudServlet(dbService, new TemplateProcessor(), frontendService)), "/crud");
            context.addServlet(new ServletHolder(new LoginServlet(dbService, frontendService)), "/login");
            context.addFilter(new FilterHolder(new AuthorizationFilter()), "/crud", null);
            //и передается лист хэндлеров, в который передаются ресурсхендлер и контекст
            server.setHandler(new HandlerList(resourceHandler, context));
            //запускаем систему сообщений
            messageSystem.start();
            //стартуем веб-сервер
            server.start();
            //join тут нужен на случай, когда серверу надо некоторое время на запуск (например большое приложение) и поэтому вызвающему потоку лучше его подождать
            server.join();
        }
    }

    public CrudWebService(ServletContextHandler context, Server server, MessageSystem messageSystem) {
        //что-то от jetty, связанное с контекстом и сервлетами
        this.context = context;
        //сервер jetty, ему передается порт - видимо это сам веб-сервер
        this.server = server;
        this.messageSystem = messageSystem;
    }

    private void createSomeUsers(DBService dbService) {
        UserDataSet savingUser1 = new UserDataSet("Roman", "123", 29, new AddressDataSet("P"), new PhoneDataSet("937"));
        UserDataSet savingUser2 = new UserDataSet("Tatiana", "456", 28, new AddressDataSet("M"), new PhoneDataSet("964"));
        UserDataSet savingUser3 = new UserDataSet("Anna", "789", 4, new AddressDataSet("SD"), new PhoneDataSet("917"));
        UserDataSet savingUser4 = new UserDataSet("Aleksandra", "321", 3, new AddressDataSet("MD"), new PhoneDataSet("915"));
        dbService.save(savingUser1);
        dbService.save(savingUser2);
        dbService.save(savingUser3);
        dbService.save(savingUser4);
    }
}
