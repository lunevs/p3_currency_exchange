package ru.skillbox.currency.exchange.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    public WebClient cbrLoaderWebClient() {
        return WebClient.builder()
                .defaultHeader("User-Agent", "Mozilla/5.0")
                .defaultHeader("Accept", "application/xml")
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .build();
    }


}
