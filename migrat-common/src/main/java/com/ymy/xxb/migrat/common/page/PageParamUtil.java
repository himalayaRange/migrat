package com.ymy.xxb.migrat.common.page;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * 分页参数工具类
 * 
 * @author : wangyi
 * 
 */
public class PageParamUtil {
	public static Integer PAGE = 0;// 默认分页
	public static Integer ROWS = 10;// 默认每页大小
	public static String SORT = "id";// 默认排序字段
	public static String ORDER = "DESC";// 默认排序顺序

	/**
	 * 组织mysql排序参数。含分页，含排序
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, Object> pageParam(HttpServletRequest request) {

		Map<String, Object> param = new HashMap<String, Object>();
		Integer page = StringUtils.isNotEmpty(request.getParameter("page"))
				? Integer.valueOf(request.getParameter("page")) : PAGE;
		Integer rows = StringUtils.isNotEmpty(request.getParameter("rows"))
				? Integer.valueOf(request.getParameter("rows")) : ROWS;
		page = page > 0 ? page : 0;
		Integer rowStart = 0;
		Integer rowEnd = rows;
		if (page > 0) {
			rowStart = (page - 1) * rows;
		}

		param.put("rowStart", rowStart);
		param.put("rowEnd", rowEnd);

		String sort = StringUtils.isNotEmpty(request.getParameter("sort")) ? request.getParameter("sort") : SORT;
		param.put("sort", sort);
		String order = StringUtils.isNotEmpty(request.getParameter("order")) ? request.getParameter("order") : ORDER;
		param.put("order", order);
		@SuppressWarnings("unchecked")
		Enumeration<String> ks = request.getParameterNames();
		while (ks.hasMoreElements()) {
			String key = ks.nextElement();
			String value = request.getParameter(key);
			param.put(key, value);
		}
		return param;
	}

	/**
	 * 封装分页参数
	 * 
	 * @param request
	 * @param map
	 */
	public static void pageParam(HttpServletRequest request, Map<String, Object> map) {
		Integer page = StringUtils.isNotEmpty(request.getParameter("page"))
				? Integer.valueOf(request.getParameter("page")) : PAGE;
		Integer rows = StringUtils.isNotEmpty(request.getParameter("rows"))
				? Integer.valueOf(request.getParameter("rows")) : ROWS;
		page = page > 0 ? page : 0;
		Integer rowStart = 0;
		Integer rowEnd = rows;
		if (page > 0) {
			rowStart = (page - 1) * rows;
		}

		map.put("rowStart", rowStart);
		map.put("rowEnd", rowEnd);

		String sort = StringUtils.isNotEmpty(request.getParameter("sort")) ? request.getParameter("sort") : SORT;
		map.put("sort", sort);
		String order = StringUtils.isNotEmpty(request.getParameter("order")) ? request.getParameter("order") : ORDER;
		map.put("order", order);
	}
}
