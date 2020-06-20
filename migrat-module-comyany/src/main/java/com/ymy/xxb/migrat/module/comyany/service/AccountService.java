package com.ymy.xxb.migrat.module.comyany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ymy.xxb.migrat.module.comyany.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService {
	@Autowired
	private AccountMapper accountMapper;
	
	void debit(String userId, int money) {
		log.info("start account service ...");
		accountMapper.debit(userId, money);
	}
}
