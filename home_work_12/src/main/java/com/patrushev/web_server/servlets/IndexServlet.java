package com.patrushev.web_server.servlets;

import com.patrushev.web_server.data.AddressDataSet;
import com.patrushev.web_server.data.UserDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IndexServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(IndexServlet.class);
    private final TemplateProcessor templateProcessor;

    public IndexServlet() throws IOException {
        this.templateProcessor = new TemplateProcessor();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("отправлен метод GET");
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("exampleObject", new UserDataSet("Roman", 0, new AddressDataSet(), null, null));
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(templateProcessor.getPage("index.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.sendRedirect("google.com");
//        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();

        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(templateProcessor.getPage("index.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
//        super.doPost(req, resp);
    }
}
