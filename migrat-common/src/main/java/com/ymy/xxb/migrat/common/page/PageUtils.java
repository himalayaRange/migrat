package com.ymy.xxb.migrat.common.page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * PageUtils 
 * @author : wangyi
 */
public class PageUtils<T> implements Serializable{

	private static final long serialVersionUID = 6189910918051117922L;

	private final static int PAGE_SIZE = 10; // 默认显示的记录数

	private List<T> data;// 记录
	private Long total;// 总记录数

	private int nowpage;// 当前页
	private int pagesize;// 页面记录大小

	private int rowStart;
	private int rowEnd;

	private String order = "desc";

	private String sort = "id";

	private Map<String, Object> condition; // 查询条件

	public PageUtils() {
	}

	public PageUtils(int nowpage, int pagesize) {
		// 计算当前页
		if (nowpage < 0) {
			this.nowpage = 1;
		} else {
			// 当前页
			this.nowpage = nowpage;
		}
		// 记录每页显示的记录数
		if (pagesize <= 0) {
			this.pagesize = PAGE_SIZE;
		} else {
			this.pagesize = pagesize;
		}
		// 计算开始的记录和结束的记录
		this.rowStart = (this.nowpage - 1) * this.pagesize;
		this.rowEnd = this.pagesize;
	}

	public PageUtils(int nowpage, int pagesize, String sort, String order) {
		this(nowpage, pagesize);
		// 排序字段，正序还是反序
		this.sort = sort;
		this.order = order;
	}

	public PageUtils(List<T> data, Long total) {
		super();
		this.data = data;
		this.total = total;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public int getNowpage() {
		return nowpage;
	}

	public void setNowpage(int nowpage) {
		this.nowpage = nowpage;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getRowStart() {
		return rowStart;
	}

	public void setRowStart(int rowStart) {
		this.rowStart = rowStart;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Map<String, Object> getCondition() {
		return condition;
	}

	public void setCondition(Map<String, Object> condition) {
		this.condition = condition;
	}
}
