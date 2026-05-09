package io.github.amichailides.merimna.user;

import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.employee.EmployeeRepository;
import io.github.amichailides.merimna.employee.exception.EmployeeNotFoundByPublicIdException;
import io.github.amichailides.merimna.user.dto.*;
import io.github.amichailides.merimna.user.exception.InvalidCurrentPasswordException;
import io.github.amichailides.merimna.user.exception.NewPasswordMustBeDifferentException;
import io.github.amichailides.merimna.user.exception.UserNotFoundByEmailException;
import io.github.amichailides.merimna.user.exception.UserNotFoundByPublicIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Override
    @Transactional
    public UserReadOnlyDTO create(UserCreateDTO dto) {
        UUID employeePublicId = dto.employeePublicId();
        Employee employee = employeeRepository.findByPublicId(employeePublicId)
                .orElseThrow(() -> new EmployeeNotFoundByPublicIdException(employeePublicId));

        userValidator.validateForCreate(employeePublicId, dto);

        User user = User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(dto.role())
                .active(true)
                .employee(employee)
                .build();

        return userMapper.toReadOnlyDTO(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserReadOnlyDTO> getAllUsers(UserSearchDTO criteria, Pageable pageable) {

        Boolean activeFilter = Boolean.TRUE.equals(criteria.includeInactive())
                ? null
                : Boolean.TRUE;

        Specification<User> spec = Specification.where(
                UserSpecifications.searchByUsernameOrEmail(criteria.q())
                        .and(UserSpecifications.hasRole(criteria.role()))
                        .and(UserSpecifications.isActive(activeFilter))
        );

        return userRepository.findAll(spec, pageable)
                .map(userMapper::toReadOnlyDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public UserReadOnlyDTO getUserByPublicId(UUID publicId) {
        User user = getUserOrThrow(publicId);

        return userMapper.toReadOnlyDTO(user);
    }

    @Override
    @Transactional
    public UserReadOnlyDTO updateUser(UUID publicId, UserUpdateDTO dto) {
        User user = getUserOrThrow(publicId);

        userValidator.validateForUpdate(dto, user);
        userMapper.updateEntity(user, dto);

        return userMapper.toReadOnlyDTO(user);
    }

    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundByEmailException::new);

        if (dto.newPassword().equals(dto.currentPassword())) {
            throw new NewPasswordMustBeDifferentException();
        }
        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new InvalidCurrentPasswordException();
        }

        String encoded = passwordEncoder.encode(dto.newPassword());
        user.setEncodedPassword(encoded);
    }

    @Override
    @Transactional(readOnly = true)
    public UserReadOnlyDTO getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundByEmailException::new);

        return userMapper.toReadOnlyDTO(user);
    }

    private User getUserOrThrow(UUID publicId) {
        return userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new UserNotFoundByPublicIdException(publicId));
    }
}
