package io.github.amichailides.merimna.user;

import io.github.amichailides.merimna.common.response.PageResponse;
import io.github.amichailides.merimna.user.dto.*;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserReadOnlyDTO> create(
            @Validated(ValidationGroupSequence.class) @RequestBody UserCreateDTO dto) {

        UserReadOnlyDTO user = userService.create(dto);
        return ResponseEntity
                .created(buildLocationUri(user.id()))
                .body(user);
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserReadOnlyDTO>> getAllUsers(
            @ModelAttribute UserSearchDTO criteria,
            Pageable pageable) {

        Page<UserReadOnlyDTO> page = userService.getAllUsers(criteria, pageable);
        return ResponseEntity.ok(PageResponse.<UserReadOnlyDTO>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserReadOnlyDTO> getUserById(
            @PathVariable @Positive(message = "{user.id.positive}") Long id) {

        UserReadOnlyDTO result = userService.getUserById(id);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserReadOnlyDTO> updateUser(
            @PathVariable @Positive(message = "{user.id.positive}") Long id,
            @Validated(ValidationGroupSequence.class) @RequestBody UserUpdateDTO dto) {

        UserReadOnlyDTO result = userService.updateUser(id, dto);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Validated(ValidationGroupSequence.class) @RequestBody ChangePasswordDTO dto) {

        userService.changePassword(userDetails.getUsername(), dto);
        return ResponseEntity.noContent().build();
    }


    private URI buildLocationUri(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
