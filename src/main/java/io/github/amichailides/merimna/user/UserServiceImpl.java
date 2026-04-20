package io.github.amichailides.merimna.user;

import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.employee.EmployeeRepository;
import io.github.amichailides.merimna.employee.exception.EmployeeNotFoundByIdException;
import io.github.amichailides.merimna.user.dto.*;
import io.github.amichailides.merimna.user.exception.InvalidCurrentPasswordException;
import io.github.amichailides.merimna.user.exception.NewPasswordMustBeDifferentException;
import io.github.amichailides.merimna.user.exception.UserNotFoundByEmailException;
import io.github.amichailides.merimna.user.exception.UserNotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Override
    @Transactional
    public UserReadOnlyDTO create(UserCreateDTO dto) {
        Long employeeId = dto.employeeId();
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundByIdException(employeeId));

        userValidator.validateForCreate(employeeId, dto);

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
    public UserReadOnlyDTO getUserById(Long id) {
        User user = getUserOrThrow(id);

        return userMapper.toReadOnlyDTO(user);

    }

    @Override
    @Transactional
    public UserReadOnlyDTO updateUser(Long id, UserUpdateDTO dto) {
        User user = getUserOrThrow(id);

        userValidator.validateForUpdate(id, dto, user);
         userMapper.updateEntity(user, dto);

         return userMapper.toReadOnlyDTO(user);
    }

    @Override
    @Transactional
    public void changePassword(String email,ChangePasswordDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundByEmailException::new);

        if (dto.newPassword().equals(dto.currentPassword())) {
            throw new NewPasswordMustBeDifferentException();
        }
        if(!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new InvalidCurrentPasswordException();
        }

        String encoded = passwordEncoder.encode(dto.newPassword());
        user.setEncodedPassword(encoded);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundByIdException::new);
    }
}
