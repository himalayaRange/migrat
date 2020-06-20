package com.ymy.xxb.migrat.module.comyany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BusinessService {
	@Autowired
	private StorageService storageService;
	@Autowired
	private OrderService orderService;
	
	/**
	 * 购买
	 * @param userId
	 * @param commodityCode
	 * @param orderCount
	 */
	@GlobalTransactional
	public void purchase(String userId, String commodityCode, int orderCount) {
		log.info("start purchase service ...");
		
		storageService.deduct(commodityCode, orderCount);
		
		orderService.create(userId, commodityCode, orderCount);
	}
}
