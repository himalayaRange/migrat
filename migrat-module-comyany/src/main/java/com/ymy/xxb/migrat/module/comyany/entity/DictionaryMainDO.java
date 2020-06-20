package com.ymy.xxb.migrat.module.comyany.entity;

import java.io.Serializable;
import java.util.List;
import com.ymy.xxb.migrat.common.vo.BaseVo;
import com.ymy.xxb.migrat.module.comyany.vo.DictionaryDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 字典类别表
 * 
 * @author yl
 * 
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DictionaryMainDO extends BaseVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 字典主表父类ID,跟目录ID为0
	 */
	@ApiModelProperty(value = "字典主表父类ID,跟目录ID为0", name = "parentId", dataType = "String", required = true)
    private String parentId;
	
    /**
     * 类别编码
     */
	@ApiModelProperty(value = "类别编码", name = "categoryCode", dataType = "String", required = true)
    private String categoryCode;

    /**
     * 类别名称
     */
	@ApiModelProperty(value = "类别名称", name = "categoryName", dataType = "String", required = true)
    private String categoryName;

    /**
     * 备注
     */
	@ApiModelProperty(value = "备注", name = "memo", dataType = "String")
    private String memo;

    /**
     * 启用状态：1 启用 0 禁用
     */
	@ApiModelProperty(value = "启用状态：1 启用 0 禁用", name = "state", dataType = "Byte", required = true)
    private Byte state;

	/**
	 * 字典明细表
	 */
	@ApiModelProperty(value = "字典明细list", name = "list", dataType = "List", hidden = true)
	private List<DictionaryDetailVO> list;
}