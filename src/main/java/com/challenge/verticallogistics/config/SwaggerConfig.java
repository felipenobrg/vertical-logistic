package com.challenge.verticallogistics.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI verticallogisticsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vertical Logistics API")
                        .description("Documentação da API de Processamento de Pedidos")
                        .version("0.0.1"));
    }
}