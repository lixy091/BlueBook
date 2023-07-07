package com.lixy.bluebook.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author lixy
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docketConfig(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo(){
        return new ApiInfo(
                "小蓝书Swagger"
                ,"小蓝书的Swagger"
                ,"1.0"
                ,"urn:tos"
                ,new Contact("lixy","","1841859910@qq.com")
                ,"Apache 2.0"
                ,"http://www.apache.org/licenses/LICENSE-2.0"
                ,new ArrayList<>());
    }

}
