package com.ymy.xxb.migrat.auth.shiro.mapper;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author: wangyi
 *
 */
public interface IdempotentTokenMapper {

	/**
	 * 创建Token
	 * @return
	 */
	public String createToken();
	

	/**
	 * 校验Token
	 * @param request
	 * @return
	 */
	public boolean checkToken(HttpServletRequest request) throws Exception;
	
}
