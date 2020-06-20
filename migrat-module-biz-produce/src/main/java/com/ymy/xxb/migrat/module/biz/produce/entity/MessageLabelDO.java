package com.ymy.xxb.migrat.module.biz.produce.entity;

import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 消息标签实体类
 *
 *
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageLabelDO extends BaseVo{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 适用模块
	 */
	@ApiModelProperty(value = "适用模块", name="applyModule", dataType="String", required = true)
	private String applyModule;

	/**
	 * 标签名称
	 */
	@ApiModelProperty(value = "标签名称", name="labelName", dataType="String", required = true)
	private String labelName;
	
	/**
	 * 标签别名
	 */
	@ApiModelProperty(value = "标签别名", name = "labelAlias")
	private String labelAlias;
	
	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序", name = "sort")
	private Integer sort;
	
	/**
     * 状态
     */
	@ApiModelProperty(value = "状态", name="state", dataType="Integer", notes="1：启用  0：禁用" , required = true)
    private Integer state;


}
