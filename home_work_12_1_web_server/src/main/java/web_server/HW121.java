package web_server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class HW121 {
    private static final int PORT = 8081;

    public static void main(String[] args) throws Exception {
        final Server server = new CrudWebService(new ServletContextHandler(ServletContextHandler.SESSIONS), new Server(PORT)).prepareServer();
        server.start();
        //join тут нужен на случай, когда серверу надо некоторое время на запуск (например большое приложение) и поэтому вызывающему потоку лучше его подождать
        server.join();
    }

}
