package io.github.amichailides.merimna.user;

import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.user.dto.UserReadOnlyDTO;
import io.github.amichailides.merimna.user.dto.UserUpdateDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserMapper {

    public UserReadOnlyDTO toReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(
                user.getId(),
                user.getActualUsername(),
                user.getEmail(),
                user.getRole(),
                user.isActive()
        );
    }

    public void updateEntity(User existing, UserUpdateDTO dto) {
        Objects.requireNonNull(existing, "existing user must not be null");
        Objects.requireNonNull(dto, "user update dto must not be null");

        if (dto.email() != null) existing.setEmail(dto.email());
    }
}
