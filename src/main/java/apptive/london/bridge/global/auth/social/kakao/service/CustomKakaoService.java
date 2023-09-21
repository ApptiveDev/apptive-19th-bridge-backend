package apptive.london.bridge.global.auth.social.kakao.service;

import apptive.london.bridge.domain.user.entity.Role;
import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.domain.user.repositoy.UserRepository;
import apptive.london.bridge.global.auth.local.data.AuthenticationResponse;
import apptive.london.bridge.global.auth.local.data.Token;
import apptive.london.bridge.global.auth.local.data.TokenType;
import apptive.london.bridge.global.auth.local.repository.TokenRepository;
import apptive.london.bridge.global.auth.social.SocialAuthenticationResponse;
import apptive.london.bridge.global.auth.social.kakao.CustomKakaoOauth;
import apptive.london.bridge.global.auth.social.kakao.dto.KakaoToken;
import apptive.london.bridge.global.auth.social.kakao.dto.KakaoUserInfo;
import apptive.london.bridge.global.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomKakaoService {

    private final CustomKakaoOauth customKakaoOauth;
    private final WebClient webClient;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    private final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    public SocialAuthenticationResponse authenticate(String email) {
        // 이메일이 없을때
        if (email == null) {
            throw new IllegalArgumentException("카카오 유저 정보를 찾을 수 없습니다.");
        }

        // 유저가 존재하지 않으면 생성
        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user;
        boolean isRegistered = true;
        if (optionalUser.isEmpty()) {
            user = User.builder()
                    .email(email)
                    .password(null)
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
            isRegistered = false;
        } else {
            user = optionalUser.get();
        }

        // 토큰 생성
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // 기존 토큰 만료
        revokeAllUserTokens(user);

        // 토큰 저장
        saveUserToken(user, jwtToken);

        return SocialAuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .isRegistered(isRegistered)
                .build();
    }

    public KakaoUserInfo getKakaoInfoByCode(String code) {
        KakaoToken token = getAccessToken(code);
        return getKakaoInfoByToken(token.getAccess_token());
    }

    public KakaoUserInfo getKakaoInfoByToken(String token) {
        KakaoUserInfo kakaoUserInfo = webClient.get()
                .uri(KAKAO_USER_INFO_URI)
                .header("Authorization", "Bearer " + token)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block();

        return kakaoUserInfo;
    }

    public KakaoToken getAccessToken(String code) {
        String uri = customKakaoOauth.getRequestURI(code);

        var response = webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(KakaoToken.class);

        return response.blockFirst();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
