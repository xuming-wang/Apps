package org.air.bigearth.apps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 前后端分离项目，通过cors协议跨域访问
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-20
 */
@Configuration
public class CorsConfig {

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1.允许任何域名使用
        corsConfiguration.addAllowedOrigin("*");
        // 2.允许任何头
        corsConfiguration.addAllowedHeader("*");
        // 3.允许任何方法（post、get等）
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 4.
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

}
