package com.ymy.xxb.migrat.module.comyany.entity;

import java.io.Serializable;
import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 字典明细表
 * 
 * @author yl
 * 
 */

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DictionaryDetailDO extends BaseVo implements Serializable{
    
	private static final long serialVersionUID = 1L;

	/**
     * 主表主键
     */
	@ApiModelProperty(value = "主表主键", name = "mid", dataType = "String", required = true)
    private String mid;


    /**
     * 级别
     */
	@ApiModelProperty(value = "级别", name = "level", dataType = "Integer", required = true)
    private Integer level;

    /**
     * 排序号
     */
	@ApiModelProperty(value = "排序号", name = "orderNo", dataType = "Integer")
    private Integer orderNo;

    /**
     * 编码
     */
	@ApiModelProperty(value = "编码", name = "code", dataType = "String", required = true)
    private String code;

    /**
     * 名称
     */
	@ApiModelProperty(value = "名称", name = "name", dataType = "String", required = true)
    private String name;

    /**
     * 启用状态： 1 启用 0 禁用
     */
	@ApiModelProperty(value = "启用状态： 1 启用 0 禁用", name = "state", dataType = "String", required = true)
    private Byte state;

    /**
     * 备注
     */
	@ApiModelProperty(value = "备注", name = "memo", dataType = "String")
    private String memo;

}