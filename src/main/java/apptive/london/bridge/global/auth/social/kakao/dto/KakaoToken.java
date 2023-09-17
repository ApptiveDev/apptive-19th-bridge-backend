package apptive.london.bridge.global.auth.social.kakao.dto;

import lombok.Data;

@Data
public class KakaoToken {
    private String token_type;
    private String access_token;
    private String id_token;
    private String expires_in;
    private String refresh_token;
    private Integer refresh_token_expires_in;
    private String scope;
}

