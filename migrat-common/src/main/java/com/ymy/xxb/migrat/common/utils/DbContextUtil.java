package com.ymy.xxb.migrat.common.utils;

import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.exception.TenantException;

public class DbContextUtil {

	private static final String nameSpacePrefix = Constants.TENANT_DATABASE_NAMESPACE_PREFIX;

	/**
	 * 获取业务子系统的名称
	 * 
	 * @param tenantGroupId
	 *            租户所属数据库组序号
	 */
	public static String getBizDbName(Integer tenantGroupId) {
		return nameSpacePrefix + "-biz-" + tenantGroupId;
	}

	/**
	 * 通过租户ID获取数据库名称
	 * 
	 * @param tenantId
	 * @return
	 */
	public static String getBizNameByTenantId(String tenantId) {
		Integer tenantIdGroupUnit = Constants.TENANT_ID_GROUP_UNIT;
		if (tenantId == null) {
			throw new TenantException("用户校验异常，当前用户未获取到租户ID ...");
		} else {
			Integer tenantGroupId = (Integer.valueOf(tenantId) - 1) / (tenantIdGroupUnit) + 1;
			return getBizDbName(tenantGroupId);
		}
	}
}
