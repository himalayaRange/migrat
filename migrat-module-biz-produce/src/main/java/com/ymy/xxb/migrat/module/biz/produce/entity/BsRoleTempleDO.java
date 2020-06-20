package com.ymy.xxb.migrat.module.biz.produce.entity;

import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色-消息模板关联表
 *
 *
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BsRoleTempleDO extends BaseVo{

	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@ApiModelProperty(value = "角色ID", name="roleId", dataType="String", required = true)
	private String roleId;

	/**
	 * 规则ID
	 */
	@ApiModelProperty(value = "规则ID", name="ruleId", dataType="String",required = true)
	private String ruleId;

	/**
	 * 消息模板ID
	 */
	@ApiModelProperty(value = "消息模板ID", name="templeId", dataType="String",required = true)
	private String templeId;

}
