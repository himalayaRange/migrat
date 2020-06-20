/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.ymy.xxb.migrat.module.website.controller.open;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.website.constant.ModuleConst;
import com.ymy.xxb.migrat.module.website.payment.alipay.AliPayApi;
import com.ymy.xxb.migrat.module.website.payment.properties.AlipPayBean;
import lombok.extern.slf4j.Slf4j;

/**
 *  支付宝回调
 * 
 * @author: wangyi
 *
 */
@Slf4j
@Controller
@RequestMapping(Constants.API_OPEN_PREFIX +  ModuleConst.Access.API_MODULE_ALIPAY_CALLBACK)
public class AliPayCallbackController {
	@Autowired
	private AlipPayBean aliPayBean;
	
	/**
	 * 支付体验页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/payment")
	public String payment(HttpServletRequest request) {
		
		return "payment";
	}
	
	/**
	 * 处理成功返回url
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/return_url")
	public SoulResult return_url(HttpServletRequest request) {
		try {
			// 获取支付宝GET过来反馈信息
			Map<String, String> map = AliPayApi.toMap(request);
			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}

			boolean verify_result = AlipaySignature.rsaCheckV1(map, aliPayBean.getPublicKey(), "UTF-8",	"RSA2");

			if (verify_result) {// 验证成功
				// TODO 请在这里加上商户的业务逻辑程序代码
				log.info("return_url 验证成功");

				return SoulResult.success("支付成功");
			} else {
				System.out.println("return_url 验证失败");
				// TODO 请在这里加上商户的业务逻辑程序异常代码
				return SoulResult.error("回调函数return_url 验证失败!");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return SoulResult.error("系统异常，" + e.getMessage());
		}
	}
	
	/**
	 * 异步通知url
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/notify_url")
	public String  notify_url(HttpServletRequest request) {
		try {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = AliPayApi.toMap(request);

			for (Map.Entry<String, String> entry : params.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}

			boolean verify_result = AlipaySignature.rsaCheckV1(params, aliPayBean.getPublicKey(), "UTF-8","RSA2");

			if (verify_result) {// 验证成功
				// TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
				log.info("notify_url 验证成功succcess");
				return "success";
			} else {
				log.info("notify_url 验证失败");
				return "failure";
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return "failure";
		}
	}
	
}
