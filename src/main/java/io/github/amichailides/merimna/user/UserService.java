package io.github.amichailides.merimna.user;

import io.github.amichailides.merimna.user.dto.UserCreateDTO;
import io.github.amichailides.merimna.user.dto.UserReadOnlyDTO;
import io.github.amichailides.merimna.user.dto.UserSearchDTO;
import io.github.amichailides.merimna.user.dto.UserUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserReadOnlyDTO create(UserCreateDTO dto);

    Page<UserReadOnlyDTO> getAllUsers(UserSearchDTO criteria, Pageable pageable);

    UserReadOnlyDTO getUserById(Long id);

    UserReadOnlyDTO updateUser(Long id, UserUpdateDTO dto);
}
