package com.ymy.xxb.migrat.auth.shiro.entity;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ymy.xxb.migrat.common.annotation.IsMobile;
import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 企业用户表
 *
 * @author: wangyi
 *
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BsUserDO extends BaseVo{
	
	private static final long serialVersionUID = -1159646721645062459L;
	
	/**
	 * 手机号
	 */
	@IsMobile(message = "手机号码不正确")
	@ApiModelProperty(value = "手机号", name="mobile", dataType="String", required=true)
	private String mobile;
	
	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名", name="username", dataType="String", required=true)
	private String username;
	
	/**
	 * 真实姓名
	 */
	@ApiModelProperty(value = "真实姓名", name = "realName", dataType="String", required = true)
	private String realName;
	
	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码", name="password", dataType="String", required=true, hidden = false)
	private String password;
	
	/**
	 * 公司ID
	 */
	@ApiModelProperty(value = "公司ID", name="companyId", dataType="String", required=true)
	private String companyId;
	
	/**
	 * 部门ID
	 */
	@ApiModelProperty(value = "部门ID", name="departmentId", dataType="String", required=false)
	private String departmentId;
	
	/**
	 * 入职时间
	 */
	@DateTimeFormat(pattern = DATE_FORMAT)
	@JsonFormat(pattern = DATE_FORMAT, timezone = "GMT+8")
	@ApiModelProperty(value = "入职时间", name="timeOfEntry", dataType="Date")
	private Date timeOfEntry;
	
	/**
	 * 员工号
	 */
	@ApiModelProperty(value = "员工号", name="workNumber", dataType="String")
	private String workNumber;
	
	/**
	 * 用户头像地址
	 */
	@ApiModelProperty(value = "用户头像地址", name="avatar", dataType="String")
	private String avatar;
	
	/**
	 * 转正时间
	 */
	@DateTimeFormat(pattern = DATE_FORMAT)
	@JsonFormat(pattern = DATE_FORMAT, timezone = "GMT+8")
	@ApiModelProperty(value = "转正时间", name="correctionTime", dataType="Date")
	private Date correctionTime;
	
	/**
     * 状态
     */
	@ApiModelProperty(value = "状态", name="state", dataType="Integer", notes="1：启用  0：禁用" , required = true)
    private Integer state;
	
	/**
     * level
     *     String
     *          saasAdmin：saas管理员具备所有权限
     *          coAdmin：企业管理（创建租户企业的时候添加）
     *          user：普通用户（需要分配角色）
     */
	@ApiModelProperty(value = "saasAdmin：saas管理员具备所有权限，coAdmin：企业管理（创建租户企业的时候添加），user：普通用户（需要分配角色）" , name = "level" , dataType = "String", required = true)
	private String level;
	
	/**
	 * 用户角色JSON
	 */
	@ApiModelProperty(value= "用户角色JSON", name = "bsRoles", dataType = "String", hidden=true)
	private String bsRoles;
	
	/**
	 * 用户角色数组[新增，修改]
	 */
	@ApiModelProperty(value = "用户角色数组", name = "roleDropList", dataType = "String", notes = "用户角色，多个角色直接用,隔开，传入角色的ID" , required = false)
	private String roleDropList;
	
	/**
	 * 部门类别
	 */
	@ApiModelProperty(value = "部门类别", name = "category", dataType = "String", hidden=true)
	private String category;
	
	/**
	 * 部门名称
	 */
	@ApiModelProperty(value= "部门名称", name = "deptName", dataType = "String", hidden=true)
	private String deptName;
}
