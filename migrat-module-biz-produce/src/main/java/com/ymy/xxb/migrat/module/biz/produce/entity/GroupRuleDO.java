package com.ymy.xxb.migrat.module.biz.produce.entity;

import java.util.Date;
import java.util.List;
import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 分组规则实体类
 *
 *
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupRuleDO extends BaseVo{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 适用模块
	 */
	@ApiModelProperty(value = "规则名称", name="ruleName", dataType="String", required = true)
	private String ruleName;

	/**
	 * 规则别名
	 */
	@ApiModelProperty(value = "规则别名", name="ruleAlias", dataType="String", required = true)
	private String ruleAlias;
	
	/**
	 * 规则说明
	 */
	@ApiModelProperty(value = "规则说明", name = "ruleExplain" , dataType="String")
	private String ruleExplain;
	
	/**
     * 状态
     */
	@ApiModelProperty(value = "规则状态", name="state", dataType="Integer", notes="1：启用  0：禁用" , required = true)
    private Integer state;

	/**
     * 创建人
     */
	@ApiModelProperty(value = "创建人", name="createUser", dataType="String")
    private String createUser;
	
	/**
     * 创建时间
     */
	@ApiModelProperty(value = "规则状态", name="createTime", dataType="Date")
    private Date createTime;
	
	/**
     * 修改人
     */
	@ApiModelProperty(value = "修改人", name="modifyUser", dataType="String")
    private String modifyUser;
	
	/**
     * 修改时间
     */
	@ApiModelProperty(value = "修改时间", name="modifyTime", dataType="Date")
    private Date modifyTime;
	
	/**
	 * 租户ID
	 */
	@ApiModelProperty(value = "租户ID", name="tenantId", dataType="String")
	private String tenantId;

	/**
	 * 模板集合
	 */
	@ApiModelProperty(value = "模板集合", name="informTemplates", dataType="List")
	private List<InformTemplateDO> informTemplates;
}
