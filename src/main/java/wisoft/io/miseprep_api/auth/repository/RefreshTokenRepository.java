package wisoft.io.miseprep_api.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wisoft.io.miseprep_api.auth.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);
}
