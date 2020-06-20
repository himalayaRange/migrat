package com.ymy.xxb.migrat.module.biz.produce.entity;

import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 通知模板-通知节点中间表
 *
 *
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InformLabelTemplateDO extends BaseVo{

	private static final long serialVersionUID = 1L;

	/**
	 * 模板ID
	 */
	@ApiModelProperty(value = "模板ID", name="tempId", dataType="String", required = true)
	private String tempId;

	/**
	 * 标签ID
	 */
	@ApiModelProperty(value = "标签ID", name="labelId", dataType="String",required = true)
	private String labelId;

}
