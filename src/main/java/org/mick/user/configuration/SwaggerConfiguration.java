package org.mick.user.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("org.mick.user.controller"))
                .paths(PathSelectors.any())
                .build().pathMapping("/") ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("User")
                .description("")
                .termsOfServiceUrl("http://localhost:8080")
                .contact(new Contact("Michael Ayerdis", "","mick_ayerdis16@outlook.com"))
                .license("License")
                .licenseUrl("mick_ayerdis16@outlook.com").version("1.0").build();
    }

}
