package com.ymy.xxb.migrat.module.comyany.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.ymy.xxb.migrat.module.comyany.entity.DictionaryMainDO;

@Data
public class DictionaryMainTree<T> implements Serializable {

    private static final long serialVersionUID = -6037803746409887975L;
    private String id;
    private String title;
    private Map<String, Object> state;
    private boolean checked = false;
    private Map<String, Object> attributes;
    private List<DictionaryMainTree<T>> childs = new ArrayList<>();
    private String parentId;
    private boolean hasParent = false;
    private boolean hasChild = false;
    private String type; // 1.字典分类表  2.分类明细表

    private DictionaryMainDO data;

}