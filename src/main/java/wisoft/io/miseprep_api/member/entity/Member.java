package wisoft.io.miseprep_api.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wisoft.io.miseprep_api.global.entity.BaseEntity;
import wisoft.io.miseprep_api.member.entity.enums.OauthProvider;

@Getter
@Entity
@Table(
        name = "members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"oauth_provider", "oauth_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OauthProvider oauthProvider;

    @Column(nullable = false)
    private String oauthId;

    public static Member create(String email, String username, String profileImageUrl,
                                OauthProvider oauthProvider, String oauthId) {
        Member member = new Member();
        member.email = email;
        member.username = username;
        member.profileImageUrl = profileImageUrl;
        member.oauthProvider = oauthProvider;
        member.oauthId = oauthId;
        return member;
    }

    public void updateProfile(String username, String profileImageUrl) {
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }


}
