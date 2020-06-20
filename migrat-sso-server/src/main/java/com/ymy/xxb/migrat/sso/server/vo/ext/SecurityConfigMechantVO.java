package com.ymy.xxb.migrat.sso.server.vo.ext;

import java.io.Serializable;
import lombok.Data;

@Data
public class SecurityConfigMechantVO implements Serializable{
	private static final long serialVersionUID = -6481443051340213303L;
	
	private String appIdAndSecretKey;

}
