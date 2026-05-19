package io.github.amichailides.merimna.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Mail mail = new Mail();
    private Frontend frontend = new Frontend();

    @Getter
    @Setter
    public static class Mail {
        private String from;
    }

    @Getter
    @Setter
    public static class Frontend {
        private String resetPasswordUrl;
    }
}