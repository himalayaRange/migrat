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
 * 新闻中心表
 */

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PressCenterDO extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * 新闻标题
     */
    @ApiModelProperty(value = "新闻标题", name = "title", dataType = "String", required = true)
    private String title;
    
    /**
     * 新闻作者
     */
    @ApiModelProperty(value = "新闻作者", name = "author", dataType = "String")
    private String author;
    
    /**
     * 新闻内容
     */
    @ApiModelProperty(value = "新闻内容", name = "content", dataType = "String",required = true)
    private String content;
    
    /**
     * 图片描述
     */
    @ApiModelProperty(value = "图片描述", name = "pictureDesc", dataType = "String")
    private String pictureDesc;
   
    /**
     * 图片路径
     */
    @ApiModelProperty(value = "图片路径", name = "pictureUrl", dataType = "String")
    private String pictureUrl;
    
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态 1:启用 0:禁用", name = "state", dataType = "Byte",required = true)
    private Byte state;


}
