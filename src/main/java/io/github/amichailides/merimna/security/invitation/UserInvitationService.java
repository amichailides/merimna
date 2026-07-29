package io.github.amichailides.merimna.security.invitation;

import java.util.UUID;

public interface UserInvitationService {

    void createForEmployee(UUID employeePublicId);

    void acceptInvitation(
            String rawToken,
            String username,
            String password
    );

}