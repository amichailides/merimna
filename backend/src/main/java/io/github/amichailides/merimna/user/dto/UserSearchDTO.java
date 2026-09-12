package io.github.amichailides.merimna.user.dto;

import io.github.amichailides.merimna.domain.Role;
import io.github.amichailides.merimna.validation.annotations.OptionalNotBlank;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "UserSearchDTO", description = "Search filters for users")
public record UserSearchDTO(
        @Schema(
                description = "Search term. Applies to username and email, case-insensitive.",
                example = "john",
                minLength = 2,
                maxLength = 100
        )
        @OptionalNotBlank(message = "{user.search.q.blank}", groups = FirstOrder.class)
        @Size(min = 2, max = 100, message = "{user.search.q.size}", groups = SecondOrder.class)
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
