package com.ymy.xxb.migrat.auth.shiro.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSentinelManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ymy.xxb.migrat.auth.shiro.constant.ShiroConst;
import com.ymy.xxb.migrat.auth.shiro.constant.ShiroModuleConst;
import com.ymy.xxb.migrat.auth.shiro.filter.KickoutSessionControlFilter;
import com.ymy.xxb.migrat.auth.shiro.filter.PermissionFilter;
import com.ymy.xxb.migrat.auth.shiro.listener.ShiroSessionListener;
import com.ymy.xxb.migrat.auth.shiro.permission.UrlPermissionResolver;
import com.ymy.xxb.migrat.auth.shiro.realm.MyShiroRealm;
import com.ymy.xxb.migrat.auth.shiro.session.MySessionManager;
import com.ymy.xxb.migrat.common.constant.Constants;

/**
 * 
 * 当前shiro-redis都是基于jedis实现的，而项目springboot2.x默认是使用lettuce的
 *
 * @author: wangyi
 *
 */
@Configuration
@AutoConfigureAfter(ShiroLifecycleBeanPostProcessorConfig.class)
public class ShiroSentinelConfig {
	
	//  Sentinel Master
	@Value(("${spring.redis.sentinel.master}"))
	private String master;
	
	// Sentinel Nodes
	@Value(("${spring.redis.sentinel.nodes}"))
	private String nodes;
	
	@Value(("${spring.redis.password}"))
	private String password;
	
	@Value(("${spring.redis.database}"))
	private Integer database;
	
	@Value(("${spring.redis.timeout}"))
	private Integer timeout;
	
	@Value(("${spring.redis.expire}"))
	private int expire;
	
	@Value(("${shiro.session.global-session-timeout}"))
	private long globalSessionTimeout;
	
	@Value(("${shiro.session.session-validation-interval}"))
	private long sessionValidationInterval;
	
	@Value(("${kickout.max-session}"))
	private int maxSession;
    
	@Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        
        // 前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl(Constants.API_OPEN_PREFIX + ShiroModuleConst.MODULE_SSO + "/unauth");
        
        //	未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl(Constants.API_OPEN_PREFIX + ShiroModuleConst.MODULE_SSO + "/unauthorized");
        
        // 自定义拦截器
        LinkedHashMap<String, Filter> filtersMap = new LinkedHashMap<>();
        // 1.登录并发控制
        filtersMap.put("kickout", kickoutSessionControlFilter());
        // 2.鉴权拦截器
        filtersMap.put("perms", permissionFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);
        
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        filterChainDefinitionMap.put("/gateway/**", "anon"); // 网关入口不进行拦截，对应的接口权限后续拦截器匹配拦截
        filterChainDefinitionMap.put("/favicon","anon");
        filterChainDefinitionMap.put("/js/**","anon");
        filterChainDefinitionMap.put("/html/**","anon");
        filterChainDefinitionMap.put("/css/**","anon");
        filterChainDefinitionMap.put("/swagger-ui.html/**","anon");
        filterChainDefinitionMap.put("/webjars/**","anon");
        filterChainDefinitionMap.put("/swagger-resources/**","anon");
        filterChainDefinitionMap.put("/v2/api-docs/**","anon");
        filterChainDefinitionMap.put("/swagger-resources/configuration/ui/**","anon");
        filterChainDefinitionMap.put("/swagger-resources/configuration/security/**","anon");
        filterChainDefinitionMap.put("/swagger", "anon");
        filterChainDefinitionMap.put("/druid", "anon"); // 数据库监控不拦截
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/openApi/**", "anon");
        filterChainDefinitionMap.put("/authenticationApi/**", "authc");
        filterChainDefinitionMap.put("/**","authc");
        /**
         * 基于动态URL的权限控制
           filterChainDefinitionMap.put("/authenticationApi/**", "authc,perms");
           filterChainDefinitionMap.put("/**","authc,perms");
         */
        
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        
        return shiroFilterFactoryBean;
    }
 
    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了)
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(ShiroConst.SHIRO_HASHALGORITHMNAME); //散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(ShiroConst.SHIRO_HASHITERATIONS); //散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }
 
    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        // 自定义权限匹配器
        myShiroRealm.setPermissionResolver(urlPermissionResolver());
        // 自定义角色匹配器
        //myShiroRealm.setRolePermissionResolver(permissionRoleResolver());
        return myShiroRealm;
    }
 
 
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }
 
    //自定义sessionManager
    @Bean
    public SessionManager sessionManager() {
        MySessionManager sessionManager = new MySessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
 		// 禁用url重写 url：jsessionid=id
 		sessionManager.setSessionIdUrlRewritingEnabled(false);
 		// 超时时间
 		sessionManager.setGlobalSessionTimeout(globalSessionTimeout);
 		// 相隔多久检查一次session有效性:10分钟
 		sessionManager.setSessionValidationInterval(sessionValidationInterval);
 		// 删除失效的session
 		sessionManager.setDeleteInvalidSessions(true);
 		// 自定义监听器
 		sessionManager.setSessionListeners(shiroSessionListener());
 		// 解决shiro默认的Cookie和容器的JESSIONID冲突导致登录失效问题
 		sessionManager.setSessionIdCookie(simpleCookie());
        return sessionManager;
    }
    
    
    /**
     * 指定本系统SESSIONID, 默认为: JSESSIONID 问题: 与SERVLET容器名冲突, 如JETTY, TOMCAT 等默认JSESSIONID,
     * 当跳出SHIRO SERVLET时如ERROR-PAGE容器会为JSESSIONID重新分配值导致登录会话丢失
     */
    @Bean
    public SimpleCookie simpleCookie() {
    	SimpleCookie cookie = new SimpleCookie("shiro.session");
    	cookie.setPath("/");
    	return cookie;
    }
 
    /**
     * 配置shiro redisManager
     * <p>
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisSentinelManager redisSentinelManager() {
        RedisSentinelManager redisSentinelManager = new RedisSentinelManager();
        redisSentinelManager.setDatabase(database);
        redisSentinelManager.setPassword(password);
        redisSentinelManager.setMasterName(master);
        redisSentinelManager.setHost(nodes);
        redisSentinelManager.setTimeout(timeout);
        
        return redisSentinelManager;
    }
 
    /**
     * cacheManager 缓存 redis实现
     * <p>
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisSentinelManager());
        return redisCacheManager;
    }
 
    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * <p>
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setExpire(expire);
        redisSessionDAO.setRedisManager(redisSentinelManager());
        return redisSessionDAO;
    }
 
    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    
    /**
     * 并发登录控制
     * @return
     */
    public KickoutSessionControlFilter kickoutSessionControlFilter() {
    	KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
    	// 使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
    	kickoutSessionControlFilter.setCacheManager(cacheManager());
    	// 用于根据会话ID，获取会话进行踢出操作的；
    	kickoutSessionControlFilter.setSessionManager(sessionManager());
    	// 是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
    	kickoutSessionControlFilter.setKickoutAfter(false);
    	// 最大会话数
    	kickoutSessionControlFilter.setMaxSession(maxSession);
    	// 踢出后跳转地址
    	kickoutSessionControlFilter.setKickoutUrl(Constants.API_OPEN_PREFIX + ShiroModuleConst.MODULE_SSO + "/kickout");
    	
    	return kickoutSessionControlFilter;
    } 
    
    /**
     * 权限认证拦截器
     * @return
     */
    public PermissionFilter permissionFilter() {
    	PermissionFilter permissionFilter = new PermissionFilter();
    	permissionFilter.setErrorUrl(Constants.API_OPEN_PREFIX + ShiroModuleConst.MODULE_SSO + "/unauthorized");
    	return permissionFilter;
    }
    
    /**
     * 自定义权限匹配器
     * @return
     */
    public UrlPermissionResolver urlPermissionResolver() {
    	UrlPermissionResolver resolver = new UrlPermissionResolver();
    	return resolver;
    }
    
    /**
     * session监听器
     * @return
     */
    public Collection<SessionListener> shiroSessionListener() {
    	 Collection<SessionListener> listeners = new ArrayList<>();
         listeners.add(new ShiroSessionListener());
         return listeners;
    }
}
