package com.ymy.xxb.migrat.module.comyany.controller;

//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import com.ymy.xxb.migrat.common.constant.Constants;
//import com.ymy.xxb.migrat.common.result.SoulResult;
//import com.ymy.xxb.migrat.module.comyany.constant.ModuleConst;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;

/**
 * 
 * redisson client server
 *
 * @author: wangyi
 *
 */
// @Slf4j
// @RestController
// @RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.API.API_MODULE_REDISSION)
// @Api(value = "RedissonController API", tags = "redisson", description = "redission客户端服务")
public class RedssionController {
	
//	@Autowired
//	private RedissonClient redissonClient;
	
	/**
	 * 用户通过客户端自行处理分布式锁业务
	 * 
	 * @param lockKey
	 * @return
	 */
//	@PostMapping(value = "/lockBiz")
//	@ApiOperation(value = "分布式锁业务操作" , httpMethod = "POST"  ,  notes = "支持重入锁，线程安全，分布式业务锁操作" , produces = "application/json;charset=UTF-8")
//	public SoulResult lockBiz(
//			@RequestParam(value = "lockKey", required = true) String lockKey
//			) {
//		RLock lock = redissonClient.getLock(lockKey);
//		try {
//			// 加锁，类似JAVA中ReentrantLock机制
//			lock.lock();
//			
//			// BIZ 处理, 例如秒杀场景
//			if (log.isInfoEnabled()) {
//				log.info("Handler Success...");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return SoulResult.error(String.format("BIZ处理失败, ERROR Detail: %s", e.getMessage()));
//		} finally {
//			lock.unlock();
//		}
//		
//		return SoulResult.success("BIZ处理成功！");
//	}
	
}
