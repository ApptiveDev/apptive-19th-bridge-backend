package apptive.london.bridge.global.auth.social.kakao.controller;

import apptive.london.bridge.global.auth.social.SocialAuthenticationResponse;
import apptive.london.bridge.global.auth.social.kakao.dto.KakaoUserInfo;
import apptive.london.bridge.global.auth.social.kakao.service.CustomKakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class CustomKakaoController {

    private final CustomKakaoService customKakaoService;

    @PostMapping("/authenticate/kakao/code/v1")
    public ResponseEntity<SocialAuthenticationResponse> kakaoAuthenticateByCode(@RequestParam("code") String code) {
        KakaoUserInfo kakaoUserInfo = customKakaoService.getKakaoInfoByCode(code);
        SocialAuthenticationResponse authenticationResponse = customKakaoService.authenticate(kakaoUserInfo.getKakao_account().getEmail());

        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @PostMapping("/authenticate/kakao/token/v1")
    public ResponseEntity<SocialAuthenticationResponse> kakaoAuthenticateByToken(@RequestBody Map<String, String> body) {
        KakaoUserInfo userInfo = customKakaoService.getKakaoInfoByToken(body.get("token"));
        SocialAuthenticationResponse authenticationResponse = customKakaoService.authenticate(userInfo.getKakao_account().getEmail());

        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }
}
