package com.ymy.xxb.migrat.module.biz.produce.mapper;

import com.ymy.xxb.migrat.auth.shiro.entity.BsRoleDO;
import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.module.biz.produce.entity.GroupRuleDO;
import com.ymy.xxb.migrat.module.biz.produce.entity.InformTemplateDO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface MessageSendMapper extends BaseMapper{

    List<GroupRuleDO> getMessageSend(Map<String, Object> param);
    List<GroupRuleDO> selectGroupRule(Map<String, Object> param);
    List<InformTemplateDO> selectInformTemplate(Map<String, Object> param);
    BsRoleDO selectByRoleName(@Param("roleName") String roleName);
    GroupRuleDO selectByRuleName(String ruleName);
    InformTemplateDO selectByTemplateTitle(String templateTitle);
}
