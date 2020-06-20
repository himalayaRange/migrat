package com.ymy.xxb.migrat.module.website.entity;

import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 联系信息录入表
 */

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TenantApplyDO extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称", name = "companyName", dataType = "String", required = true)
    private String companyName;
    /**
     * 公司地址
     */
    @ApiModelProperty(value = "公司地址", name = "companyAddress", dataType = "String")
    private String companyAddress;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", name = "mailbox", dataType = "String")
    private String mailbox;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", name = "mobile", dataType = "String", required = true)
    private String mobile;
    /**
     * 公司座机
     */
    @ApiModelProperty(value = "座机号", name = "telePhone", dataType = "String")
    private String telePhone;
    /**
     * 留言我们
     */
    @ApiModelProperty(value = "留言我们", name = "message", dataType = "String")
    private String message;
    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态:1 审核 0 未审核", name = "state", dataType = "Byte",required = true)
    private Byte state;
    /**
     * 状态名称
     */
    @ApiModelProperty(value = "审核状态名称", name = "stateName", dataType = "String", hidden = true)
    private String stateName;


}
