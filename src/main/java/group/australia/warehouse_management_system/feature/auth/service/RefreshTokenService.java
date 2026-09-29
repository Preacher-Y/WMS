package group.australia.warehouse_management_system.feature.auth.service;

import group.australia.warehouse_management_system.feature.auth.entity.RefreshTokenEntity;
import group.australia.warehouse_management_system.feature.auth.repository.RefreshTokenJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenJpaRepository repository;
    private final long refreshExpirationMs;

    public RefreshTokenService(
            RefreshTokenJpaRepository repository,
            @Value("${app.jwt.refresh-token-expiration-ms:604800000}") long refreshExpirationMs
    ) {
        this.repository = repository;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public record RotationResult(String newToken, String userEmail) {}

    public String create(String userEmail) {
        var raw = UUID.randomUUID().toString();
        repository.save(RefreshTokenEntity.builder()
                .tokenHash(hash(raw))
                .userEmail(userEmail)
                .expiresAt(LocalDateTime.now().plus(refreshExpirationMs, ChronoUnit.MILLIS))
                .revoked(false)
                .familyId(UUID.randomUUID().toString())
                .build());
        return raw;
    }

    @Transactional
    public RotationResult useAndRotate(String rawToken) {
        var entity = repository.findByTokenHash(hash(rawToken))
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        if (entity.isRevoked()) {
            revokeFamily(entity.getFamilyId());
            throw new BadCredentialsException("Refresh token reuse detected — all sessions revoked");
        }

        if (entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("Refresh token expired");
        }

        entity.setRevoked(true);
        repository.save(entity);

        var newRaw = UUID.randomUUID().toString();
        repository.save(RefreshTokenEntity.builder()
                .tokenHash(hash(newRaw))
                .userEmail(entity.getUserEmail())
                .expiresAt(LocalDateTime.now().plus(refreshExpirationMs, ChronoUnit.MILLIS))
                .revoked(false)
                .familyId(entity.getFamilyId())
                .build());

        return new RotationResult(newRaw, entity.getUserEmail());
    }

    public void revoke(String rawToken) {
        repository.findByTokenHash(hash(rawToken)).ifPresent(e -> {
            e.setRevoked(true);
            repository.save(e);
        });
    }

    private void revokeFamily(String familyId) {
        repository.findAllByFamilyId(familyId).forEach(e -> {
            e.setRevoked(true);
            repository.save(e);
        });
    }

    private String hash(String raw) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            var bytes = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
