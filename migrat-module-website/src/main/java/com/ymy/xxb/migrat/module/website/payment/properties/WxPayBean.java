package com.ymy.xxb.migrat.module.website.payment.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "wxpay")
public class WxPayBean {

	// 公众号账号ID
	private String appId;
	// 秘钥
	private String appSecret;
	// 商户号
	private String mchId;
	// 商户秘钥
	private String partnerKey;
	// 证书路径
	private String certPath;
	// 域名
	private String domain;

}
