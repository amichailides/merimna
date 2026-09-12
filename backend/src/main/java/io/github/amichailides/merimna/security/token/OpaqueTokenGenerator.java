package io.github.amichailides.merimna.security.token;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.Base64;


@Component
public class OpaqueTokenGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public String generate() {
        byte[] randomBytes = new byte[64];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }
}

