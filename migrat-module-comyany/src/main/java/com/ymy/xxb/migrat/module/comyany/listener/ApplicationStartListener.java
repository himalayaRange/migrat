package com.ymy.xxb.migrat.module.comyany.listener;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApplicationStartListener implements ApplicationListener<WebServerInitializedEvent>{
 
	@Override
	public void onApplicationEvent(final WebServerInitializedEvent event) {
		int port = event.getWebServer().getPort();
		final String host = getHost();
        final String domain = System.getProperty("migrat.httpPath");
        if (StringUtils.isBlank(domain)) {
            Domain.getInstance()
                    .setHttpPath("http://" + String.join(":", host, String.valueOf(port)));
        } else {
            Domain.getInstance()
                    .setHttpPath(domain);
        }
        log.info("------------- START APPLICATION LISTENER -----------------");
	}
	
	private String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "127.0.0.1";
        }
    }
}
