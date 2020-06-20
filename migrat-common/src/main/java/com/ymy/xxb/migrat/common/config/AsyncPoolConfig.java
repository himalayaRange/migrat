package com.ymy.xxb.migrat.common.config;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import com.ymy.xxb.migrat.common.constant.Constants;
/**
 * AsyncPool Config
 * @author wangyi
 *
 */
@Configuration
public class AsyncPoolConfig {

	@Bean(Constants.ASYNC_POOL)
	public ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(200);
		executor.setKeepAliveSeconds(30);
		executor.setThreadNamePrefix("Migrat-Async-Thread");
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(60);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}
}
