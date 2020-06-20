package com.ymy.xxb.migrat.common.config;

import java.lang.reflect.Method;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.utils.DbContextUtil;
import com.ymy.xxb.migrat.common.vo.ProfileResult;

@Aspect
@Component
public class DynamicDataSourceAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

	@Before("@annotation(DS)")
	@SuppressWarnings("rawtypes")
	public void beforeSwitchDS(JoinPoint point) {
		Class<? extends Object> className = point.getTarget().getClass();
		String methodName = point.getSignature().getName();
		Class[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();
		// 默认主库
		String dataSource = DataSourceContextHolder.DEFAULT_DS;
		try {
			Method method = className.getMethod(methodName, argClass);
			if (method.isAnnotationPresent(DS.class)) {
				DS annotation = method.getAnnotation(DS.class);
				String dsValue = annotation.value();
				// 1. 完全自动路由
				if (Constants.AUTO_ROUTING.equals(dsValue)) {
					dataSource = autoRouting();
				} else if (Constants.MANUAL_ROUTING.equals(dsValue)) {
					// 2. 手动路由，从当前线程中获取路由值
					// 项目启动路由
					String startedContext = DataSourceContextHolder.getStartedContext();
					if (startedContext != null) {
						dataSource = DataSourceContextHolder.getTenantContext();
					} else {
						dataSource = dsValue;
					}
				}else {
					// 3. 完全手动路由，需要登录状态
					dataSource = dsValue;
				}
			}
		} catch (Exception e) {
			if(LOGGER.isErrorEnabled()) {
				LOGGER.error("switch dataScouce exception : {" + e.getMessage() + "}");
			}
			e.printStackTrace();
		}
		DataSourceContextHolder.setDB(dataSource);
	}

	@After("@annotation(DS)")
	public void afterSwithDS(JoinPoint point) {
		DataSourceContextHolder.clearDB();
	}
	
	
	/**
	 * 自动路由到指定的数据库
	 */ 
	private  String autoRouting() {
		String tenantContext = DataSourceContextHolder.getTenantContext();
		Object principal = SecurityUtils.getSubject().getPrincipal();
		String currentDataSourceName = null;
		if (principal == null) {
			if (tenantContext != null) {
				// 用户登录，从ThreadLocal获取，该值在登录时候设置，登录成功或者失败都会清除tenantContext
				currentDataSourceName = tenantContext;
			} else {
				// 未登录，从ThreadLocal获取，该值在登录时候设置，登录成功或者失败都会清除tenantContext
				currentDataSourceName = Constants.DEFAULT_DS;
			}
		} else {
			ProfileResult profile = (ProfileResult) principal;
			String tenantId = profile.getTenantId();
			currentDataSourceName = DbContextUtil.getBizNameByTenantId(tenantId);
		}
		return currentDataSourceName;
	}
}
