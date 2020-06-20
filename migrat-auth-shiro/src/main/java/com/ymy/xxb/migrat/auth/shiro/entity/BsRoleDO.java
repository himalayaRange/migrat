package com.ymy.xxb.migrat.auth.shiro.entity;

import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 企业角色 
 *
 * @author: wangyi
 *
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BsRoleDO extends BaseVo{
	
	private static final long serialVersionUID = -7486880505334687418L;
	
	/**
	 * 角色编码
	 */
	@ApiModelProperty(value = "角色编码", name="roleCode", dataType="String", required = true)
	private String roleCode;
	
	/**
	 * 角色名称
	 */
	@ApiModelProperty(value = "角色名称", name="roleName", dataType="String", required = true)
	private String roleName;
	
	/**
	 * 说明
	 */
	@ApiModelProperty(value = "角色描述", name="description", dataType="String")
	private String description;
	
	/**
     * 状态
     */
	@ApiModelProperty(value = "状态", name="state", dataType="Integer", notes="1：启用  0：禁用" , required = true)
    private Integer state;
	
	/**
	 * 角色权限JSON
	 */
	@ApiModelProperty(value = "角色权限JSON", name = "bsPermissions", dataType = "String", hidden = true)
	private String bsPermissions;
	
}
