package io.github.amichailides.merimna.user;

import io.github.amichailides.merimna.user.dto.UserCreateDTO;
import io.github.amichailides.merimna.user.dto.UserReadOnlyDTO;

public interface UserService {
    UserReadOnlyDTO create(UserCreateDTO dto);
}
