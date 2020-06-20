package com.ymy.xxb.migrat.module.biz.produce.entity;

import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 基础模块配置实体类
 *
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BasicModuleDO extends BaseVo {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 上级模块ID
	 */
	@ApiModelProperty(value = "上级模块ID", name="pid", dataType="String", required = true)
	private String pid;

	/**
	 * 模块编码
	 */
	@ApiModelProperty(value = "模块编码", name="moduleCode", dataType="String", required = true)
	private String moduleCode;
	
	/**
	 * 模块名称
	 */
	@ApiModelProperty(value = "模块名称", name = "moduleName", dataType = "String", required = true)
	private String moduleName;

	/**
	 * 模块类型： 0.模块 1.按钮
	 */
	@ApiModelProperty(value = "类型： 0.模块 1.按钮", name = "type", dataType="Integer", required = true)
	private Integer type;

	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述", name = "description" , dataType = "String")
	private String description;

	
	/**
     * 状态
     */
	@ApiModelProperty(value = "状态", name="state", dataType="Integer", notes="1：启用  0：禁用" , required = true)
    private Integer state;

}
