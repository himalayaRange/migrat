package com.ymy.xxb.migrat.sso.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import com.ymy.xxb.migrat.sso.server.constant.Const;
import com.ymy.xxb.migrat.sso.server.vo.SecurityConfigVO;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private SecurityConfigVO securityConfigVO;
	
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	//log.info("---------- 开始对客户端进行授权 -----------------");
		InMemoryClientDetailsServiceBuilder inMemory = clients.inMemory();
		ClientDetailsServiceBuilder<InMemoryClientDetailsServiceBuilder>.ClientBuilder scopes = null;
		Map<String, Map<String, String>> scKeys = securityConfigVO.getAppIdAndSecretKeyMaps();
		Iterator<Entry<String, Map<String, String>>> iterator = scKeys.entrySet().iterator();
		int count = 0;
		while(iterator.hasNext()) {
			count ++;
			Entry<String, Map<String, String>> entry = iterator.next();
			String appId = entry.getKey();
			String appSecret= entry.getValue().get("appSecret");
			if (count > 1) {
				scopes = scopes
					.and()
					.withClient(appId)
					.secret(new BCryptPasswordEncoder().encode(appSecret))
					.authorizedGrantTypes("authorization_code", "refresh_token")
					.scopes("read" , "write")
					.autoApprove(true);
			} else {
				scopes = inMemory
					.withClient(appId)
					.secret(new BCryptPasswordEncoder().encode(appSecret))
					.authorizedGrantTypes("authorization_code", "refresh_token")
					.scopes("read" , "write")
					.autoApprove(true);
			}
		}
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.tokenStore(jwtTokenStore()).accessTokenConverter(jwtAccessTokenConverter());
        DefaultTokenServices tokenServices = (DefaultTokenServices) endpoints.getDefaultAuthorizationServerTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        tokenServices.setAccessTokenValiditySeconds(securityConfigVO.getAccessTokenValiditySeconds());
        tokenServices.setRefreshTokenValiditySeconds(securityConfigVO.getRefreshTokenValiditySeconds());
        endpoints.tokenServices(tokenServices);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("isAuthenticated()");
    }

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(Const.SSO_JWT_SIGN_KEY);
        return converter;
    }

}