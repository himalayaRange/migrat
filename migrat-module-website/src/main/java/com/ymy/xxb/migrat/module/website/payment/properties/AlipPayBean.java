package com.ymy.xxb.migrat.module.website.payment.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "alipay")
public class AlipPayBean {
	// appId
	private String appId;
	// 私钥
	private String privateKey;
	// 支付宝公钥
	private String publicKey;
	// 网关
	private String serverUrl;
	// 域名
	private String domain;
	
}
