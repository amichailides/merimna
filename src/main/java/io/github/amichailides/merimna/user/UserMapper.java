package io.github.amichailides.merimna.user;

import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.user.dto.UserReadOnlyDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserReadOnlyDTO toReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isActive()
        );
    }
}
