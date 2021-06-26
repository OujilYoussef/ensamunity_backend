package com.project.ensamunity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket EnsamunityApi(){
return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Ensamunity API")
                .version("1.0")
                .description("API for Ensamunity Application")
                .contact(new Contact("Oujil Youssef",
                        "https://www.linkedin.com/in/youssef-oujil-7960821a6" ,
                        "youssef.oujil@usmba.ac.ma"))
                .license("Apache License Version 2.0")
                .build();

    }
}
