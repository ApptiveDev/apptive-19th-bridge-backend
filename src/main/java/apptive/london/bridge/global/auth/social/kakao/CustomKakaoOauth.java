package apptive.london.bridge.global.auth.social.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomKakaoOauth {
    @Value("${social.kakao.token_uri}")
    private String TOKEN_URI;

    @Value("${social.kakao.redirect_uri}")
    private String REDIRECT_URI;

    @Value("${social.kakao.grant_type}")
    private String GRANT_TYPE;

    @Value("${social.kakao.client_id}")
    private String CLIENT_ID;

    public String getRequestURI(String code) {
        return TOKEN_URI + "?grant_type=" + GRANT_TYPE
                + "&client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&code=" + code;
    }
}
