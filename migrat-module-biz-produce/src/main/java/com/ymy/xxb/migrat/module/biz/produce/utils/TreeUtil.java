package com.ymy.xxb.migrat.module.biz.produce.utils;

import com.ymy.xxb.migrat.module.biz.produce.vo.BasicModuleTree;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeUtil {

	protected TreeUtil() {

	}

	/**
	 * 构建基础模块配置树
	 * 
	 * @param nodes
	 * @return
	 */
	public static <T> BasicModuleTree<T> buildBasicModuleTree(List<BasicModuleTree<T>> nodes) {
		if (nodes == null) {
			return null;
		}
		List<BasicModuleTree<T>> topNodes = new ArrayList<>();
		nodes.forEach(children -> {
			String pid = children.getParentId();
			if (pid == null || "0".equals(pid.toUpperCase())) {
				topNodes.add(children);
				return;
			}
			for (BasicModuleTree<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChilds().add(children);
					children.setHasParent(true);
					parent.setHasChild(true);
					return;
				}
			}
		});

		BasicModuleTree<T> root = new BasicModuleTree<>();
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

}