package com.ymy.xxb.migrat.module.biz.produce.entity;

import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 消息通知配置实体类
 *
 * @author: wangyi
 *
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotifyDO extends BaseVo{

	private static final long serialVersionUID = -7403446639097953994L;
	
	/**
	 * 用户ID
	 */
	@ApiModelProperty(value = "消息通知用户的ID", name="userId", dataType="String", required = true)
	private String userId;

	/**
	 * 生产阶段ID
	 */
	@ApiModelProperty(value = "生产阶段ID", name="stageId", dataType="String", required = true)
	private String stageId;
	
	/**
	 * 生产阶段ID,根据stageId关联出来
	 */
	@ApiModelProperty(value = "生产阶段名称，根据stageId关联出来", name = "stageName", hidden = true)
	private String stageName;
	
	/**
	 * 消息通知类型
	 */
	@ApiModelProperty(value = "消息通知类型 1：提前通知 2：定时通知 3：延迟通知", name = "notifyType", required = true)
	private String notifyType;
	
	/**
	 * 消息通知方式
	 */
	@ApiModelProperty(value = "消息通知方式 1：微信小程序通知 2：邮件通知 3：短息通知", name = "notifyWay", required = true)
	private String notifyWay;
	
	/**
	 * 通知时间间隔
	 */
	@ApiModelProperty(value = "通知时间间隔，提前通知为负数，定时通知为0，延迟通知为正数", name = "timeTtl", required = true)
	private Double timeTtl;
	
	/**
	 * 时间单位
	 */
	@ApiModelProperty(value = "时间单位 1： 天 2：小时 3：分钟", name = "timeUnit", required = true)
	private String timeUnit;
	
	/**
     * 状态
     */
	@ApiModelProperty(value = "状态", name="state", dataType="Integer", notes="1：启用  0：禁用" , required = true)
    private Integer state;
}
