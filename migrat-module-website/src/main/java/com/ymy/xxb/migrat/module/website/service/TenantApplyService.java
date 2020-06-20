package com.ymy.xxb.migrat.module.website.service;

import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.common.service.BaseService;
import com.ymy.xxb.migrat.common.vo.BaseVo;
import com.ymy.xxb.migrat.module.website.mapper.TenantApplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TenantApplyService extends BaseService {

    @Autowired
    TenantApplyMapper tenantApplyMapper;

    @Override
    protected <T extends BaseVo> BaseMapper getBaseMapper() {
        return tenantApplyMapper;
    }
}
