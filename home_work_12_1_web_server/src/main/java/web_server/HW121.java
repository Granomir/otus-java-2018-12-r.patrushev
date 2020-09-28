package web_server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class HW121 {
    private static final int PORT = 8081;

    public static void main(String[] args) throws Exception {
        new CrudWebService(new ServletContextHandler(ServletContextHandler.SESSIONS), new Server(PORT)).start();
    }

}
