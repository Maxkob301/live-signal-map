package com.signalmap.liveSignalMap.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI liveSignalMapOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LiveSignalMap API")
                        .description("Backend for Wi-Fi and mobile signal heatmap in university buildings")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Maxim")
                                .email("k0betsm@yandex.ru")));
    }
}