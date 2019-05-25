package org.air.bigearth.apps.filter;

import org.air.bigearth.apps.system.domain.basic.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登陆过滤器
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public class SessionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User)session.getAttribute("loginUser");

        String strURL = request.getRequestURL().toString();
        if (strURL.indexOf("/login") == -1 && strURL.indexOf("/login.action")== -1) {
            if (user == null) {
                String errors = "您还没有登录，或者session已过期。请先登陆!";
                request.setAttribute("Message", errors);
                //跳转至登录页面
                request.getRequestDispatcher("/login").forward(request, response);
            }else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
