package com.ymy.xxb.migrat.module.biz.produce.controller.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Maps;
import com.ymy.xxb.migrat.auth.shiro.entity.DepartmentDO;
import com.ymy.xxb.migrat.auth.shiro.utils.ShiroContextUtil;
import com.ymy.xxb.migrat.common.annotation.ControllerEndpoint;
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.page.PageParamUtil;
import com.ymy.xxb.migrat.common.page.PageUtils;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.common.utils.StringUtil;
import com.ymy.xxb.migrat.module.biz.produce.constant.ModuleConst;
import com.ymy.xxb.migrat.module.biz.produce.entity.GroupRuleDO;
import com.ymy.xxb.migrat.module.biz.produce.entity.GroupRuleDetailDO;
import com.ymy.xxb.migrat.module.biz.produce.service.GroupRuleDetailService;
import com.ymy.xxb.migrat.module.biz.produce.service.GroupRuleService;
import com.ymy.xxb.migrat.module.biz.produce.service.InformTemplateRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 规则管理模块
 *
 *
 */
@RestController
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.Api.API_MODULE_GROUP_RULE)
@Api(value = "GroupRule API", tags = "GroupRule", description = "消息标签控制器")
public class GroupRuleController {

	@Autowired
	private GroupRuleService groupRuleService;
	
	@Autowired
	private GroupRuleDetailService groupRuleDetailService;
	
	@Autowired
	private InformTemplateRuleService informTemplateRuleService;


	/**
	 * 分页查询分组规则模块信息
	 * @param request
	 * @param response
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping(value = "/select")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "模块id", name = "templeId", dataType = "String", paramType = "query"),
			@ApiImplicitParam(value = "当前页数", name = "page", dataType = "Integer", paramType = "query", required = true),
			@ApiImplicitParam(value = "每页显示条数", name = "rows", dataType = "Integer", paramType = "query", required = true)
	})
	@ApiOperation(value = "获取分组规则列表" , httpMethod = "GET"  ,  notes = "根据参数获取分组规则模块列表" , produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> param = PageParamUtil.pageParam(request);
			PageUtils<GroupRuleDO> page = groupRuleService.selectPage(param);
			return SoulResult.success("查询成功", page);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("查询异常 ,ERROR:" + e.getMessage());
		}
	}
	
	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping(value = "/findById")
	@ControllerEndpoint(operation = "查询修改初始化数据" , exceptionMessage = "查询修改初始化数据")

	@ApiOperation(value = "查询修改初始化数据" , httpMethod = "GET"  ,  notes = "查询修改初始化数据" , produces = "application/json;charset=UTF-8")
	public SoulResult findById (HttpServletRequest request, HttpServletResponse response, String id){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try {
			List<GroupRuleDetailDO> dList = new ArrayList<GroupRuleDetailDO>();
			//修改
			GroupRuleDO groupRuleDO = groupRuleService.findById(id);
			//查询对应的规则明细信息
			dList = groupRuleDetailService.findDetaiListByPid(groupRuleDO.getId());
			returnMap.put("groupRuleDO", groupRuleDO);
			returnMap.put("dList", dList);
			return SoulResult.success("初始化成功",returnMap);
		} catch (Exception e) {
			return SoulResult.success("查询成功");
		}
	}
	

	/**
	 * 新增分组规则信息
	 * @param request
	 * @param response
	 * @param groupRuleDO
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@PostMapping(value = "/insert")
	@ControllerEndpoint(operation = "新增分组规则信息" , exceptionMessage = "新增分组规则信息失败")
	@ApiOperation(value = "新增分组规则信息" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "新增分组规则信息")
	public SoulResult insert(HttpServletRequest request, HttpServletResponse response, GroupRuleDO groupRuleDO,List<GroupRuleDetailDO> groupRuleDetailList) {
		try {
			if (groupRuleDO == null
					|| StringUtils.isEmpty(groupRuleDO.getRuleName())
					|| StringUtils.isEmpty(groupRuleDO.getRuleAlias())) {
				return SoulResult.warn("适用模块,标签名称不能为空");
			}
			//校验适用模块,标签名称
			Map<String, Object> param = Maps.newHashMap();
			param.put("ruleName", groupRuleDO.getRuleName());
			param.put("ruleAlias", groupRuleDO.getRuleAlias());
			List<DepartmentDO> list = groupRuleService.select(param);
			if(list != null && list.size() > 0){
				return SoulResult.warn("适用模块,标签名称已存在");
			}
			//保存
			groupRuleDO.setCreateUser(ShiroContextUtil.currentUsername());
			int temp = groupRuleService.insert(groupRuleDO);
			if (temp > 0) {
				if (StringUtil.isNotEmptyList(groupRuleDetailList)) {
					for(GroupRuleDetailDO  dBean : groupRuleDetailList ){
						groupRuleDetailService.insert(dBean);
					}
				}
				return SoulResult.success("新增成功");
			} else {
				return SoulResult.error("新增失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("新增异常,ERROR:" + e.getMessage());
		}
	}

	/**
	 * 修改分组规则信息
	 * @param request
	 * @param response
	 * @param groupRuleDO
	 * @param groupRuleDetailList
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@PostMapping(value = "/update")
	@ControllerEndpoint(operation = "修改分组规则信息" , exceptionMessage = "修改分组规则信息")
	@ApiOperation(value = "修改分组规则信息" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "修改分组规则信息")
	public SoulResult update(HttpServletRequest request, HttpServletResponse response, GroupRuleDO groupRuleDO,List<GroupRuleDetailDO> groupRuleDetailList) {
		try {
			if (groupRuleDO == null
					|| StringUtils.isEmpty(groupRuleDO.getRuleName())
					|| StringUtils.isEmpty(groupRuleDO.getRuleAlias())) {
				return SoulResult.warn("适用模块,标签名称不能为空");
			}
			groupRuleDO.setModifyUser(ShiroContextUtil.currentUsername());
			int temp = groupRuleService.update(groupRuleDO);
			if (temp > 0) {
				if(StringUtil.isNotEmptyList(groupRuleDetailList)){
					groupRuleDetailService.deleteMxByRid(groupRuleDO.getId());
					for(GroupRuleDetailDO dBean : groupRuleDetailList ){
						groupRuleDetailService.insert(dBean);
					}
				}
				return SoulResult.success("修改成功");
			} else {
				return SoulResult.error("修改失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("修改异常,ERROR:" + e.getMessage());
		}
	}


	/**
	 * 按ID删除分组规则
	 * @param id
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@DeleteMapping("/delete")
	@ControllerEndpoint(operation = "删除分组规则" , exceptionMessage = "删除分组规则")
	@ApiOperation(value = "删除分组规则" ,httpMethod = "DELETE"  , notes = "按ID删除分组规则信息")
	public SoulResult delete(
			@ApiParam(name="id", value="分组规则ID",required=true)@RequestParam(value = "id", required = true) String id) {
		try {
			//是否有相关数据使用该规则
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("ruleId", id);
			Long co = informTemplateRuleService.count(param);
			if(co > 0 ){
				return SoulResult.warn("该规则已在信息模板中使用，请先删除相关模板数据！");
			}
			int temp = groupRuleService.logicDelete(id);
			if (temp > 0) {
				//删除相关的规则明细
				groupRuleDetailService.deleteMxByRid(id);
				return SoulResult.success("删除成功");
			} else {
				return SoulResult.error("删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("删除异常,ERROR:" + e.getMessage());
		}
	}
}
