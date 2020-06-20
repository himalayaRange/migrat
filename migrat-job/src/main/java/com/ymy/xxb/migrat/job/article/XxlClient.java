package com.ymy.xxb.migrat.job.article;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.ymy.xxb.migrat.coodinator.register.config.FeiginConfig;
import com.ymy.xxb.migrat.coodinator.register.constant.Const;

@FeignClient(name = Const.SERVICE_ID_TENANT, configuration = FeiginConfig.class)
public interface XxlClient {
	
	@GetMapping("/openApi/sso/job/xxl")
	public void execute();
}
