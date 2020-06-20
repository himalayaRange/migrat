package com.ymy.xxb.migrat.module.biz.produce.entity;

import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 通知模板-分组规则中间表
 *
 *
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InformTemplateRuleDO extends BaseVo{

	private static final long serialVersionUID = 1L;

	/**
	 * 模板ID
	 */
	@ApiModelProperty(value = "模板ID", name="templeId", dataType="String", required = true)
	private String templeId;

	/**
	 * 规则ID
	 */
	@ApiModelProperty(value = "规则ID", name="ruleId", dataType="String",required = true)
	private String ruleId;

}
