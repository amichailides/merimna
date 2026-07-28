package io.github.amichailides.merimna.security.invitation;

import java.util.UUID;

public interface UserInvitationService {

    void createForEmployee(UUID employeePublicId);
}