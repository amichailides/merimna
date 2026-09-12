package io.github.amichailides.merimna.security.event;

public record UserInvitationCreatedEvent(
        String email,
        String rawToken
) {
}