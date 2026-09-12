package io.github.amichailides.merimna.user.audit;

import io.github.amichailides.merimna.audit.EntityChangeSet;
import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.user.dto.UserUpdateDTO;
import org.springframework.stereotype.Component;

@Component
public class UserChangeDetector {

    public EntityChangeSet detectChanges(User user, UserUpdateDTO dto) {
        return EntityChangeSet.builder()
                .trackIfPresent("email", user.getEmail(), dto.email())
                .build();
    }
}