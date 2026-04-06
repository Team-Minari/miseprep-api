package wisoft.io.miseprep_api.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wisoft.io.miseprep_api.member.entity.Member;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "refresh_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static RefreshToken create(Member member, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.member = member;
        refreshToken.token = token;
        refreshToken.createdAt = LocalDateTime.now();
        return refreshToken;
    }

    public void rotate(String newToken) {
        this.token = newToken;
        this.createdAt = LocalDateTime.now();
    }
}
