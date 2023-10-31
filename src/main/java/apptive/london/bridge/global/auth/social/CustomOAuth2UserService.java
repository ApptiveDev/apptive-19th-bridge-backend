package apptive.london.bridge.global.auth.social;

import apptive.london.bridge.domain.user.entity.Role;
import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth2 서드파티 필드
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        String email = attributes.getEmail();

        // oauth 유효성 검사
        validate(email);

        // 사용자 검색
        User user = userRepository.findByEmail(email).get();

        // 신규 사용자라면 새로 생성
        if (user == null) {
            user = User.builder()
                    .email(email)
                    .password(null)
                    .role(Role.USER)
                    .build();

            userRepository.save(user);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private void validate(String email) {
        if (email == null) {
            throw new IllegalArgumentException("서드파티의 응답에 email이 존재하지 않습니다.");
        }
    }
}
