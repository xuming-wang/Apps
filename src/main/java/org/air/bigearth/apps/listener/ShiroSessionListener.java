package org.air.bigearth.apps.listener;


import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义session监听器
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-03-29
 */
public class ShiroSessionListener implements SessionListener {
    public static final Logger logger= LoggerFactory.getLogger(ShiroSessionListener.class);

    /**
     * 统计在线人数
     * juc包下线程安全自增
     */
    private final AtomicInteger sessionCount = new AtomicInteger(0);

    /**
     * 会话创建时触发
     * @param session
     */
    @Override
    public void onStart(Session session) {
        StringBuilder sessionInfo = new StringBuilder();
        sessionInfo.append(session.getId()).append(",")
                .append(session.getStartTimestamp()).append(",")
                .append(session.getLastAccessTime()).append(",")
                .append(session.getTimeout()).append(",");
        logger.info(sessionInfo.toString());
        //会话创建，在线人数加一
        sessionCount.incrementAndGet();
        logger.info("onStart:" + sessionCount);
    }

    /**
     * 退出会话时触发
     * @param session
     */
    @Override
    public void onStop(Session session) {
        //会话退出,在线人数减一
        sessionCount.decrementAndGet();
        logger.info("onStop:" + sessionCount);
    }

    /**
     * 会话过期时触发
     * @param session
     */
    @Override
    public void onExpiration(Session session) {
        //会话过期,在线人数减一
        sessionCount.decrementAndGet();
        logger.info("onExpiration:" + sessionCount);
    }
    /**
     * 获取在线人数使用
     * @return
     */
    public AtomicInteger getSessionCount() {
        logger.info("getSessionCount:" + sessionCount);
        return sessionCount;
    }
}
