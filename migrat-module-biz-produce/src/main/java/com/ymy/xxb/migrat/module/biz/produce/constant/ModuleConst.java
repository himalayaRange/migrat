package com.ymy.xxb.migrat.module.biz.produce.constant;

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
		
		/* 通知模块 */
		public static final String API_MODULE_NOTIFY = "/notify";

		/* 消息标签 */
		public static final String API_MODULE_MESSAGE_LABEL = "/messageLabel";

		/* 通知节点 */
		public static final String API_MODULE_INFORM_NODE = "/informNode";

		/* 消息模板 */
		public static final String API_MODULE_INFORM_TEMPLATE = "/informTemplate";

		/* 消息发送 */
		public static final String API_MODULE_MESSAGE_SEND = "/messageSend";

		/* 角色模板绑定 */
		public static final String API_MODULE_ROLE_TEMPLE = "/roleTemple";

		/* 基础模块配置 */
		public static final String API_MODULE_BASIC_MODULE = "/basicModule";
		
		/* 授权RPC调用DEMO模块*/
		public static final String API_MODULE_RPC = "/rpc";
		
		public static final String API_MODULE_GROUP_RULE= "/groupRule";
	}
	
	/*
	 * 无需授权模块
	 */
	class Access {
		
		/* Nacos模块 */
		public static final String API_MODULE_NACOS= "/nacos";
		
		/* 微服务RPC调用DEMO模块 */
		public static final String API_MODULE_RPC = "/rpc";
	} 
	
	
}
