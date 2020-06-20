package com.ymy.xxb.migrat.auth.shiro.session;

import java.io.Serializable;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.util.WebUtils;
import com.ymy.xxb.migrat.auth.shiro.constant.ShiroConst;

public class MySessionManager extends DefaultWebSessionManager {
 
    private static final String AUTHORIZATION = ShiroConst.HEADER_SESSION_ID;
 
    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";
 
    public MySessionManager() {
        super();
    }
 
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String id = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
        //如果请求头中有 Authorization 则其值为sessionId
        if (!StringUtils.isEmpty(id)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        } else {
            //否则按默认规则从cookie取sessionId
            return super.getSessionId(request, response);
        }
    }
    
    /**
     * 解决一次请求频繁调用redis问题，将sessionId缓存到request，第一次请求redis,后续还有10次左右从request中获取
     */
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException  {
    	Serializable sessionId = getSessionId(sessionKey);
    	 ServletRequest request = null;
    	 if (sessionKey instanceof WebSessionKey) {
    		 request = ((WebSessionKey) sessionKey).getServletRequest();
    	 }
    	 
    	 if (request != null && sessionId != null) {
    		 Object sessionObj = request.getAttribute(sessionId.toString());
    		if (sessionObj != null) {
    			return (Session) sessionObj;
    		}
    	 }
    	 
		Session session = super.retrieveSession(sessionKey);
		if (request != null && null != sessionId) {
			request.setAttribute(sessionId.toString(), session);
		}
		return session;
    }
}
