package com.ymy.xxb.migrat.module.website.controller.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.website.constant.ModuleConst;
import com.ymy.xxb.migrat.module.website.controller.ext.AliPayApiController;
import com.ymy.xxb.migrat.module.website.payment.alipay.AliPayApi;
import com.ymy.xxb.migrat.module.website.payment.alipay.AliPayApiConfig;
import com.ymy.xxb.migrat.module.website.payment.io.PrintfUtils;
import com.ymy.xxb.migrat.module.website.payment.properties.AlipPayBean;
import com.ymy.xxb.migrat.module.website.payment.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 支付宝支付
 *
 * @author: wangyi
 *
 */
@Controller
@Api(value = "支付宝支付API", tags = "AliPay API")
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.Api.API_MODULE_ALIPAY)
public class AliPayController extends AliPayApiController{
	private static final Logger logger = LoggerFactory.getLogger(AliPayController.class);
	
	/**
	 * 支付宝回调函数前缀
	 */
	private static final String API_PREFIX = Constants.API_OPEN_PREFIX +  ModuleConst.Access.API_MODULE_ALIPAY_CALLBACK;
	
	@Autowired
	private AlipPayBean aliPayBean;
	
	@Override
	public AliPayApiConfig getApiConfig() {
		return AliPayApiConfig.New()
				.setAppId(aliPayBean.getAppId())
				.setAlipayPublicKey(aliPayBean.getPublicKey())
				.setPrivateKey(aliPayBean.getPrivateKey())
				.setServiceUrl(aliPayBean.getServerUrl())
				.setSignType("RSA2")
				.setCharset("UTF-8")
				.build();
	}

	/**
	 * 网站支付
	 * @param response
	 */
	@ResponseBody
	@GetMapping(value = "/pcPay") 
	@DS(value = Constants.DEFAULT_DS)
	@ApiOperation(value = "网站支付" , httpMethod = "GET" , notes = "网站支付", produces = MediaType.TEXT_HTML_VALUE )
	public void pcPay(HttpServletResponse response){
		try {
			String totalAmount = "88.88"; 
			String outTradeNo =StringUtils.getOutTradeNo();
			logger.info("pc outTradeNo > "+outTradeNo);
			
			String returnUrl = aliPayBean.getDomain() + API_PREFIX + "/return_url";
			String notifyUrl = aliPayBean.getDomain() + API_PREFIX + "/notify_url";
			AlipayTradePagePayModel model = new AlipayTradePagePayModel();
			
			model.setOutTradeNo(outTradeNo);
			model.setProductCode("FAST_INSTANT_TRADE_PAY");
			model.setTotalAmount(totalAmount);
			model.setSubject("MIGRAT PC支付测试");
			model.setBody("MIGRAT PC支付测试");
			model.setPassbackParams("passback_params");
			//花呗分期相关的设置
			/**
			 * 测试环境不支持花呗分期的测试
			 * hb_fq_num代表花呗分期数，仅支持传入3、6、12，其他期数暂不支持，传入会报错；
			 * hb_fq_seller_percent代表卖家承担收费比例，商家承担手续费传入100，用户承担手续费传入0，仅支持传入100、0两种，其他比例暂不支持，传入会报错。
			 */
			//	ExtendParams extendParams = new ExtendParams();
			//	extendParams.setHbFqNum("3");
			//	extendParams.setHbFqSellerPercent("0");
			//	model.setExtendParams(extendParams);
			
			AliPayApi.tradePage(response , model , notifyUrl, returnUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * H5 页面支付
	 * @param response
	 */
	@ResponseBody
	@GetMapping(value = "/wapPay") 
	@DS(value = Constants.DEFAULT_DS)
	@ApiOperation(value = "H5 页面支付" , httpMethod = "GET" , notes = "H5 页面支付")
	public void wapPay(HttpServletResponse response) {
		String body = "支付宝H5支付测试数据Body";
		String subject = "Migrat Wap支付测试";
		String totalAmount = "2";
		String passbackParams = "2";
		String returnUrl = aliPayBean.getDomain() + API_PREFIX + "/return_url";
		String notifyUrl = aliPayBean.getDomain() + API_PREFIX + "/notify_url";

		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setBody(body);
		model.setSubject(subject);
		model.setTotalAmount(totalAmount);
		model.setPassbackParams(passbackParams);
		String outTradeNo = StringUtils.getOutTradeNo();
		model.setOutTradeNo(outTradeNo);
		model.setProductCode("QUICK_WAP_PAY");

		try {
			AliPayApi.wapPay(response, model, returnUrl, notifyUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * APP支付
	 * 
	 */
	@ResponseBody
	@GetMapping(value = "/appPay")
	@DS(value = Constants.DEFAULT_DS)
	@ApiOperation(value = "APP支付" , httpMethod = "GET" , notes = "APP支付")
	public SoulResult appPay() {
		try {
			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
			model.setBody("支付宝App支付测试数据");
			model.setSubject("App支付测试-By SINCE1990");
			model.setOutTradeNo(StringUtils.getOutTradeNo());
			model.setTimeoutExpress("30m");
			model.setTotalAmount("0.01");
			model.setPassbackParams("callback params");
			model.setProductCode("QUICK_MSECURITY_PAY");
			String orderInfo = AliPayApi.startAppPay(model, aliPayBean.getDomain() + API_PREFIX + "/notify_url");
			return SoulResult.success(orderInfo);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return SoulResult.error("系统错误:" + e.getMessage());
		}
	}
	
	
	/**
	 * 条形码支付
	 * 
	 */
	@ResponseBody
	@GetMapping(value = "/tradePay")
	@DS(value = Constants.DEFAULT_DS)
	@ApiOperation(value = "条形码支付" , httpMethod = "GET" , notes = "条形码支付")
	public String  tradePay(@RequestParam("auth_code") String authCode) {
		String subject = "支付宝条形码支付测试";
		String totalAmount = "6.5";
		String notifyUrl = aliPayBean.getDomain() + API_PREFIX + "/notify_url";

		AlipayTradePayModel model = new AlipayTradePayModel();
		model.setAuthCode(authCode);
		model.setSubject(subject);
		model.setTotalAmount(totalAmount);
		model.setOutTradeNo(StringUtils.getOutTradeNo());
		model.setScene("bar_code");
		model.setTimeoutExpress("30m");
		try {
			return AliPayApi.tradePay(model,notifyUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	/**
	 * 扫码支付
	 */
	@ResponseBody
	@GetMapping(value ="/tradePrecreatePay")
	@DS(value = Constants.DEFAULT_DS)
	@ApiOperation(value = "扫码支付" , httpMethod = "GET" , notes = "扫码支付")
	public void tradePrecreatePay(HttpServletRequest request , HttpServletResponse response) {
		response.setContentType("UTF-8");
		String subject = "支付宝扫码支付测试";
		String totalAmount = "55";
		// 商家的ID
		String storeId = "2088102176653920";
		String notifyUrl = aliPayBean.getDomain() + API_PREFIX + "/notify_url";

		AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
		model.setSubject(subject);
		model.setTotalAmount(totalAmount);
		model.setStoreId(storeId);
		model.setTimeoutExpress("30m");
		model.setOutTradeNo(StringUtils.getOutTradeNo());
		try {
			String resultStr = AliPayApi.tradePrecreatePay(model, notifyUrl);
			JSONObject jsonObject = JSONObject.parseObject(resultStr);
			String qrCode = jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
			PrintfUtils.WriteQrCodeToOutPutSteam(response.getOutputStream(), qrCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
