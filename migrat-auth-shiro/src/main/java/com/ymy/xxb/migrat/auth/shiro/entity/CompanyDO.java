package com.ymy.xxb.migrat.auth.shiro.entity;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 企业公司实体类 
 *
 * @author wangyi
 *
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CompanyDO extends BaseVo {

	private static final long serialVersionUID = 3152703013418827130L;

	/**
	 * 公司名称
	 */
	@ApiModelProperty(value = "公司名称", name="name", dataType="String", required=true)
	private String name;
	
	/**
	 * 企业登陆的账号ID
	 */
	@ApiModelProperty(value = "企业登陆的账号ID", name="managerId", dataType="String", required=true)
	private String managerId;
	
	/**
	 * 当前的版本
	 */
	@ApiModelProperty(value = "当前的版本", name="version", dataType="String")
	private String version;
	
	/**
	 * 续期时间
	 */
	@DateTimeFormat(pattern = DATE_FORMAT)
	@JsonFormat(pattern = DATE_FORMAT, timezone = "GMT+8")
	@ApiModelProperty(value = "续期时间", name="renewalDate", dataType="Date")
	private Date renewalDate;
	
	/**
	 * 到期时间
	 */
	@JsonFormat(pattern = DATE_FORMAT, timezone = "GMT+8")
	@ApiModelProperty(value = "到期时间", name="expirationDate", dataType="Date")
	private Date expirationDate;
	
	/**
	 * 公司地区
	 */
	@ApiModelProperty(value = "公司地区", name="companyArea", dataType="String")
	private String companyArea;
	
	@ApiModelProperty(value = "公司地址", name="companyAddress", dataType="String")
	private String companyAddress;
	
	/**
	 * 营业执照-图片ID
	 */
	@ApiModelProperty(value = "营业执照-图片ID", name="businessLicenseId", dataType="String")
	private String businessLicenseId;
	
	/**
	 * 法人代表
	 */
	@ApiModelProperty(value = "法人代表", name="legalRepresentative", dataType="String")
	private String legalRepresentative;
	
	/**
	 * 公司电话
	 */
	@ApiModelProperty(value = "公司电话", name="companyPhone", dataType="String")
	private String companyPhone;
	
	/**
	 * 邮箱
	 */
	@ApiModelProperty(value = "邮箱", name="mailbox", dataType="String")
	private String mailbox;
	
	/**
     * 公司规模
     */
	@ApiModelProperty(value = "公司规模", name="companySize", dataType="String")
    private String companySize;
    
    /**
     * 所属行业
     */
	@ApiModelProperty(value = "所属行业", name="industry", dataType="String")
    private String industry;
    
    /**
     * 备注
     */
	@ApiModelProperty(value = "备注", name="remarks", dataType="String")
    private String remarks;
    
    /**
     * 审核状态
     */
	@ApiModelProperty(value = "审核状态", name="auditState", dataType="String", notes="1:审核通过 0:未审核" , required = true)
    private String auditState;
    
    /**
     * 状态
     */
	@ApiModelProperty(value = "状态", name="state", dataType="Integer", notes="1：启用  0：禁用" , required = true)
    private Integer state;
    
    /**
     * 当前余额
     */
	@ApiModelProperty(value = "当前余额", name="balance", dataType="Double", required = true)
    private Double balance;
  
}
