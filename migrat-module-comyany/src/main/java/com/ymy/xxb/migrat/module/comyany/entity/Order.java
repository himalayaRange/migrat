package com.ymy.xxb.migrat.module.comyany.entity;

import lombok.Data;

@Data
public class Order {

	private String userId;
	
	private String commodityCode ;
	
	private int count;
	
	private int money;
}
