package io.github.amichailides.merimna.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@ConfigurationProperties(prefix = "security")
@Component
@EnableConfigurationProperties
@Getter
@Setter
public class SecurityProperties {

    private String secret;
    private AccessToken accessToken = new AccessToken();
    private RefreshToken refreshToken = new RefreshToken();

    @Getter
    @Setter
    public static class AccessToken {
        private Duration expiration;
    }

    @Getter
    @Setter
    public static class RefreshToken {
        private Duration expiration;
        private boolean secureCookie = true; // για application-dev
    }
}
