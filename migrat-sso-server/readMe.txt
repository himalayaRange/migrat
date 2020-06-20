客户端调用：
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
package com.ymy.xxb.migrat.sso.server.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * WEB客户端配置
 * @author: wangyi
 *
 */
@Configuration
@EnableOAuth2Sso
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ClientWebSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
     * 让Security 忽略这些url，不做拦截处理
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers
                (
            		"/swagger-ui.html/**", "/webjars/**",
                    "/swagger-resources/**", "/v2/api-docs/**",
                    "/swagger-resources/configuration/ui/**", "/swagger-resources/configuration/security/**",
                    "/images/**","/css/**","/*.ico"
                 );
    }
    
    /**
     * 方法接口请求权限配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .authorizeRequests().antMatchers("/login", "/logout","/swagger").permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().disable();
    }
    
}
------------------------------------------------------------------------------------------------

yml配置：
spring:
  profiles:
    active: dev
   
  main:
    allow-bean-definition-overriding: true
  thymeleaf:
    cache: false
    encoding: utf-8
    enabled: true
    prefix: classpath:/templates/
    suffix: .html
 
mybatis:
   mapper-locations: classpath:/mappers/*.xml  
   type-aliases-package: com.ymy.xxb.migrat.console.entity
   configuration:
     call-setters-on-nulls: true

#客户端应用
security:
   basic:
      enabled: false
   oauth2:
      #客户端应用
      client:
         client-id: client1
         client-secret: clientSecret1
         # 认证服务器地址
         user-authorization-uri: http://127.0.0.1:9001/sso/server/oauth/authorize
         #认证服务器请求令牌地址
         access-token-uri: http://127.0.0.1:9001/sso/server/oauth/token
      #认证服务器获取秘钥
      resource:
         jwt:
            key-uri: http://127.0.0.1:9001/sso/server/oauth/token_key