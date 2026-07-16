package io.github.amichailides.merimna.user;

import io.github.amichailides.merimna.common.response.PageResponse;
import io.github.amichailides.merimna.user.dto.*;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
@Tag(
        name = "Users",
        description = "Manage application users and account credentials"
)
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserReadOnlyDTO> create(
            @Validated(ValidationGroupSequence.class) @RequestBody UserCreateDTO dto) {

        UserReadOnlyDTO user = userService.create(dto);
        return ResponseEntity
                .created(buildLocationUri(user.publicId()))
                .body(user);
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserReadOnlyDTO>> getAllUsers(
            @Validated(ValidationGroupSequence.class) @ModelAttribute UserSearchDTO criteria,
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

    @GetMapping("/{publicId}")
    public ResponseEntity<UserReadOnlyDTO> getUserByPublicId(
            @PathVariable UUID publicId) {

        UserReadOnlyDTO result = userService.getUserByPublicId(publicId);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{publicId}")
    public ResponseEntity<UserReadOnlyDTO> updateUser(
            @PathVariable UUID publicId,
            @Validated(ValidationGroupSequence.class) @RequestBody UserUpdateDTO dto) {

        UserReadOnlyDTO result = userService.updateUser(publicId, dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/me")
    public UserReadOnlyDTO getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {

        return userService.getByEmail(userDetails.getUsername());
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Validated(ValidationGroupSequence.class) @RequestBody ChangePasswordDTO dto) {

        userService.changePassword(userDetails.getUsername(), dto);
        return ResponseEntity.noContent().build();
    }


    private URI buildLocationUri(UUID publicId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{publicId}")
                .buildAndExpand(publicId)
                .toUri();
    }
}
