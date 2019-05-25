package org.air.bigearth.apps.config;

import java.util.LinkedHashMap;
import java.util.Map;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.air.bigearth.apps.filter.KickoutSessionControlFilter;
import org.air.bigearth.apps.listener.ShiroSessionListener;
import org.air.bigearth.apps.realm.UserRealm;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Shiro 配置
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
@Configuration
public class ShiroConfig {
	/**
	 * Session超时时间，单位为毫秒（默认30分钟）
	 */
	@Value("${shiro.session.expireTime}")
	private int expireTime;

	/**
	 * 相隔多久检查一次session的有效性，单位毫秒，默认就是10分钟
	 */
	@Value("${shiro.session.validationInterval}")
	private int validationInterval;

	/**
	 * 设置Cookie的域名
	 */
	@Value("${shiro.cookie.domain}")
	private String domain;

	/**
	 * 设置cookie的有效访问路径
	 */
	@Value("${shiro.cookie.path}")
	private String path;

	/**
	 * 设置HttpOnly属性
	 */
	@Value("${shiro.cookie.httpOnly}")
	private boolean httpOnly;

	/**
	 * 设置Cookie的过期时间，秒为单位
	 */
	@Value("${shiro.cookie.maxAge}")
	private int maxAge;

	/**
	 * 登录地址
	 */
	@Value("${shiro.user.loginUrl}")
	private String loginUrl;

	/**
	 * 权限认证失败地址
	 */
	@Value("${shiro.user.unauthorizedUrl}")
	private String unauthorizedUrl;

	/**
	 * ShiroFilterFactoryBean 处理拦截资源文件问题。
	 * 注意：初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
	 * Web应用中,Shiro可控制的Web请求必须经过Shiro主过滤器的拦截
	 * Filter Chain定义说明
	 * 	1、一个URL可以配置多个Filter，使用逗号分隔
	 * 	2、当设置多个过滤器时，全部验证通过，才视为通过
	 *  3、部分过滤器可指定参数，如perms，roles
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		//访问的是后端url地址为 /login的接口
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/index");
		// 未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");

		// 自定义拦截器限制并发人数
		//Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
		//filtersMap.put("kickout", kickoutSessionControlFilter());
		//shiroFilterFactoryBean.setFilters(filtersMap);

		// 拦截器.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置不会被拦截的链接,不登录可以访问的资源,顺序判断; anon 表示资源都可以匿名访问
		/// filterChainDefinitionMap.put("/static/**", "anon");
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/webuploader/**", "anon");
		filterChainDefinitionMap.put("/fonts/**", "anon");
		filterChainDefinitionMap.put("/image/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/ajaxLogin", "anon");
		filterChainDefinitionMap.put("/system/userlogin", "anon");
		filterChainDefinitionMap.put("/retrieve/mtl", "anon");
		filterChainDefinitionMap.put("/retrieve/query", "anon");
		filterChainDefinitionMap.put("/retrieve/query_dataset", "anon");
		filterChainDefinitionMap.put("/retrieve/datasets", "anon");
		//filterChainDefinitionMap.put("/static/**", "anon");
		//filterChainDefinitionMap.put("/swagger-ui.html#", "anon");
		// swagger接口权限 开放
		filterChainDefinitionMap.put("/swagger-ui.html", "anon");
		filterChainDefinitionMap.put("/webjars/**", "anon");
		filterChainDefinitionMap.put("/v2/**", "anon");
		filterChainDefinitionMap.put("/swagger-resources/**", "anon");

		//filterChainDefinitionMap.put("/hello", "anon");
		//filterChainDefinitionMap.put("/system/community", "perms[public_community]");

		// 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了,加上这个会导致302，请求重置，暂不明白原因
		 //filterChainDefinitionMap.put("/logout", "logout");
		//配置某个url需要某个权限码
		//filterChainDefinitionMap.put("/hello", "perms[how_are_you]");
		
		// 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 
		// <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问;user:remember me的可以访问-->
		filterChainDefinitionMap.put("/fine", "user");
		//filterChainDefinitionMap.put("/**", "authc");

		//其他资源都需要认证  authc 表示需要认证才能进行访问 user表示配置记住我或认证通过可以访问的地址
		filterChainDefinitionMap.put("/**", "user,authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		System.out.println("Shiro拦截器工厂类注入成功");
		return shiroFilterFactoryBean;
	}
	
	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(userRealm());
		
		//将缓存注入安全管理器，就不会反复执行  realm的授权方法了；只要实现了shiro的cache接口、CacheManager接口就可以用来注入安全管理器
		//shiro自带的一个内存缓存，本质是hashmap，MemoryConstrainedCacheManager()，试验没问题，非常轻，简单的登录用这个
		//securityManager.setCacheManager(new MemoryConstrainedCacheManager());
		securityManager.setCacheManager(ehCacheManager());
		
		//用了redis缓存注入安全管理器，会报一个序列化失败的错误，推测是new SimpleAuthenticationInfo 时 user对象无法序列化，加上序列化就好了
		//securityManager.setCacheManager(cacheManager());
		
		//将session托管给redis，nigix试验分布式，确实 做到了session共享
		securityManager.setSessionManager(sessionManager());
		
		  //注入记住我管理器;  
	    //securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}

	/**
	 * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
	 * @return
	 */
	@Bean
	public UserRealm userRealm() {
		UserRealm userRealm = new UserRealm();
		userRealm.setCacheManager(ehCacheManager());
		return userRealm;
	}
	
	/**
	 * 配置shiro redisManager
	 * 网上的一个 shiro-redis 插件，实现了shiro的cache接口、CacheManager接口就
	 * @return
	 */
	/*@Bean
	public RedisManager redisManager() {
		RedisManager redisManager = new RedisManager();
		redisManager.setHost("localhost");
		redisManager.setPort(6379);
		redisManager.setExpire(1800);// 配置过期时间
		// redisManager.setTimeout(timeout);
		// redisManager.setPassword(password);
		return redisManager;
	}*/
	/**
	 * cacheManager 缓存 redis实现
	 * 网上的一个 shiro-redis 插件
	 * @return
	 */
	/*@Bean
	public RedisCacheManager cacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		redisCacheManager.setRedisManager(redisManager());
		return redisCacheManager;
	}*/
	

	/**
	 * RedisSessionDAO shiro sessionDao层的实现 通过redis
	 */
	/*public RedisSessionDAO redisSessionDAO() {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		redisSessionDAO.setRedisManager(redisManager());
		return redisSessionDAO;
	}*/
	/**
	 * shiro session的管理
	 */
	/*public DefaultWebSessionManager SessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(redisSessionDAO());
		return sessionManager;
	}*/
	
	/*@Bean
	public SimpleCookie rememberMeCookie(){

	       //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
	       SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
	       //<!-- 记住我cookie生效时间30天 ,单位秒;-->
	       simpleCookie.setMaxAge(maxAge * 24 * 60 * 60);
	       simpleCookie.setHttpOnly(true);
	       return simpleCookie;
	}*/
	/**  
	  * cookie管理对象;  
	  * @return  
	  */  
	@Bean
	public CookieRememberMeManager rememberMeManager(){  
	  
	       System.out.println("ShiroConfiguration.rememberMeManager()");  
	       CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();  
	      // cookieRememberMeManager.setCipherKey(org.apache.shiro.codec.Base64.decode("6ZmI6I2j5Y+R5aSn5ZOlAA=="));
	      // cookieRememberMeManager.setCookie(rememberMeCookie());
	       return cookieRememberMeManager;  
	}
	
	  /**
     * Shiro生命周期处理器,static解决配置文件@value取值为空
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }
    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

	/**
	 * 配置session监听
	 * @return
	 */
	@Bean("sessionListener")
	public ShiroSessionListener sessionListener(){
		ShiroSessionListener sessionListener = new ShiroSessionListener();
		return sessionListener;
	}

	/**
	 * 配置会话ID生成器
	 * @return
	 */
	@Bean
	public SessionIdGenerator sessionIdGenerator() {
		return new JavaUuidSessionIdGenerator();
	}

	/**
	 * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
	 * MemorySessionDAO 直接在内存中进行会话维护
	 * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
	 * @return
	 */
	@Bean
	public SessionDAO sessionDAO() {
		EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
		//使用ehCacheManager
		//enterpriseCacheSessionDAO.setCacheManager(ehCacheManager());
		//设置session缓存的名字 默认为 shiro-activeSessionCache
		enterpriseCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
		//sessionId生成器
		enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
		return enterpriseCacheSessionDAO;
	}

	/**
	 * 配置保存sessionId的cookie
	 * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
	 * @return
	 */
	@Bean("sessionIdCookie")
	public SimpleCookie sessionIdCookie(){
		//这个参数是cookie的名称
		SimpleCookie simpleCookie = new SimpleCookie("sid");
		//setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

		//setcookie()的第七个参数
		//设为true后，只能通过http访问，javascript无法访问
		//防止xss读取cookie
		simpleCookie.setHttpOnly(true);
		simpleCookie.setPath("/");
		//maxAge=-1表示浏览器关闭时失效此Cookie
		simpleCookie.setMaxAge(-1);
		// 记住我cookie生效时间30天 ,单位秒;
		//simpleCookie.setMaxAge(maxAge * 24 * 60 * 60);
		return simpleCookie;
	}

	/**
	 * 配置会话管理器，设定会话超时及保存
	 * @return
	 */
	@Bean("sessionManager")
	public DefaultWebSessionManager sessionManager() { DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		Collection<SessionListener> listeners = new ArrayList<SessionListener>();
		//配置监听
		listeners.add(sessionListener());
		sessionManager.setSessionListeners(listeners);
		sessionManager.setSessionIdCookie(sessionIdCookie());
		sessionManager.setSessionDAO(sessionDAO());
		System.out.println(sessionDAO().getActiveSessions());
		sessionManager.setCacheManager(ehCacheManager());
		System.out.println("expireTime:"+expireTime);
		//全局会话超时时间（单位毫秒），默认30分钟
		sessionManager.setGlobalSessionTimeout(expireTime * 60 * 1000);
		//是否开启删除无效的session对象  默认为true
		sessionManager.setDeleteInvalidSessions(true);
		//是否开启定时调度器进行检测过期session 默认为true
		sessionManager.setSessionValidationSchedulerEnabled(true);
		//设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
		//设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
		sessionManager.setSessionValidationInterval(validationInterval * 60 * 1000);

		//取消url 后面的 JSESSIONID
		sessionManager.setSessionIdUrlRewritingEnabled(false);

		return sessionManager;
	}

	/**
	 * thymeleaf模板引擎和shiro框架的整合
	 */
	@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}

	/**
	 * 缓存管理器
	 * @return
	 */
	@Bean
	public EhCacheManager ehCacheManager(){
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
		return cacheManager;
	}

	/**
	 * 并发登录控制
	 * @return
	 */
	@Bean
	public KickoutSessionControlFilter kickoutSessionControlFilter(){
		KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
		//用于根据会话ID，获取会话进行踢出操作的；
		kickoutSessionControlFilter.setSessionManager(sessionManager());
		//使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
		kickoutSessionControlFilter.setCacheManager(ehCacheManager());
		//是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；
		kickoutSessionControlFilter.setKickoutAfter(false);
		//同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
		kickoutSessionControlFilter.setMaxSession(1);
		//被踢出后重定向到的地址；
		kickoutSessionControlFilter.setKickoutUrl("/login?kickout=1");
		return kickoutSessionControlFilter;
	}


}