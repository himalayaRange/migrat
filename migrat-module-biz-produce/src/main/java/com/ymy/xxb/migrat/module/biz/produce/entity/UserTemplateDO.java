package com.ymy.xxb.migrat.module.biz.produce.entity;

import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户消息组合实体类
 *
 *
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserTemplateDO extends BaseVo{

	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名", name="username", dataType="String")
	private String username;
	
	/**
	 * 角色名称
	 */
	@ApiModelProperty(value = "角色名称", name="roleName", dataType="String", required = true)
	private String roleName;

	/**
	 * 模板名称
	 */
	@ApiModelProperty(value = "模板名称", name="templateTitle", dataType="String", required = true)
	private String templateTitle;
	
	/**
	 * 消息内容
	 */
	@ApiModelProperty(value = "消息内容", name = "messageContent", dataType = "String")
	private String messageContent;

	/**
	 * 模板编码
	 */
	@ApiModelProperty(value = "模板编码", name="templateCode", dataType="String")
	private String templateCode;

	/**
	 * 规则名称
	 */
	@ApiModelProperty(value = "规则名称", name="ruleName", dataType="String")
	private String ruleName;

	/**
	 * 规则别名
	 */
	@ApiModelProperty(value = "规则别名", name="ruleAlias", dataType="String")
	private String ruleAlias;


}
