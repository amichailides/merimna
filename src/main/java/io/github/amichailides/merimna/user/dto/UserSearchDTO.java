package io.github.amichailides.merimna.user.dto;

import io.github.amichailides.merimna.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record UserSearchDTO(
        @Schema(
                description = "Search term (applies to username and email, case-insensitive)",
                example = "John",
                maxLength = 100
        )
        @Size(max = 100, message = "{user.search.q.size}")
        String q,

        @Schema(
                description = "Filter by user role",
                example = "ADMIN"
        )
        Role role,

        @Schema(
                description = "Include inactive users in results",
                example = "false",
                defaultValue = "false"
        )
        Boolean includeInactive
) {}
