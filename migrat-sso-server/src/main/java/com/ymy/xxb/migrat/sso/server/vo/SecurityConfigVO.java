package com.ymy.xxb.migrat.sso.server.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import com.ymy.xxb.migrat.sso.server.vo.ext.SecurityConfigMechantVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Component
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "security")
@PropertySource("classpath:/production/security.properties")
public class SecurityConfigVO extends SecurityConfigMechantVO implements Serializable{

	private static final long serialVersionUID = -6474077990563871477L;
	// 应用ID
	private String appId;
	
	// 应用秘钥
	private String appSecret;
	
	// 默认的accessToken有效时间
	private int accessTokenValiditySeconds;
	
	// 默认刷新token时间
	private int refreshTokenValiditySeconds;
	
	public Map<String, Map<String,String>> getAppIdAndSecretKeyMaps() {
		Map<String,Map<String,String>> appIdAndSecretKeyMaps = new HashMap<String, Map<String, String>>();
		String appIdAndSecretKey = this.getAppIdAndSecretKey();
		if( !StringUtils.isEmpty(appIdAndSecretKey) ) {
			String[] appIdAndSecretKeyArray = appIdAndSecretKey.split(",");
			for(int i = 0 ; i < appIdAndSecretKeyArray.length ; i ++ ) {
				String  appIdAndSecretKeyArrayItem = appIdAndSecretKeyArray[i];
				String[] appIdAndSecretKeyArrayItemArray = appIdAndSecretKeyArrayItem.split("@");
				if(appIdAndSecretKeyArrayItemArray.length == 2) {
					Map<String,String> secrityDetail = new HashMap<String,String>();
					secrityDetail.put("appSecret", appIdAndSecretKeyArrayItemArray[1]);
					appIdAndSecretKeyMaps.put(appIdAndSecretKeyArrayItemArray[0], secrityDetail);
				}
			}
		}
		return appIdAndSecretKeyMaps;
	}
	
}
