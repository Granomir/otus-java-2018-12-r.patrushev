package com.patrushev.web_server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class Main {

    public static void main(String[] args) throws Exception {
        new CrudWebService(new ServletContextHandler(ServletContextHandler.SESSIONS), new Server(8080)).start();
    }
}
