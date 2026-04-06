package wisoft.io.miseprep_api.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wisoft.io.miseprep_api.auth.client.KakaoClient;
import wisoft.io.miseprep_api.auth.client.KakaoClient.KakaoUserResponse;
import wisoft.io.miseprep_api.auth.dto.request.TestLoginRequest;
import wisoft.io.miseprep_api.auth.dto.response.AuthResponse;
import wisoft.io.miseprep_api.auth.dto.request.TokenRefreshRequest;
import wisoft.io.miseprep_api.auth.entity.RefreshToken;
import wisoft.io.miseprep_api.auth.jwt.JwtTokenProvider;
import wisoft.io.miseprep_api.auth.repository.RefreshTokenRepository;
import wisoft.io.miseprep_api.global.exception.BusinessException;
import wisoft.io.miseprep_api.global.exception.ErrorCode;

import wisoft.io.miseprep_api.member.entity.Member;
import wisoft.io.miseprep_api.member.entity.enums.OauthProvider;
import wisoft.io.miseprep_api.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final KakaoClient kakaoClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthResponse login(String code) {
        String kakaoAccessToken = kakaoClient.getAccessToken(code);
        KakaoUserResponse kakaoUser = kakaoClient.getUserInfo(kakaoAccessToken);

        Member member = memberRepository
                .findByOauthProviderAndOauthId(OauthProvider.KAKAO, String.valueOf(kakaoUser.id()))
                .orElseGet(() -> memberRepository.save(Member.create(
                        kakaoUser.kakaoAccount().email(),
                        kakaoUser.kakaoAccount().profile().nickname(),
                        kakaoUser.kakaoAccount().profile().profileImageUrl(),
                        OauthProvider.KAKAO,
                        String.valueOf(kakaoUser.id())
                )));

        String jwtAccessToken = jwtTokenProvider.createAccessToken(member.getId());
        String jwtRefreshToken = jwtTokenProvider.createRefreshToken(member.getId());
        saveOrRotateRefreshToken(member, jwtRefreshToken);

        return new AuthResponse(jwtAccessToken, jwtRefreshToken);
    }

    public AuthResponse refresh(TokenRefreshRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN));

        String newAccessToken = jwtTokenProvider.createAccessToken(refreshToken.getMember().getId());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(refreshToken.getMember().getId());
        refreshToken.rotate(newRefreshToken);

        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    public AuthResponse testLogin(TestLoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseGet(() -> memberRepository.save(Member.create(
                        request.email(),
                        request.username(),
                        request.profileImageUrl(),
                        OauthProvider.KAKAO,
                        request.email()
                )));

        String jwtAccessToken = jwtTokenProvider.createAccessToken(member.getId());
        String jwtRefreshToken = jwtTokenProvider.createRefreshToken(member.getId());
        saveOrRotateRefreshToken(member, jwtRefreshToken);

        return new AuthResponse(jwtAccessToken, jwtRefreshToken);
    }

    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }

    private void saveOrRotateRefreshToken(Member member, String token) {
        refreshTokenRepository.findByMemberId(member.getId())
                .ifPresentOrElse(
                        rt -> rt.rotate(token),
                        () -> refreshTokenRepository.save(RefreshToken.create(member, token))
                );
    }
}
