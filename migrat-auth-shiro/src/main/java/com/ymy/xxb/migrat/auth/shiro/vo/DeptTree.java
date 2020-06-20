package com.ymy.xxb.migrat.auth.shiro.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.ymy.xxb.migrat.auth.shiro.entity.DepartmentDO;

/**
 * @author wangyi
 */
@Data
public class DeptTree<T> implements Serializable {

    private static final long serialVersionUID = 7681873362531265829L;

    private String id;
    private String icon;
    private String href;
    private String title;
    private Map<String, Object> state;
    private boolean checked = false;
    private Map<String, Object> attributes;
    private List<DeptTree<T>> childs = new ArrayList<>();
    private String parentId;
    private boolean hasParent = false;
    private boolean hasChild = false;

    private DepartmentDO data;

}