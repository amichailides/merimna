package io.github.amichailides.merimna.security.invitation;

import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.security.exception.InvalidUserInvitationException;
import io.github.amichailides.merimna.security.exception.UserInvitationAlreadyAcceptedException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(
        name = "user_invitations",
        indexes = {
                @Index(
                        name = "idx_user_invitations_employee_id",
                        columnList = "employee_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(
            name = "token_hash",
            nullable = false,
            unique = true,
            length = 64,
            updatable = false
    )
    private String tokenHash;

    @Column(
            name = "account_email",
            nullable = false,
            updatable = false
    )
    private String accountEmail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "accepted_at")
    private Instant acceptedAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    private UserInvitation(
            Employee employee,
            String accountEmail,
            String tokenHash,
            Instant createdAt,
            Instant expiresAt
    ) {
        this.employee = employee;
        this.accountEmail = accountEmail;
        this.tokenHash = tokenHash;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public static UserInvitation createFor(
            Employee employee,
            String accountEmail,
            String tokenHash,
            Instant now,
            Instant expiresAt
    ) {
        return new UserInvitation(
                employee,
                accountEmail,
                tokenHash,
                now,
                expiresAt
        );
    }

    public boolean isAccepted() {
        return acceptedAt != null;
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public boolean isExpired(Instant now) {
        return !expiresAt.isAfter(now);
    }

    public boolean isValid(Instant now) {
        return !isAccepted()
                && !isRevoked()
                && !isExpired(now);
    }

    public void accept(Instant now) {
        if (!isValid(now)) {
            throw new InvalidUserInvitationException();
        }

        this.acceptedAt = now;
    }

    public void revoke(Instant now) {
        if (isAccepted()) {
            throw new UserInvitationAlreadyAcceptedException();
        }

        if (isRevoked()) {
            return;
        }

        this.revokedAt = now;
    }
}