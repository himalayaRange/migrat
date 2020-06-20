package com.ymy.xxb.migrat.module.website.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.module.website.constant.ModuleConst;
import com.ymy.xxb.migrat.module.website.controller.ext.WxPayApiController;
import com.ymy.xxb.migrat.module.website.payment.properties.WxPayBean;
import com.ymy.xxb.migrat.module.website.payment.wxpay.WxPayApiConfig;
import com.ymy.xxb.migrat.module.website.payment.wxpay.WxPayApiConfig.PayModel;
import io.swagger.annotations.Api;

@Controller
@Api(value = "微信支付API", tags = "WxPay")
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.Api.API_MODULE_WXPAY)
public class WxPayController extends WxPayApiController{
	
	@Autowired
	private WxPayBean wxPayBean;
	
	private String notify_url;
	
	/**
	 * PayModel.BUSINESSMODEL 商户模式
	 * PayModel.SERVICEMODE 服务商模式
	 */
	@Override
	public WxPayApiConfig getApiConfig() {
		notify_url = wxPayBean.getDomain().concat("/wxpay/pay_notify");
		
		return WxPayApiConfig.New()
				.setAppId(wxPayBean.getAppId())
				.setMchId(wxPayBean.getMchId())
				.setPaternerKey(wxPayBean.getPartnerKey())
				.setPayModel(PayModel.BUSINESSMODEL);
	}
	
}
