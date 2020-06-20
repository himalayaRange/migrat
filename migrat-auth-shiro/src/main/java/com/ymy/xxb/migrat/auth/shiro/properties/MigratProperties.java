package com.ymy.xxb.migrat.auth.shiro.properties;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "migrat")
public class MigratProperties {
	
	/**
	 * 项目启动后是否自动打开浏览器
	 */
	private boolean autoOpenBrowser = true;
	
	/**
	 * 环境配置
	 */
	private String[] autoOpenBrowserEnv = {};
	
	/**
	 * 是否进行SQL拦截配置
	 */
	private String[] sqlFilterPath = {};

}
