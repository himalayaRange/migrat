package com.ymy.xxb.migrat.module.comyany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ymy.xxb.migrat.module.comyany.mapper.StorageMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StorageService {
	@Autowired
	private StorageMapper storageMapper;
	
	void deduct(String commodityCode, int count) {
		log.info("start storage service ...");
		storageMapper.deduct(commodityCode, count);
	}
	
}
