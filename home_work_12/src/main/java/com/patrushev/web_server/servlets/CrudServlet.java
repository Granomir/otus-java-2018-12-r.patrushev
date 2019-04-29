package com.patrushev.web_server.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CrudServlet extends HttpServlet {
    private final TemplateProcessor templateProcessor;

    public CrudServlet() throws IOException {
        this.templateProcessor = new TemplateProcessor();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("должен попасть на страницу crud");
        resp.getWriter().println();
//        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
