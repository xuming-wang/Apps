package org.air.bigearth.apps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Spring RestTemplate配置类，Spring注入方式使用
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-25
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 设置从主机读取数据超时（单位：毫秒）
        factory.setReadTimeout(40000);
        // 设置连接主机超时（单位：毫秒）
        factory.setConnectTimeout(15000);
        return factory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }
}
