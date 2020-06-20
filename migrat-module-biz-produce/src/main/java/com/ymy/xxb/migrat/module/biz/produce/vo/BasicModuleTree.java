package com.ymy.xxb.migrat.module.biz.produce.vo;

import com.ymy.xxb.migrat.module.biz.produce.entity.BasicModuleDO;
import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangyi
 */
@Data
public class BasicModuleTree<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String icon;
    private String href;
    private String title;
    private Map<String, Object> state;
    private boolean checked = false;
    private Map<String, Object> attributes;
    private List<BasicModuleTree<T>> childs = new ArrayList<>();
    private String parentId;
    private boolean hasParent = false;
    private boolean hasChild = false;

    private BasicModuleDO data;

}