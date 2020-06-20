package com.ymy.xxb.migrat.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 注解记录日志
 * @author wangyi
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerEndpoint {
	
	String operation() default "";
	
	String exceptionMessage() default "Migrat系统内容异常";
}
