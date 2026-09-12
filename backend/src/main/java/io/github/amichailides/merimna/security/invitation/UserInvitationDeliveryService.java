package io.github.amichailides.merimna.security.invitation;

public interface UserInvitationDeliveryService {

    void sendInvitation(String email, String rawToken);
}