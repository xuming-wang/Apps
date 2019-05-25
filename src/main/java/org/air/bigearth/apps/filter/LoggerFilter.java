//package org.air.bigearth.apps.filter;
//
//import org.air.bigearth.apps.system.domain.basic.User;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.session.Session;
//import org.apache.shiro.subject.Subject;
//import org.springframework.stereotype.Component;
//import org.thymeleaf.templateresolver.FileTemplateResolver;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//
//@Component
//@WebFilter(urlPatterns = "/system/*",filterName = "loggerFilter")
//public class LoggerFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        System.out.println("==========init========");
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        System.out.println("==========doFilter========");
//        HttpServletRequest request = (HttpServletRequest)servletRequest;
//        HttpServletResponse response = (HttpServletResponse)servletResponse;
//        String reqUrl = request.getRequestURI();
//        filterChain.doFilter(request, response);
//        System.out.println(reqUrl);
//        HttpSession session = request.getSession(false);
//        /*if (session != null && session.getAttribute("loginUser") != null) {
//            filterChain.doFilter(request, response);
//        } else {
//            response.sendRedirect("/login");
//        }*/
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}
