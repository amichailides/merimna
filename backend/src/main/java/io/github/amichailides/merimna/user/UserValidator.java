package io.github.amichailides.merimna.user;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.exception.ConflictValidationException;
import io.github.amichailides.merimna.user.dto.UserCreateDTO;
import io.github.amichailides.merimna.user.dto.UserUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateForCreate(UUID employeePublicId, UserCreateDTO dto) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (userRepository.existsByEmployeePublicId(employeePublicId)) {
            errors.put("employeeId", ErrorCode.EMPLOYEE_ALREADY_HAS_ACCOUNT.getMessageKey());
        }

        if (userRepository.existsByUsername(dto.username())) {
            errors.put("username", ErrorCode.USERNAME_ALREADY_EXISTS.getMessageKey());
        }

        if (userRepository.existsByEmail(dto.email())) {
            errors.put("email", ErrorCode.EMAIL_ALREADY_EXISTS.getMessageKey());
        }

        throwIfConflicts(errors);
    }

    public void validateForUpdate(UserUpdateDTO dto, User existing) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (emailChanged(dto, existing)
                && userRepository.existsByEmailAndPublicIdNot(dto.email(), existing.getPublicId())) {
            errors.put("email", ErrorCode.EMAIL_ALREADY_EXISTS.getMessageKey());
        }

        throwIfConflicts(errors);
    }

    private boolean emailChanged(UserUpdateDTO dto, User existing) {
        return dto.email() != null && !dto.email().equals(existing.getEmail());
    }

    private void throwIfConflicts(Map<String, String> errors) {
        if (!errors.isEmpty()) {
            throw new ConflictValidationException(errors);
        }
    }
}
