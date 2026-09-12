package io.github.amichailides.merimna.user;

import io.github.amichailides.merimna.user.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserReadOnlyDTO create(UserCreateDTO dto);

    Page<UserReadOnlyDTO> getAllUsers(UserSearchDTO criteria, Pageable pageable);

    UserReadOnlyDTO getUserByPublicId(UUID publicId);

    UserReadOnlyDTO updateUser(UUID publicId, UserUpdateDTO dto);

    void changePassword(String email, ChangePasswordDTO dto);

    UserReadOnlyDTO getByEmail(String email);
}
