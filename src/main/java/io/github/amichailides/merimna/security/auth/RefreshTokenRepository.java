package io.github.amichailides.merimna.security.auth;

import io.github.amichailides.merimna.domain.RefreshToken;
import io.github.amichailides.merimna.domain.RevocationReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Modifying
    @Query("""
    UPDATE RefreshToken t
    SET t.revokedAt = :now,
        t.revocationReason = :reason
    WHERE t.user.id = :userId
      AND t.revokedAt IS NULL
      AND t.expiresAt > :now
""")
    int revokeAllActiveTokensForUser(
            @Param("userId") Long userId,
            @Param("reason") RevocationReason reason,
            @Param("now") Instant now
    );
}
