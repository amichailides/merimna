package io.github.amichailides.merimna.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
@Profile("mailtrap-api")
public class MailtrapApiConfig {

    @Bean
    public RestClient mailtrapRestClient(AppProperties appProperties) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        JdkClientHttpRequestFactory requestFactory =
                new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        return RestClient.builder()
                .baseUrl("https://send.api.mailtrap.io")
                .requestFactory(requestFactory)
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + appProperties.getMail().getApiToken()
                )
                .build();
    }
}