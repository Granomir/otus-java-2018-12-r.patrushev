package com.patrushev.web_server;

import com.patrushev.web_server.messageSystem.MessageSystem;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class Main {

    public static void main(String[] args) throws Exception {
        final ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        final Server server = new Server(8080);
        final MessageSystem messageSystem = new MessageSystem();
        new CrudWebService(servletContextHandler, server, messageSystem).start();
    }
}
