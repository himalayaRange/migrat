package com.ymy.xxb.migrat.zuul.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
	
	// 是否开启swagger2，开发环境开启，正式环境关闭
	@Value(value = "${swagger.open}")
	private Boolean open; 
	
	@Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("路由网关(Zuul)：利用swagger2聚合 MIGRAT API文档")
                .description("migrat zuul swagger2 api")
                .termsOfServiceUrl("")
                .contact(new Contact("wangyi","13127636621@163.com","13127636621@163.com"))
                .version("1.0")
                .build();
    }
	
}
