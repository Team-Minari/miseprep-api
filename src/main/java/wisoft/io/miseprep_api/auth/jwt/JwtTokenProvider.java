package wisoft.io.miseprep_api.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final JwtParser jwtParser;
    private final SecretKey secretKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.secret()));
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    public String createAccessToken(Long memberId) {
        return createToken(memberId, jwtProperties.accessExpiration());
    }

    public String createRefreshToken(Long memberId) {
        return createToken(memberId, jwtProperties.refreshExpiration());
    }

    public boolean validateToken(String token) {
        try {
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        return Long.parseLong(parseToken(token).getSubject());
    }

    private String createToken(Long memberId, long expiration) {
        Date now = new Date();

        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration))
                .signWith(secretKey)
                .compact();
    }

    private Claims parseToken(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }
}
