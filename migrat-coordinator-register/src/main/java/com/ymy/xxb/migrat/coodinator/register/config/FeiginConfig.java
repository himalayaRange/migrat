package com.ymy.xxb.migrat.coodinator.register.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import com.ymy.xxb.migrat.coodinator.register.filter.FeignBasicAuthRequestInterceptor;
import feign.RequestInterceptor;
import feign.codec.Decoder;

/**
 * Feigin Configuration
 *
 * @author: wangyi
 *
 */
@Configuration
public class FeiginConfig {

	@Bean
	public RequestInterceptor requestInterceptor() {
		return new FeignBasicAuthRequestInterceptor();
	}
	
	@Bean
	public Decoder feignDecoder() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<>();
		supportedMediaTypes.add(MediaType.ALL);
		converter.setSupportedMediaTypes(supportedMediaTypes);
		ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(converter);
		return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
	}
	
}
