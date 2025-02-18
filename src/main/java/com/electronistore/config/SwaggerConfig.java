package com.electronistore.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Electronic-Store",
                version = "1.0.0v",
                description = "This is electronic backend project and created documentation",
                termsOfService = "",
                contact = @Contact(
                    name = "Amit Kumar Patel",
                        email = "amitrrrvaa0003@gmail.com",
                        url = "https://www.linkedin.com/in/amit-kumar-patel-033b1a202?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app"
                )

        ),
        servers = {
        @Server(url = "http://localhost:3700", description = "Local Server")
}
)
public class SwaggerConfig {


    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi userApi(){
        return GroupedOpenApi.builder()
                .group("User API")
                .pathsToMatch("/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi productApi(){
        return GroupedOpenApi.builder()
                .group("Product API")
                .pathsToMatch("/products/**")
                .build();
    }

    @Bean
    public GroupedOpenApi orderApi(){
        return GroupedOpenApi.builder()
                .group("Order API")
                .pathsToMatch("/orders/**")
                .build();
    }

    @Bean
    public GroupedOpenApi categoryApi(){
        return GroupedOpenApi.builder()
                .group("Category API")
                .pathsToMatch("/categories/**")
                .build();
    }

    @Bean
    public GroupedOpenApi cartApi(){
        return GroupedOpenApi.builder()
                .group("Cart API")
                .pathsToMatch("/carts/**")
                .build();
    }

    @Bean
    public  GroupedOpenApi authApi(){
        return GroupedOpenApi.builder()
                .group("Auth API")
                .pathsToMatch("/auth/**")  // this will show
                .pathsToExclude("/auth/login-with-google")   // we excluded means hide this endpoint API from Swagger
                .build();
    }



}
