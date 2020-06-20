package com.ymy.xxb.migrat.auth.shiro.entity;

import java.io.Serializable;
import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 部门实体类
 * 
 * @author wangyi
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DepartmentDO extends BaseVo implements Serializable{

	private static final long serialVersionUID = -6584424192828951997L;
	
	/**
	 * 父级ID
	 */
	@ApiModelProperty(value = "父级ID", name = "parentId", dataType = "String")
	private String parentId;
	
	/**
	 * 公司ID
	 */
	@ApiModelProperty(value = "公司ID", name = "companyId", dataType = "String", required = true)
	private String companyId;
	
	/**
	 * 部门名称
	 */
	@ApiModelProperty(value = "部门名称", name = "name", dataType = "String", required = true)
	private String name;
	
	/**
	 * 部门编码，同级别部门不可重复
	 */
	@ApiModelProperty(value = "部门编码，同级别部门不可重复", name = "code", dataType = "String", required = true)
	private String code;
	
	/**
	 * 部门类别
	 */
	@ApiModelProperty(value = "部门类别,从字典表中获取，传入categoryCode的值为'dept_category', @See{/authenticationApi/dictionaryMain/queryDictionaryDetail}", name = "category", dataType = "String")
	private String category;
	
	/**
	 * 负责人的ID
	 */
	@ApiModelProperty(value = "负责人ID", name = "managerId", dataType = "String")
	private String managerId;
	
	/**
	 * 负责人
	 */
	@ApiModelProperty(value = "负责人", name = "manager", dataType = "String")
	private String manager;
	
	/**
	 * 所属城市
	 */
	@ApiModelProperty(value = "所属城市", name = "city", dataType = "String")
	private String city;
	
	/**
	 * 介绍
	 */
	@ApiModelProperty(value = "介绍", name = "introduce", dataType = "String")
	private String introduce;
	
	/**
	 * 状态 1：启用  0：禁用
	 */
	@ApiModelProperty(value = "状态 1：启用  0：禁用", name = "state", dataType = "Integer")
	private Integer state;
	
}
