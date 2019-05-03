package com.patrushev.web_server;

import com.patrushev.web_server.data.AdminDataSet;
import com.patrushev.web_server.data.DataSet;
import com.patrushev.web_server.data.DataSetDAO;
import com.patrushev.web_server.data.UserDataSet;
import com.patrushev.web_server.dbutils.DBService;
import com.patrushev.web_server.dbutils.DBServiceHibernateImpl;
import com.patrushev.web_server.servlets.CrudServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.hibernate.cfg.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Main {
    private final static int PORT = 8080;
    private final static String STATIC = "/static";

    public static void main(String[] args) throws Exception {
        new Main().start();
    }

    private void start() throws Exception {
        Configuration configuration = new Configuration();
        configuration.configure();
        try (DBService dbService = new DBServiceHibernateImpl(configuration, new DataSetDAO())) {
            createAdmin(dbService);

            //создается ресурсхэндлер jetty и ему передается путь к папке с ресурсами (html-страницы, картинки и т.п.)
            ResourceHandler resourceHandler = new ResourceHandler();
            Resource resource = Resource.newClassPathResource(STATIC);
            resourceHandler.setBaseResource(resource);

            //что-то от jetty, связанное с контекстом и сервлетами
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            //создается наш сервлет и передается сервлетхолдеру jetty, который передается в контекст jetty
            //еще передается путь, перейдя по которому (адрес:порт/timer) откроется страница, управлямая нашим сервлетом
            context.addServlet(new ServletHolder(new CrudServlet(dbService)), "/crud");

            //сервер jetty, ему передается порт - видимо это сам веб-сервер
            Server server = new Server(PORT);
            //и передается лист хэндлеров, в который передаются ресурсхендлер и контекст
            server.setHandler(new HandlerList(resourceHandler, context));


            server.start();
            //join тут нужен на случай, когда серверу надо некоторое время на запуск (например большое приложение) и поэтому вызвающему потоку лучше его подождать
            server.join();
        }
    }

    private void createAdmin(DBService dbService) {
        AdminDataSet adminDataSet = new AdminDataSet("admin", "admin");
        //сохраняю юзеров в БД
        dbService.save(adminDataSet);
    }
}
