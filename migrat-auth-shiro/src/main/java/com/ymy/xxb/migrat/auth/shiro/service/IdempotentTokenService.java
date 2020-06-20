package com.ymy.xxb.migrat.auth.shiro.service;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ymy.xxb.migrat.auth.shiro.constant.ShiroConst;
import com.ymy.xxb.migrat.auth.shiro.mapper.IdempotentTokenMapper;
import com.ymy.xxb.migrat.common.service.RedisService;

@Service
public class IdempotentTokenService implements IdempotentTokenMapper {
	@Autowired
	private RedisService redisService;
	
	@Override
	public String createToken() {
		String str = UUID.randomUUID().toString().replaceAll("-", "");
		StringBuilder token = new StringBuilder();
		try {
			token.append(ShiroConst.AUTO_IDEMPOTENT_PREFIX).append(str);
			redisService.setEx(token.toString(), token.toString(), 10000L);
			if (StringUtils.isNotEmpty(token.toString())) 
				return token.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean checkToken(HttpServletRequest request) throws Exception {
		String token = request.getHeader(ShiroConst.HEADER_IDEMPOTENT);
		if (StringUtils.isBlank(token)) {
			throw new Exception("the header is null !");
		}

		if (!redisService.exist(token)) {
			throw new Exception("the header is not exist !");
		}
		
		boolean remove = redisService.remove(token);
		if (!remove) {
			throw new Exception("the header remove failed !");
		} 
		
		return true;
	}

}
