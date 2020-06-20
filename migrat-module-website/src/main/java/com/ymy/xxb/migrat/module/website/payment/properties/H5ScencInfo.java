package com.ymy.xxb.migrat.module.website.payment.properties;

import com.alibaba.fastjson.JSON;

/**
 *  场景信息
 *  @author wangyi.
 */

public class H5ScencInfo {
	
	private H5 h5_info;
	
	public H5 H5_info() {
		
		return h5_info;
	}
	
	public H5 getH5_info() {
		return h5_info;
	}

	public void setH5_info(H5 h5_info) {
		this.h5_info = h5_info;
	}
	
	
	@Override
	public String toString() {
		return JSON.toJSONString(h5_info);
	}
	
	public static class H5{
		// H5页面类型
		private String type;
		// 应用的信息
		private String app_name;
		// 绑定的ID
		private String bundle_id;
		// 包名
		private String package_name;
		// WAP网站URL
		private String wap_url;
		// WAP网站名称
		private String wap_name;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getApp_name() {
			return app_name;
		}
		public void setApp_name(String app_name) {
			this.app_name = app_name;
		}
		public String getBundle_id() {
			return bundle_id;
		}
		public void setBundle_id(String bundle_id) {
			this.bundle_id = bundle_id;
		}
		public String getPackage_name() {
			return package_name;
		}
		public void setPackage_name(String package_name) {
			this.package_name = package_name;
		}
		public String getWap_url() {
			return wap_url;
		}
		public void setWap_url(String wap_url) {
			this.wap_url = wap_url;
		}
		public String getWap_name() {
			return wap_name;
		}
		public void setWap_name(String wap_name) {
			this.wap_name = wap_name;
		}
	}
	
}
