package com.ymy.xxb.migrat.module.website.constant;

/**
 * 模块控制器定义
 *
 * @author: wangyi
 *
 */
public interface ModuleConst {
	
	/*
	 * 需授权模块
	 */
	class Api {
		
		/* 支付宝支付模块 */
		public static final String API_MODULE_ALIPAY = "/alipay";
		
		/* 微信支付模块 */
		public static final String API_MODULE_WXPAY = "/wxpay";
		
	}
	
	/*
	 * 无需授权模块
	 */
	class Access {
		/* 支付宝支付回调 */
		public static final String API_MODULE_ALIPAY_CALLBACK = "/aliPayCallback";
		
		/* 微信支付回调 */
		public static final String API_MODULE_WXPAY_CALLBACK = "/wxPayCallback";
		
		/*关于我们 */
		public static final String API_MODULE_ABOUTUS = "/aboutUs";
		
		/*联系我们 */
		public static final String API_MODULE_TENANT_APPLY = "/tenantApply";
		
	}
}
