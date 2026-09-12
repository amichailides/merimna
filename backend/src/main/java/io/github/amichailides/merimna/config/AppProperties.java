package io.github.amichailides.merimna.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Mail mail = new Mail();
    private Frontend frontend = new Frontend();
    private Cors cors = new Cors();

    @Getter
    @Setter
    public static class Mail {

        @NotBlank
        private String from;

        private String apiToken;
    }

    @Getter
    @Setter
    public static class Frontend {

        @NotBlank
        private String resetPasswordUrl;

        @NotBlank
        private String acceptInvitationUrl;
    }

    @Getter
    @Setter
    public static class Cors {

        @NotEmpty
        private List<String> allowedOrigins = new ArrayList<>();
    }
}