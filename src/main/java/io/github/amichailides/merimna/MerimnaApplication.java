package io.github.amichailides.merimna;

import io.github.amichailides.merimna.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class MerimnaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MerimnaApplication.class, args);
    }

}
