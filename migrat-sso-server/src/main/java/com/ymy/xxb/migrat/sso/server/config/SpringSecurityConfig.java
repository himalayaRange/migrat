package com.ymy.xxb.migrat.sso.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
	@Autowired
	protected SavedRequestAwareAuthenticationSuccessHandler cstAuthenticationSuccessHandler; 
	
	@Autowired
	protected AuthenticationFailureHandler cstAuthenticationFailureHandler;
    
	@Autowired
	private LogoutHandler cstLogoutHandler;
	
	@Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setHideUserNotFoundExceptions(false);
        return authenticationProvider;
    }

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
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .requestMatchers().antMatchers("/oauth/**", "/login","/logout")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(cstAuthenticationSuccessHandler) //成功后跳转自定义方法
        		.failureHandler(cstAuthenticationFailureHandler) //失败后跳转自定义方法
        		.and()
        		.logout()
        		.addLogoutHandler(cstLogoutHandler)
        		.deleteCookies("JSESSIONID")
        		.invalidateHttpSession(true)
                .permitAll()
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

}

