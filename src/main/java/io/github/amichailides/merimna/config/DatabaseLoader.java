package io.github.amichailides.merimna.config;

import io.github.amichailides.merimna.domain.Role;
import io.github.amichailides.merimna.domain.UserRole;
import io.github.amichailides.merimna.user.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;


// TODO(#8): Refactor role seeding to Flyway migration
// - Avoid runtime seeding via CommandLineRunner
// - Ensure idempotent, versioned data initialization
// - Align with infrastructure-as-code (Flyway)
// See Issue #8
@Component
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        Arrays.stream(UserRole.values()).forEach(roleEnum -> {
            if (roleRepository.findByName(roleEnum).isEmpty()) {
                Role newRole = Role.builder()
                        .name(roleEnum)
                        .description("Default system role for " + roleEnum)
                        .build();

                roleRepository.save(newRole);
                System.out.println("generated role in db: " + roleEnum);
            }
        });
    }
}
