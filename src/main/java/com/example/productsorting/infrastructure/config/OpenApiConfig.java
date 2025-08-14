package com.example.productsorting.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productSortingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Sorting API")
                        .description("API for sorting products based on sales and stock criteria")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Jose Luis Moreno Rodriguez")
                                .email("jlmorenorodriguez@gmail.com")
                                .url("https://example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development server"),
                        new Server()
                                .url("https://api.example.com")
                                .description("Production server (fictional)")
                ));
    }
}