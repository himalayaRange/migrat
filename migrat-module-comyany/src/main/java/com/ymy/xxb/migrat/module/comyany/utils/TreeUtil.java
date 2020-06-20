package com.ymy.xxb.migrat.module.comyany.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ymy.xxb.migrat.module.comyany.vo.DictionaryMainTree;

public class TreeUtil {

    protected TreeUtil() {

    }

    public static <T> DictionaryMainTree<T> buildDictionaryMainTree(List<DictionaryMainTree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<DictionaryMainTree<T>> topNodes = new ArrayList<>();
        nodes.forEach(children -> {
            String pid = children.getParentId();
            if (pid == null || "0".equals(pid.toUpperCase())) {
                topNodes.add(children);
                return;
            }
            for (DictionaryMainTree<T> parent : nodes) {
                String id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getChilds().add(children);
                    children.setHasParent(true);
                    parent.setHasChild(true);
                    return;
                }
            }
        });

        DictionaryMainTree<T> root = new DictionaryMainTree<>();
        root.setId("0");
        root.setParentId("");
        root.setHasParent(false);
        root.setHasChild(true);
        root.setChecked(true);
        root.setChilds(topNodes);
        Map<String, Object> state = new HashMap<>(16);
        root.setState(state);
        return root;
    }
    

    public static <T> List<DictionaryMainTree<T>> buildList(List<DictionaryMainTree<T>> nodes, String idParam) {
        if (nodes == null) {
            return new ArrayList<>();
        }
        List<DictionaryMainTree<T>> topNodes = new ArrayList<>();
        nodes.forEach(children -> {
            String pid = children.getParentId();
            if (pid == null || idParam.equals(pid)) {
                topNodes.add(children);
                return;
            }
            nodes.forEach(parent -> {
                String id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getChilds().add(children);
                    children.setHasParent(true);
                    parent.setHasChild(true);
                }
            });
        });
        return topNodes;
    }
    
}