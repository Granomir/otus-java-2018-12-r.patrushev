package com.patrushev.web_server.auth;

import com.patrushev.web_server.servlets.CrudServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthorizationFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);
    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) {
        this.context = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String uri = req.getRequestURI();
        this.context.log("Requested Resource:" + uri);

        HttpSession session = req.getSession(false);
        logger.info("Проверка наличия активной сессии");
        if (session == null) {
            logger.info("Активная сессия отсутствует");
            res.setStatus(403);
        } else {
            logger.info("Активная сессия существует");
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
