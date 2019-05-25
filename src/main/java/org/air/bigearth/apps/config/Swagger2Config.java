package org.air.bigearth.apps.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2 配置
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    public static final String SWAGGER_SCAN_BASE_PACKAGE = "org.air.bigearth.apps";
    public static final String VERSION = "1.0.0";

    /**
     * 是否开启swagger，正式环境一般是需要关闭的，可根据springboot的多环境配置进行设置
     */
    @Value(value = "${swagger.enabled}")
    Boolean swaggerEnabled;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            // 是否开启
            .enable(swaggerEnabled)
            .select()
            // api接口包扫描路径
            .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
            // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
            .paths(PathSelectors.any())
            .build();
    }

    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 设置文档的标题
                .title("Swagger2 接口文档")
                 // 设置文档的描述
                .description("")
                // 设置文档的License信息
                .termsOfServiceUrl("")
                // 设置文档的联系方式
                .contact(new Contact("Apps", "", ""))
                .version(VERSION)
                .build();
    }
}