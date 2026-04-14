package io.github.amichailides.merimna.user;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.ConflictValidationException;
import io.github.amichailides.merimna.user.dto.UserCreateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateForCreate(Long employeeId, UserCreateDTO dto) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (userRepository.existsByEmployeeId(employeeId)) {
            errors.put("employeeId", ErrorCode.EMPLOYEE_ALREADY_HAS_ACCOUNT.getMessageKey());
        }
        if (userRepository.existsByUsername(dto.username())) {
            errors.put("username", ErrorCode.USERNAME_ALREADY_EXISTS.getMessageKey());
        }
        if (userRepository.existsByEmail(dto.email())) {
            errors.put("email", ErrorCode.EMAIL_ALREADY_EXISTS.getMessageKey());
        }

        if (!errors.isEmpty()) {
            throw new ConflictValidationException(errors);
        }
    }
}
