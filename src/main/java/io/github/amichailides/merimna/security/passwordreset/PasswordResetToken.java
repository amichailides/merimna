package io.github.amichailides.merimna.security.passwordreset;

import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.security.exception.PasswordResetTokenAlreadyUsedException;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "password_reset_tokens",
        indexes = {
                @Index(name = "idx_password_reset_tokens_token_hash", columnList = "token_hash"),
                @Index(name = "idx_password_reset_tokens_user_id", columnList = "user_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_hash", nullable = false, unique = true, length = 64, updatable = false)
    private String tokenHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "used_at")
    private Instant usedAt;

    public boolean isUsed() {
        return usedAt != null;
    }

    public boolean isExpired(Instant now) {
        return !expiresAt.isAfter(now);
    }

    public boolean isValid(Instant now) {
        return !isUsed() && !isExpired(now);
    }

    public void markUsed(Instant now) {
        if (isUsed()) {
            throw new PasswordResetTokenAlreadyUsedException();
        }

        this.usedAt = now;
    }
}