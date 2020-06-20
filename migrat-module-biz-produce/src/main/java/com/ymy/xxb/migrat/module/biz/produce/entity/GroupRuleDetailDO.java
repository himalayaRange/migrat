package com.ymy.xxb.migrat.module.biz.produce.entity;

import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 表xx_group_rule_detail数据库Bean.
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupRuleDetailDO extends BaseVo {

	private static final long serialVersionUID = 1L;

	/**
     * 规则明细主键ID.
     */
	@ApiModelProperty(value = "规则明细主键ID", name="id", dataType="String", required = true)
    private String id;

    /**
     * 规则主键ID.
     */
	@ApiModelProperty(value = "规则主键ID", name="rid", dataType="String", required = true)
    private String rid;

    /**
     * 字典表主表id.
     */
	@ApiModelProperty(value = "字典表主表id", name="dicMid", dataType="String", required = true)
    private String dicMid;

    /**
     * 字典表明细表id.
     */
	@ApiModelProperty(value = "字典表明细表id", name="dicDetailId", dataType="String", required = true)
    private String dicDetailId;

    /**
     * 筛选条件.
     */
	@ApiModelProperty(value = "筛选条件", name="filter", dataType="String")
    private String filter;

    /**
     * 设定条件.
     */
	@ApiModelProperty(value = "设定条件", name="setCondition", dataType="String")
    private String setCondition;

    /**
     * 明细状态 1:启用 0:禁用.
     */
	@ApiModelProperty(value = "明细状态 1:启用 0:禁用", name="state", dataType="Byte")
    private Byte state;
	
	 /**
     * 租户ID
     */
	@ApiModelProperty(value = "租户ID", name="tenantId", dataType="String")
	private String tenantId;

}