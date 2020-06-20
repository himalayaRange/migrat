package com.ymy.xxb.migrat.auth.shiro.aspect;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.ymy.xxb.migrat.auth.shiro.service.IdempotentTokenService;
import com.ymy.xxb.migrat.common.annotation.AutoIdempotent;
import com.ymy.xxb.migrat.common.aspect.AspectSupport;

@Aspect
@Component
public class AutoIdempotentAspect extends AspectSupport {
	@Autowired
	private IdempotentTokenService tokenService;

	@Pointcut("@annotation(com.ymy.xxb.migrat.common.annotation.AutoIdempotent)")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint point) throws Exception {
		Method method = resolveMethod(point);
		AutoIdempotent annotation = method.getAnnotation(AutoIdempotent.class);
		try {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = attr.getRequest();
			boolean checkToken = tokenService.checkToken(request);
			if (checkToken) {
				return point.proceed();
			}
			throw new Exception("稍等，勿重复提交！");
		} catch (Exception ex) {
			throw new Exception("校验失败，请求终止！");
		} catch (Throwable e) {
			throw new RuntimeException("point proceed failed !");
		}
	}
}
