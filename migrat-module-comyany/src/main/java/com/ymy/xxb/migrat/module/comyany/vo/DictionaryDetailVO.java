package com.ymy.xxb.migrat.module.comyany.vo;

import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DictionaryDetailVO extends BaseVo{
	private static final long serialVersionUID = -4674034729940962629L;
	
	/**
     * 主表主键
     */
	@ApiModelProperty(value = "主表主键", name = "mid", dataType = "String", required = true)
    private String mid;
	
	@ApiModelProperty(value = "字典类别，1.字典分类 2.字典明细", name = "type", dataType = "String", required = true)
	private String type;
	
	@ApiModelProperty(value = "明细名称", name = "title", dataType = "String", required = true)
	private String title;
}
