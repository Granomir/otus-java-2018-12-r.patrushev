package web_server.servlets;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.jupiter.api.*;
import web_server.CrudWebService;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CrudServletTest {
    private static Server server;
    private final static int PORT = 8081;

    @BeforeEach
    void setUp() throws Exception {
        server = new CrudWebService(new ServletContextHandler(ServletContextHandler.SESSIONS), new Server(PORT)).prepareServer();
        server.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        server.stop();
    }

    @Test
    @DisplayName("test GET /crud")
    public void doGet() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) makeUrl("/crud").openConnection();
        connection.setRequestMethod("GET");
        assertEquals(HttpStatus.OK_200, connection.getResponseCode(), "doGet works");
    }

    @Test
    public void doPost() {
        //TODO короче просто надо потратить на тесты время!!!
        // 1) для сервлетов замокать их поля и дергать методы и проверять всё, что они кладут в респонз
        // 2) фильтр авторизации протестировать простым юнит-тестом
        // 3) проверить, что редирект, если нет сессии
    }

    private URL makeUrl(String part) throws MalformedURLException {
        return new URL("http://localhost:" + PORT + part);
    }
}