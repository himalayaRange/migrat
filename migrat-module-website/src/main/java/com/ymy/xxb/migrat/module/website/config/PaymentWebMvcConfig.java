package com.ymy.xxb.migrat.module.website.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.ymy.xxb.migrat.module.website.payment.filter.AliPayInterceptor;
import com.ymy.xxb.migrat.module.website.payment.filter.WxPayInterceptor;

@Configuration
public class PaymentWebMvcConfig implements WebMvcConfigurer{

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(new AliPayInterceptor()).addPathPatterns("/authenticationApi/alipay/**");
		
		registry.addInterceptor(new WxPayInterceptor()).addPathPatterns("/authenticationApi/wxpay/**");

	}
	
}
