package com.ymy.xxb.migrat.module.comyany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ymy.xxb.migrat.module.comyany.entity.Order;
import com.ymy.xxb.migrat.module.comyany.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService {
	@Autowired
	private AccountService accountService;
	@Autowired
	private OrderMapper orderMapper;
	
	Order create(String userId, String commodityCode, int count) {
		log.info("start order service ...");
		// 计算商品总金额
		int orderMoney = 100;

		// 账户扣除金额
		accountService.debit(userId, orderMoney);
		
		// 插入订单
		Order order = new Order();
		order.setUserId(userId);
		order.setCommodityCode(commodityCode);
		order.setCount(count);
		order.setMoney(orderMoney);
		
		return orderMapper.insertOrder(order);
	}
}
