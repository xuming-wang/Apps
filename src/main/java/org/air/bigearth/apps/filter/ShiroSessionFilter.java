package org.air.bigearth.apps.filter;

import org.air.bigearth.apps.system.domain.basic.User;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通过过滤器设置shiroSession过期时间
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-03-29
 */
public class ShiroSessionFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(ShiroSessionFilter.class);

    public List<String> excludes = new ArrayList<String>();
    //ms 180000L;
    private long serverSessionTimeout = 60L;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if(logger.isDebugEnabled()){
            logger.debug("shiro session filter init~~~~~~~~~~~~");
        }
        String temp = filterConfig.getInitParameter("excludes");
        if (temp != null) {
            String[] url = temp.split(",");
            for (int i = 0; url != null && i < url.length; i++) {
                excludes.add(url[i]);
            }
        }
        String timeout = filterConfig.getInitParameter("serverSessionTimeout");
        if(StringUtils.isNotBlank(timeout)){
            //this.serverSessionTimeout = NumberUtils.toLong(timeout,1800L)*1000L;
            System.out.println(serverSessionTimeout);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if(logger.isDebugEnabled()){
            logger.debug("shiro session filter is open");
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if(handleExcludeURL(req, resp)){
            filterChain.doFilter(request, response);
            return;
        }

        Subject currentUser = SecurityUtils.getSubject();

        User user = (User)currentUser.getSession().getAttribute("loginUser");
        if(currentUser.isAuthenticated()){
            if (user != null) {
                //filterChain.doFilter(req, resp);
            } else {
                // 跳转登录
                String url = "/login";
                resp.sendRedirect(url);
            }
            //currentUser.getSession().setTimeout(serverSessionTimeout);
        }
        filterChain.doFilter(req, resp);
    }

    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {

        if (excludes == null || excludes.isEmpty()) {
            return false;
        }

        String url = request.getServletPath();
        for (String pattern : excludes) {
            Pattern p = Pattern.compile("^" + pattern);
            Matcher m = p.matcher(url);
            if (m.find()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void destroy() {

    }
}
