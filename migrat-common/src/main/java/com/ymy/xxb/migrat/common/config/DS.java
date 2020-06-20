package com.ymy.xxb.migrat.common.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.ymy.xxb.migrat.common.constant.Constants;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface DS {
	
	/**
	 * 
	 * Constants.DEFAULT_DS ： 默认的库路由
	 * 
	 * Constants.MANUAL_ROUTING ： 手动路由，由于系统由shiro管理，未登录状态下无法自动路由，
	 * 		
	 * 							   一般用于系统初始化需要路由的库，需要在方法执行前设置库到ThreadLocal中
	 * 
	 * @see com.ymy.xxb.migrat.common.config.DataSourceContextHolder
	 * 
	 * Constatns.AUTO_ROUTING : 完全自动路由，路由的库从shiro中获取，所以需要在登录状态下
	 * 
	 * @return
	 */
	String value() default  Constants.DEFAULT_DS;
	
}
