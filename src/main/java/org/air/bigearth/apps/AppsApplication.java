package org.air.bigearth.apps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SpringBoot启动入口类
 *  不添加(exclude = {MultipartAutoConfiguration.class}),则ajax上传文件formData为空,通过request获取
 *  添加(exclude = {MultipartAutoConfiguration.class}),则ajax上传文件formData不为空,通过request获取ServletFileUpload获取
 *
 * @author wangxuming
 * @version 1.0
 * @date 2018-12-17
 */
@SpringBootApplication //(exclude = {MultipartAutoConfiguration.class})
@EnableSwagger2 //启动swagger注解
@ServletComponentScan(basePackages = {"org.air.bigearth.apps"})
public class AppsApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppsApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AppsApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... strings) throws Exception {
        logger.info("程序启动");
    }


}
