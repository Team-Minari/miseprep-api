package wisoft.io.miseprep_api.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wisoft.io.miseprep_api.member.entity.Member;
import wisoft.io.miseprep_api.member.entity.enums.OauthProvider;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauthProviderAndOauthId(OauthProvider oauthProvider, String oauthId);

    Optional<Member> findByEmail(String email);
}
