package com.ymy.xxb.migrat.module.biz.produce.entity;

import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 通知模板实体类
 *
 *
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InformTemplateDO extends BaseVo{

	private static final long serialVersionUID = 1L;

	/**
	 * 节点名称
	 */
	@ApiModelProperty(value = "发送节点", name="sendAlias", dataType="String")
	private String sendAlias;
	
	/**
	 * 发送节点
	 */
	@ApiModelProperty(value = "发送节点", name="sendNode", dataType="String", required = true)
	private String sendNode;

	/**
	 * 模板标题
	 */
	@ApiModelProperty(value = "模板标题", name="templateTitle", dataType="String", required = true)
	private String templateTitle;
	
	/**
	 * 模板编号
	 */
	@ApiModelProperty(value = "模板编号", name = "templateCode", dataType = "String", required = true)
	private String templateCode;

	/**
	 * 消息内容
	 */
	@ApiModelProperty(value = "消息内容", name="messageContent", dataType="String")
	private String messageContent;
	
	/**
     * 状态
     */
	@ApiModelProperty(value = "状态", name="state", dataType="Integer", notes="1：启用  0：禁用" , required = true)
    private Integer state;


}
